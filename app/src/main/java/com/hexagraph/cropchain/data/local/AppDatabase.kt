package com.hexagraph.cropchain.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hexagraph.cropchain.domain.model.Crop
import com.hexagraph.cropchain.domain.model.MetaMaskAccounts

@Database(entities = [Crop::class, MetaMaskAccounts::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cropDao(): CropDao

    abstract fun metaMaskDao(): MetaMaskDao
}