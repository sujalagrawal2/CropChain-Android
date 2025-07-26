package com.hexagraph.cropchain.workManager

import android.content.Context
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.hexagraph.cropchain.data.repository.CropRepositoryImpl
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class WorkManagerRepository @Inject constructor(@ApplicationContext context: Context) {
    private val workManager = WorkManager.getInstance(context)

    fun count() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val workManager1 = OneTimeWorkRequestBuilder<WorkManagerUploadPhotoToPinata>() .setConstraints(constraints)
            .build()

        workManager.enqueue(workManager1)

    }
}