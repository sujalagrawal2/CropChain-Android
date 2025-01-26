package com.hexagraph.cropchain.data.local

import androidx.room.Dao
import androidx.room.Query
import com.hexagraph.cropchain.domain.model.Crop

@Dao
interface CropDao {

    @Query("SELECT * FROM crop")
    fun getAllCrops(): List<Crop>
}