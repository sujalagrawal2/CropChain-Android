package com.hexagraph.cropchain.domain.repository

import com.hexagraph.cropchain.domain.model.Crop
import kotlinx.coroutines.flow.Flow

interface CropRepository {
    fun getAllCrops(): Flow<List<Crop>>
}