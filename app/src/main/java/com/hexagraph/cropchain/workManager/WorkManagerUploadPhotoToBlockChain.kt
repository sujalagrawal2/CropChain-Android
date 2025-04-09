package com.hexagraph.cropchain.workManager

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.hexagraph.cropchain.Web3J
import com.hexagraph.cropchain.domain.repository.CropRepository
import com.hexagraph.cropchain.util.uploadImageToPinata
import com.hexagraph.cropchain.util.uriToFile
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject


@HiltWorker
class WorkManagerUploadPhotoToBlockChain @AssistedInject constructor(
    @Assisted ctx: Context,
    @Assisted param: WorkerParameters,
    private val cropRepositoryImpl: CropRepository,
    private val web: Web3J
) : CoroutineWorker(ctx, param) {

    override suspend fun doWork(): Result {
        return try {
            setForeground(getForegroundInfo())
            val cropList = cropRepositoryImpl.getBlockChainUploadCrops()
            val total = cropList.size
            var cnt = 0
            var failure: Boolean = false
            makeStatusNotification("0 / $total", applicationContext, cnt, total)
            cropList.forEach { crop ->
                val result = web.uploadImage(crop.url!!)
                result.onSuccess {
                    cnt++
                    crop.uploadedToBlockChain = true
                    cropRepositoryImpl.updateCrop(crop)
                    makeStatusNotification("$cnt / $total", applicationContext, cnt, total)
                }
                result.onFailure {
                    failure = true
                }
            }
            if (!failure)
                Result.success()
            else Result.retry()
        } catch (e: Exception) {
            Log.e("Worker", "Error uploading", e)
            Result.retry()
        }
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(
            2,
            createForegroundNotification(applicationContext, "Uploading..", 0, 0)
        )
    }

}
