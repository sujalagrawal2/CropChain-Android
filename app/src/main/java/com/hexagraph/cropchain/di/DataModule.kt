package com.hexagraph.cropchain.di

import android.app.Application
import androidx.room.Room
import com.hexagraph.cropchain.data.local.AppDatabase
import com.hexagraph.cropchain.data.local.CropDao
import com.hexagraph.cropchain.data.repository.CropRepositoryImpl
import com.hexagraph.cropchain.domain.repository.CropRepository
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
    fun provideCropRepoImpl(cropRepositoryImpl: CropRepositoryImpl): CropRepository {
        return cropRepositoryImpl
    }

    @Provides
    fun provideCropDao(database: AppDatabase): CropDao {
        return database.cropDao()
    }
}