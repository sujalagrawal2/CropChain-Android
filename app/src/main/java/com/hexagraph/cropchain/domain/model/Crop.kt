package com.hexagraph.cropchain.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "crop")
data class Crop(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var url: String? = null,
    val reviewed: Boolean = false,
    var uploadedToPinata: Boolean = false,
    var uploadedToBlockChain: Boolean = false,
    val date: String,
    val uid: String,
    val review: String? = null
)
