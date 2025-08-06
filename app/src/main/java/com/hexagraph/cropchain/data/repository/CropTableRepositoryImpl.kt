package com.hexagraph.cropchain.data.repository

import com.hexagraph.cropchain.data.local.CropTableDao
import com.hexagraph.cropchain.domain.model.Crop
import com.hexagraph.cropchain.domain.repository.CropTableRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CropTableRepositoryImpl @Inject constructor(
    private val cropTableDao: CropTableDao
) : CropTableRepository {

    override suspend fun insertCrop(crop: Crop): Long {
        return cropTableDao.insertCrop(crop)
    }

    override suspend fun getCropById(cropId: Long): Crop? {
        return cropTableDao.getCropById(cropId)
    }

    override fun getAllCrops(): Flow<List<Crop>> {
        return cropTableDao.getAllCrops()
    }

    override suspend fun getCropsNotUploadedToBlockchain(): List<Crop> {
        return cropTableDao.getCropsNotUploadedToBlockchain()
    }

    override suspend fun getCropsUploadedToBlockchain(): List<Crop> {
        return cropTableDao.getCropsUploadedToBlockchain()
    }

    override suspend fun updateCrop(crop: Crop) {
        return cropTableDao.updateCrop(crop)
    }

    override suspend fun updateReviewStatus(cropId: Long, reviewed: Boolean, review: String?) {
        return cropTableDao.updateReviewStatus(cropId, reviewed, review)
    }

    override suspend fun updateCropDetails(cropId: Long, title: String, description: String, latitude: Double, longitude: Double, address: String) {
        return cropTableDao.updateCropDetails(cropId, title, description, latitude, longitude, address)
    }

    override suspend fun updateBlockchainStatus(cropId: Long, uploaded: Boolean, txHash: String?) {
        return cropTableDao.updateBlockchainStatus(cropId, uploaded, txHash)
    }

    override suspend fun deleteCrop(cropId: Long) {
        return cropTableDao.deleteCrop(cropId)
    }
}
