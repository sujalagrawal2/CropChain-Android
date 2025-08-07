package com.hexagraph.cropchain.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RecentActivity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val type: Int,  // 0-> farmer upload , 1-> review, 2-> verify
    val idOfRecentActivity: Int,
    val isSeen: Boolean = false,
    val transactionHash: String?,
    val status: Int ,// 0-> not Started, 1-> In Progress , 2-> Completed,
    val reason : String? = null,
    val title : String = ""
)