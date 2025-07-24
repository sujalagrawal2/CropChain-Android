package com.hexagraph.cropchain.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MetaMaskAccounts(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val account: String = "",
    val isConnected: Boolean = false
)
