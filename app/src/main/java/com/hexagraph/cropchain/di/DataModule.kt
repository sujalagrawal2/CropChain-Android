package com.hexagraph.cropchain.di

import android.app.Application
import androidx.room.Room
import com.hexagraph.cropchain.data.local.AppDatabase
import com.hexagraph.cropchain.data.local.CropDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(app, AppDatabase::class.java, "crop.db")
            .build()
    }

    @Provides
    fun provideCropDao(database: AppDatabase): CropDao {
        return database.cropDao()
    }
}