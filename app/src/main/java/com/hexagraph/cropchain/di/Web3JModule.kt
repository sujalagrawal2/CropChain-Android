package com.hexagraph.cropchain.di

import com.hexagraph.cropchain.data.repository.Web3jRepositoryImpl
import com.hexagraph.cropchain.data.web3j.Web3j
import com.hexagraph.cropchain.domain.repository.Web3jRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Web3JModule {

    @Provides
    @Singleton
    fun provideWeb3J(): Web3j {
        return Web3j()
    }

    @Provides
    fun provideWeb3JRepository(web3jRepositoryImpl: Web3jRepositoryImpl): Web3jRepository {
       return web3jRepositoryImpl
    }

}