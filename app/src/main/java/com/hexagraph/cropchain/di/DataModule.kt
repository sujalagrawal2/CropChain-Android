package com.hexagraph.cropchain.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.hexagraph.cropchain.data.local.AppDatabase
import com.hexagraph.cropchain.data.local.CropImageDao
import com.hexagraph.cropchain.data.local.CropTableDao
import com.hexagraph.cropchain.data.local.MetaMaskDao
import com.hexagraph.cropchain.data.local.RecentActivityDao
import com.hexagraph.cropchain.data.local.apppreferences.AppPreferences
import com.hexagraph.cropchain.data.metamask.MetaMask
import com.hexagraph.cropchain.data.repository.CropRepositoryImpl
import com.hexagraph.cropchain.data.repository.CropTableRepositoryImpl
import com.hexagraph.cropchain.data.repository.MetaMaskRepositoryImpl
import com.hexagraph.cropchain.data.repository.MetaMaskSDKRepositoryImpl
import com.hexagraph.cropchain.data.repository.PinataRepositoryImpl
import com.hexagraph.cropchain.data.repository.RecentActivityRepositoryImpl
import com.hexagraph.cropchain.domain.repository.CropRepository
import com.hexagraph.cropchain.domain.repository.CropTableRepository
import com.hexagraph.cropchain.domain.repository.MetaMaskRepository
import com.hexagraph.cropchain.domain.repository.MetaMaskSDKRepository
import com.hexagraph.cropchain.domain.repository.PinataRepository
import com.hexagraph.cropchain.domain.repository.RecentActivityRepository
import com.hexagraph.cropchain.workManager.WorkManagerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun provideRecentActivityDao(database: AppDatabase): RecentActivityDao {
        return database.recentActivityDao()
    }
    @Provides
    fun provideRecentActivityRepository(
        impl: RecentActivityRepositoryImpl
    ): RecentActivityRepository {
        return impl
    }

    @Provides
    @Singleton
    fun providePinataRepoImpl(pinataRepositoryImpl: PinataRepositoryImpl): PinataRepository {
        return pinataRepositoryImpl
    }


    @Provides
    fun provideMetaMaskRepoImpl(metaMaskRepositoryImpl: MetaMaskRepositoryImpl): MetaMaskRepository {
        return metaMaskRepositoryImpl
    }

    @Provides
    fun provideMetaMaskSDKRepoImpl(metaMaskSDKRepositoryImpl: MetaMaskSDKRepositoryImpl): MetaMaskSDKRepository {
        return metaMaskSDKRepositoryImpl
    }

    @Provides
    fun provideCropDao(database: AppDatabase): CropImageDao {
        return database.cropImageDao()
    }

    @Provides
    fun provideCropTableDao(database: AppDatabase): CropTableDao {
        return database.cropTableDao()
    }

    @Provides
    fun provideMetaMaskDao(database: AppDatabase): MetaMaskDao {
        return database.metaMaskDao()
    }

    @Provides
    @Singleton
    fun provideWorkManagerRepo(
        @ApplicationContext context: Context,
    ): WorkManagerRepository {
        return WorkManagerRepository(
            context
        )
    }

    @Provides
    @Singleton
    fun provideMetaMask(
        @ApplicationContext context: Context,
        appPreference: AppPreferences
    ): MetaMask {
        return MetaMask(context, appPreference)
    }

    @Provides
    fun provideCropTableRepository(cropTableDao: CropTableDao): CropTableRepository{
        return CropTableRepositoryImpl(cropTableDao)
    }
}