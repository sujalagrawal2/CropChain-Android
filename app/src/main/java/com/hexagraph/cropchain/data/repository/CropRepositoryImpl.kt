package com.hexagraph.cropchain.data.repository

import com.hexagraph.cropchain.data.local.CropImageDao
import com.hexagraph.cropchain.domain.model.CropImages
import com.hexagraph.cropchain.domain.repository.CropRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CropRepositoryImpl @Inject constructor(
    private val cropImageDao: CropImageDao
) : CropRepository {

    override suspend fun insertCropImage(cropImage: CropImages): Long {
        return cropImageDao.insertCropImage(cropImage)
    }

    override fun getCropImagesByCropId(cropId: Long): Flow<List<CropImages>> {
        return cropImageDao.getCropImagesByCropId(cropId)
    }

    override suspend fun getCropImagesByCropIdSync(cropId: Long): List<CropImages> {
        return cropImageDao.getCropImagesByCropIdSync(cropId)
    }

    override fun getAllCropImages(): Flow<List<CropImages>> {
        return cropImageDao.getAllCropImages()
    }

    override suspend fun getPendingPinataUploads(): List<CropImages> {
        return cropImageDao.getPendingPinataUploads()
    }

    override suspend fun getSuccessfulPinataUploads(): List<CropImages> {
        return cropImageDao.getSuccessfulPinataUploads()
    }

    override suspend fun getCropImageById(imageId: Long): CropImages? {
        return cropImageDao.getCropImageById(imageId)
    }

    override suspend fun updateCropImage(cropImage: CropImages) {
        return cropImageDao.updateCropImage(cropImage)
    }

    override suspend fun updateUploadProgress(imageId: Long, progress: Int) {
        return cropImageDao.updateUploadProgress(imageId, progress)
    }

    override suspend fun updatePinataUploadStatus(imageId: Long, status: Int, url: String?) {
        return cropImageDao.updatePinataUploadStatus(imageId, status, url)
    }

    override suspend fun deleteCropImage(cropImage: CropImages) {
        return cropImageDao.deleteCropImage(cropImage)
    }

    override suspend fun deleteCropImageById(imageId: Long) {
        return cropImageDao.deleteCropImageById(imageId)
    }

    override suspend fun deleteAllCropImagesByCropId(cropId: Long) {
        return cropImageDao.deleteAllCropImagesByCropId(cropId)
    }

    override suspend fun getCropImagesCount(cropId: Long): Int {
        return cropImageDao.getCropImagesCount(cropId)
    }

    override suspend fun getUploadedCropImagesCount(cropId: Long): Int {
        return cropImageDao.getUploadedCropImagesCount(cropId)
    }
}