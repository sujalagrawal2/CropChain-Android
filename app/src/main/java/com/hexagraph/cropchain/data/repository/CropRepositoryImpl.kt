package com.hexagraph.cropchain.data.repository

import com.hexagraph.cropchain.data.local.CropDao
import com.hexagraph.cropchain.domain.model.Crop
import com.hexagraph.cropchain.domain.repository.CropRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CropRepositoryImpl @Inject constructor(
    private val cropDao: CropDao
) : CropRepository {
    override fun getAllCrops(): Flow<List<Crop>> {
        return cropDao.getAllCrops()
    }

    override suspend fun insertCrop(crop: Crop) {
        return cropDao.insertCrop(crop)
    }

    override suspend fun getAllCrop(): List<Crop> {
        return cropDao.getAllCrop()
    }

    override suspend fun getPinataUploadCrops(): List<Crop> {
        return cropDao.getPinataUploadCrops()
    }

    override suspend fun getBlockChainUploadCrops(): List<Crop> {
        return cropDao.getBlockChainUploadCrops()
    }

    override suspend fun updateCrop(crop: Crop) {
        return cropDao.updateCrop(crop)
    }

    override fun getAllUploadedCrops(): Flow<List<Crop>> {
        return cropDao.getAllUploadedCrops()
    }
}