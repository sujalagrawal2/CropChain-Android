package com.hexagraph.cropchain.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "crop")
data class Crop(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var url: String? = null,
    val reviewed: Boolean = false,
    var uploadedToPinata: Int = -1, // -1 not uploaded, 0 unsuccessful , 1 successful
    var uploadedToBlockChain: Boolean = false,
    val date: String,
    val uid: String,
    val review: String? = null,
    var transactionHash: String? = null,
    val verifiedTransactionHash: Int = -1,  // -1 not verified, 0 unsuccessful , 1 successful,
    var uploadProgress: Int = 0,
    val fileName: String = "Unknown Image"
)
