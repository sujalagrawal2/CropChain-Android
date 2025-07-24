package com.hexagraph.cropchain.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.hexagraph.cropchain.domain.model.MetaMaskAccounts
import kotlinx.coroutines.flow.Flow

@Dao
interface MetaMaskDao {
    @Query("SELECT * FROM MetaMaskAccounts")
    fun getAllAccounts(): Flow<List<MetaMaskAccounts>>

    @Insert
    suspend fun insertAccount(metaMaskAccounts: MetaMaskAccounts)

    @Query("DELETE FROM MetaMaskAccounts")
    suspend fun deleteAllAccounts()

    @Delete
    suspend fun deleteAccount(metaMaskAccounts: MetaMaskAccounts)

    @Update
    suspend fun updateAccount(metaMaskAccounts: MetaMaskAccounts)

}