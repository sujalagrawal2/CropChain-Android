package com.hexagraph.cropchain.data.repository

import com.hexagraph.cropchain.data.local.MetaMaskDao
import com.hexagraph.cropchain.domain.model.MetaMaskAccounts
import com.hexagraph.cropchain.domain.repository.MetaMaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MetaMaskRepositoryImpl @Inject constructor(private val metaMaskDao: MetaMaskDao) :
    MetaMaskRepository {
    override fun getAllAccounts(): Flow<List<MetaMaskAccounts>> {
        return metaMaskDao.getAllAccounts()
    }

    override suspend fun insertAccount(metaMaskAccounts: MetaMaskAccounts) {
        return metaMaskDao.insertAccount(metaMaskAccounts)
    }

    override suspend fun deleteAllAccounts() {
        return metaMaskDao.deleteAllAccounts()
    }

    override suspend fun deleteAccount(metaMaskAccounts: MetaMaskAccounts) {
        return metaMaskDao.deleteAccount(metaMaskAccounts)
    }

    override suspend fun updateAccount(metaMaskAccounts: MetaMaskAccounts) {
        return metaMaskDao.updateAccount(metaMaskAccounts)
    }
}