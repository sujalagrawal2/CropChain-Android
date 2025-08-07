package com.hexagraph.cropchain.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hexagraph.cropchain.domain.model.Crop
import com.hexagraph.cropchain.domain.model.CropImages
import com.hexagraph.cropchain.domain.model.MetaMaskAccounts
import com.hexagraph.cropchain.domain.model.RecentActivity

@Database(entities = [CropImages::class, MetaMaskAccounts::class, Crop::class, RecentActivity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cropImageDao(): CropImageDao
    abstract fun cropTableDao(): CropTableDao

    abstract fun metaMaskDao(): MetaMaskDao
    abstract fun recentActivityDao(): RecentActivityDao
}