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
import com.hexagraph.cropchain.util.uriToFile
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltWorker
class WorkManagerUploadPhotoToPinata @AssistedInject constructor(
    @Assisted ctx: Context,
    @Assisted param: WorkerParameters,
    private val cropRepositoryImpl: CropRepository,
    private val pinataRepositoryImpl: PinataRepository
) : CoroutineWorker(ctx, param) {

    override suspend fun doWork(): Result {
        return try {
            val crops = cropRepositoryImpl.getPinataUploadCrops()
            if (crops.isEmpty()) return Result.success()

            setForeground(createProgressForeground("Uploading to Pinata", 0, crops.size))

            var failures = 0
            crops.forEachIndexed { index, crop ->
                updateNotification(index + 1, crops.size)
                if (crop.uploadedToPinata == 0) {
                    crop.uploadedToPinata = -1
                    crop.uploadProgress = 0
                    cropRepositoryImpl.updateCrop(crop)
                }

                var lastUpdateTime = 0L

                val result = pinataRepositoryImpl.uploadImageToPinata(
                    uriToFile(applicationContext, crop.uid),
                    onProgress = { progress ->
                        Log.d("PinataUploadProgress", "crop id : ${crop.id} $progress% uploaded")
                        if (progress > crop.uploadProgress)
                            crop.uploadProgress = progress
                        val now = System.currentTimeMillis()
                        if (now - lastUpdateTime >= 300) {
                            lastUpdateTime = now
                            CoroutineScope(Dispatchers.IO).launch {
                                cropRepositoryImpl.updateCrop(crop)
                            }
                        }
                    })
                result.onSuccess { url ->
                    crop.uploadedToPinata = 1
                    crop.url = url
                    cropRepositoryImpl.updateCrop(crop)
                }
                result.onFailure {
                    crop.uploadedToPinata = 0
                    crop.uploadProgress = 0 // Hello
                    cropRepositoryImpl.updateCrop(crop)
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