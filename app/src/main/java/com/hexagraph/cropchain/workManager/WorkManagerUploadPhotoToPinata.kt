package com.hexagraph.cropchain.workManager

import android.content.Context
import android.content.pm.ServiceInfo
import android.os.Build
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.hexagraph.cropchain.domain.repository.CropRepository
import com.hexagraph.cropchain.domain.repository.PinataRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

@HiltWorker
class WorkManagerUploadPhotoToPinata @AssistedInject constructor(
    @Assisted ctx: Context,
    @Assisted param: WorkerParameters,
    private val cropRepositoryImpl: CropRepository,
    private val pinataRepositoryImpl: PinataRepository
) : CoroutineWorker(ctx, param) {

    override suspend fun doWork(): Result {
        return try {
            // Get pending Pinata uploads using the new repository method
            val crops = cropRepositoryImpl.getPendingPinataUploads()
            if (crops.isEmpty()) return Result.success()

            setForeground(createProgressForeground("Uploading to Pinata", 0, crops.size))

            var failures = 0
            crops.forEachIndexed { index, crop ->
                updateNotification(index + 1, crops.size)

                // Reset failed uploads to pending state
                if (crop.uploadedToPinata == 0) {
                    cropRepositoryImpl.updatePinataUploadStatus(crop.id, -1, null)
                    cropRepositoryImpl.updateUploadProgress(crop.id, 0)
                }

                var lastUpdateTime = 0L

                // Use the localPath to get the file instead of converting from UID
                val file = File(crop.localPath)
                if (!file.exists()) {
                    Log.e("PinataWorker", "File not found at path: ${crop.localPath}")
                    cropRepositoryImpl.updatePinataUploadStatus(crop.id, 0, null)
                    failures++
                }

                val result = pinataRepositoryImpl.uploadImageToPinata(
                    file,
                    onProgress = { progress ->
                        Log.d("PinataUploadProgress", "crop id : ${crop.id} $progress% uploaded")

                        // Throttle updates to avoid too frequent database writes
                        val now = System.currentTimeMillis()
                        if (now - lastUpdateTime >= 300) {
                            lastUpdateTime = now
                            CoroutineScope(Dispatchers.IO).launch {
                                cropRepositoryImpl.updateUploadProgress(crop.id, progress)
                            }
                        }
                    }
                )

                result.onSuccess { ipfsHash ->
                    val ipfsUrl = "https://gateway.pinata.cloud/ipfs/$ipfsHash"
                    cropRepositoryImpl.updatePinataUploadStatus(crop.id, 1, ipfsUrl)
                    Log.d("PinataWorker", "Successfully uploaded crop ${crop.id}: $ipfsUrl")
                }

                result.onFailure { exception ->
                    cropRepositoryImpl.updatePinataUploadStatus(crop.id, 0, null)
                    cropRepositoryImpl.updateUploadProgress(crop.id, 0)
                    Log.e("PinataWorker", "Failed to upload crop ${crop.id}: ${exception.message}")
                    failures++
                }
            }

            if (failures > 0) Result.retry() else Result.success()

        } catch (e: Exception) {
            Log.e("PinataWorker", "Unexpected error", e)
            Result.retry()
        }
    }

    private suspend fun updateNotification(current: Int, total: Int) {
        val notification = createForegroundNotification2(
            applicationContext,
            "Uploading $current of $total",
            current,
            total
        )
        val info =
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q)
                ForegroundInfo(
                    1,
                    notification,
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
                ) else
                ForegroundInfo(
                    1,
                    notification
                )
        setForeground(info)
    }

    private fun createProgressForeground(title: String, progress: Int, total: Int): ForegroundInfo {
        val notification = createForegroundNotification2(applicationContext, title, progress, total)
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q)
            return ForegroundInfo(1, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC)
        return ForegroundInfo(1, notification)
    }
}