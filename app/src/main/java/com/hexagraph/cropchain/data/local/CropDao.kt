package com.hexagraph.cropchain.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.hexagraph.cropchain.domain.model.Crop
import kotlinx.coroutines.flow.Flow

@Dao
interface CropDao {
    @Query("SELECT * FROM crop")
    fun getAllCrops(): Flow<List<Crop>>

    @Insert
    suspend fun insertCrop(crop: Crop)

    @Query("SELECT * FROM crop ORDER BY date DESC")
    fun getAllCrop(): Flow<List<Crop>>

    @Query("SELECT * FROM crop WHERE uploadedToPinata =  0 OR uploadedToPinata = -1")
    suspend fun getPinataUploadCrops(): List<Crop>

    @Query("SELECT * FROM crop WHERE uploadedToBlockChain =  0 AND uploadedToPinata = 1")
    suspend fun getBlockChainUploadCrops(): List<Crop>

    @Update
    suspend fun updateCrop(crop: Crop)

    @Query("SELECT * FROM crop WHERE uploadedToBlockChain=1 AND uploadedToPinata=1")
    fun getAllUploadedCrops(): Flow<List<Crop>>

}