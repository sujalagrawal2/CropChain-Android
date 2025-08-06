package com.hexagraph.cropchain.domain.repository

import com.hexagraph.cropchain.domain.model.CropImages
import kotlinx.coroutines.flow.Flow

interface CropRepository {
    suspend fun insertCropImage(cropImage: CropImages): Long
    fun getCropImagesByCropId(cropId: Long): Flow<List<CropImages>>
    suspend fun getCropImagesByCropIdSync(cropId: Long): List<CropImages>
    fun getAllCropImages(): Flow<List<CropImages>>
    suspend fun getPendingPinataUploads(): List<CropImages>
    suspend fun getSuccessfulPinataUploads(): List<CropImages>
    suspend fun getCropImageById(imageId: Long): CropImages?
    suspend fun updateCropImage(cropImage: CropImages)
    suspend fun updateUploadProgress(imageId: Long, progress: Int)
    suspend fun updatePinataUploadStatus(imageId: Long, status: Int, url: String?)
    suspend fun deleteCropImage(cropImage: CropImages)
    suspend fun deleteCropImageById(imageId: Long)
    suspend fun deleteAllCropImagesByCropId(cropId: Long)
    suspend fun getCropImagesCount(cropId: Long): Int
    suspend fun getUploadedCropImagesCount(cropId: Long): Int
}
