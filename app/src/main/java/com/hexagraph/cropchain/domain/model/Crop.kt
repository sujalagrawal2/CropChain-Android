package com.hexagraph.cropchain.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "crops")
data class Crop(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String = "",
    val description: String = "",
    val uploadedToBlockChain: Boolean = false,
    val reviewed: Boolean = false,
    val review: String? = null,
    val transactionHash: String? = null,
    val verifiedTransactionHash: Int = -1,  // -1 not verified, 0 unsuccessful , 1 successful
    val createdDate: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val address: String = ""
)
