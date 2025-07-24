package com.hexagraph.cropchain.domain.repository

import com.hexagraph.cropchain.domain.model.MetaMaskAccounts
import kotlinx.coroutines.flow.Flow

interface MetaMaskRepository {

    fun getAllAccounts(): Flow<List<MetaMaskAccounts>>

    suspend fun insertAccount(metaMaskAccounts: MetaMaskAccounts)

    suspend fun deleteAllAccounts()

    suspend fun deleteAccount(metaMaskAccounts: MetaMaskAccounts)

    suspend fun updateAccount(metaMaskAccounts: MetaMaskAccounts)

}