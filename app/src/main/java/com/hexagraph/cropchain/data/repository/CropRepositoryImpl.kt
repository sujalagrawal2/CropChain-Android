package com.hexagraph.cropchain.data.repository

import com.hexagraph.cropchain.data.local.CropDao
import com.hexagraph.cropchain.domain.model.Crop
import com.hexagraph.cropchain.domain.repository.CropRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CropRepositoryImpl @Inject constructor(
    private val cropDao: CropDao
): CropRepository {
    override fun getAllCrops(): Flow<List<Crop>> {
        return cropDao.getAllCrops()
    }
}