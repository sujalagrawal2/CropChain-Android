package com.hexagraph.cropchain.domain.repository

import com.hexagraph.cropchain.domain.model.Crop
import kotlinx.coroutines.flow.Flow

interface CropRepository {
    fun getAllCrops(): Flow<List<Crop>>

    suspend fun insertCrop(crop: Crop)

    fun getAllCrop(): Flow<List<Crop>>

    suspend fun getPinataUploadCrops(): List<Crop>
    suspend fun getBlockChainUploadCrops(): List<Crop>

    suspend fun updateCrop(crop: Crop)

    fun getAllUploadedCrops(): Flow<List<Crop>>

}