package com.hexagraph.cropchain.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(
    tableName = "cropImages",
    foreignKeys = [
        ForeignKey(
            entity = Crop::class,
            parentColumns = ["id"],
            childColumns = ["cropId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CropImages(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var url: String? = null,
    var uploadedToPinata: Int = -1, // -1 not uploaded, 0 unsuccessful , 1 successful
    val date: String,
    val uid: String,
    var uploadProgress: Int = 0,
    val fileName: String = "Unknown Image",
    val cropId: Long,
    val localPath: String = "" // To store local file path for upload
)
