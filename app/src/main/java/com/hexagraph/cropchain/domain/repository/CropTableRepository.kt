package com.hexagraph.cropchain.domain.repository

import com.hexagraph.cropchain.domain.model.Crop
import kotlinx.coroutines.flow.Flow

interface CropTableRepository {
    suspend fun insertCrop(crop: Crop): Long
    suspend fun getCropById(cropId: Long): Crop?
    fun getAllCrops(): Flow<List<Crop>>
    suspend fun getCropsNotUploadedToBlockchain(): List<Crop>
    suspend fun getCropsUploadedToBlockchain(): List<Crop>
    suspend fun updateCrop(crop: Crop)
    suspend fun updateReviewStatus(cropId: Long, reviewed: Boolean, review: String?)
    suspend fun updateCropDetails(cropId: Long, title: String, description: String, latitude: Double, longitude: Double, address: String)
    suspend fun updateBlockchainStatus(cropId: Long, uploaded: Boolean, txHash: String?)
    suspend fun deleteCrop(cropId: Long)
}
