package com.hexagraph.cropchain.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "crop")
data class Crop(
    @PrimaryKey(autoGenerate = true)
    val id: Long=0,
    val url: String,
    val status: String
)
