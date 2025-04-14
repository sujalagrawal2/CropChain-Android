package com.hexagraph.cropchain.workManager

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.hexagraph.cropchain.domain.repository.CropRepository
import com.hexagraph.cropchain.util.uploadImageToPinata
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
    private val cropRepositoryImpl: CropRepository
) : CoroutineWorker(ctx, param) {
    //    override suspend fun doWork(): Result {
//        return try {
//            setForeground(getForegroundInfo())
//            val cropList = cropRepositoryImpl.getPinataUploadCrops()
//            val total = cropList.size
//            var cnt = 0
//            var failure: Boolean = false
//            makeStatusNotification("0 / $total", applicationContext, cnt, total)
//            cropList.forEach { crop ->
//                val result = uploadImageToPinata(uriToFile(applicationContext, crop.uid))
//                result.onSuccess { url ->
//                    crop.uploadedToPinata = true
//                    crop.url ="https://gateway.pinata.cloud/ipfs/"+url
//                    cropRepositoryImpl.updateCrop(crop)
//                    cnt++
//                    makeStatusNotification("$cnt / $total", applicationContext, cnt, total)
//                }
//                result.onFailure {
//                    failure = true
//                }
//            }
//            if (!failure)
//                Result.success()
//            else Result.retry()
//        } catch (e: Exception) {
//            Log.e("Worker", "Error uploading", e)
//            Result.retry()
//        }
//    }
//
//    override suspend fun getForegroundInfo(): ForegroundInfo {
//        return ForegroundInfo(
//            2,
//            createForegroundNotification(applicationContext, "Uploading..", 0, 0)
//        )
//    }
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
                    crop.uploadProgress=0
                    cropRepositoryImpl.updateCrop(crop)
                }
//            when (val uploadResult = pinata.uploadToPinata(crop.url.orEmpty())) {
//                is UploadResult.Success -> {
//                    crop.uploadedToPinata = true
//                    cropRepository.markUploaded(crop)
//                }
//                is UploadResult.Failure -> {
//                    failures++
//                    Log.e("PinataWorker", "Failed to upload: ${uploadResult.error}")
//                }
//            }
                var lastUpdateTime = 0L
                val result = uploadImageToPinata(
                    uriToFile(applicationContext, crop.uid),
                    onProgress = { progress ->
                        Log.d("PinataUploadProgress", "crop id : ${crop.id} $progress% uploaded")
                        if(progress>crop.uploadProgress)
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
        val info = ForegroundInfo(
            1,
            createForegroundNotification2(
                applicationContext,
                "Uploading $current of $total",
                current,
                total
            )
        )
        setForeground(info)
    }

    private fun createProgressForeground(title: String, progress: Int, total: Int): ForegroundInfo {
        val notification = createForegroundNotification2(applicationContext, title, progress, total)
        return ForegroundInfo(1, notification)
    }
}