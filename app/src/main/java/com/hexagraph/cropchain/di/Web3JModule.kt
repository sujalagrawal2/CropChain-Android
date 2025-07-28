package com.hexagraph.cropchain.di

import android.content.Context
import com.hexagraph.cropchain.data.repository.Web3jRepositoryImpl
import com.hexagraph.cropchain.data.web3j.Web3j
import com.hexagraph.cropchain.domain.repository.Web3jRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Web3JModule {

    @Provides
    @Singleton
    fun provideWeb3J(@ApplicationContext context: Context): Web3j {
        return Web3j(context)
    }

    @Provides
    fun provideWeb3JRepository(web3jRepositoryImpl: Web3jRepositoryImpl): Web3jRepository {
        return web3jRepositoryImpl
    }

}