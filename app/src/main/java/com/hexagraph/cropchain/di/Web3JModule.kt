package com.hexagraph.cropchain.di

import com.hexagraph.cropchain.Web3J
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
    fun provideWeb3J(): Web3J {
        return Web3J()
    }
}