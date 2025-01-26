package com.hexagraph.cropchain.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hexagraph.cropchain.domain.model.Crop

@Database(entities = [Crop::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun cropDao(): CropDao
}