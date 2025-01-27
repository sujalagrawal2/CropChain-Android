package com.hexagraph.cropchain.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.hexagraph.cropchain.domain.model.Crop
import kotlinx.coroutines.flow.Flow

@Dao
interface CropDao {
    @Query("SELECT * FROM crop")
    fun getAllCrops(): Flow<List<Crop>>

    @Insert
    suspend fun insertCrop(crop: Crop)

}