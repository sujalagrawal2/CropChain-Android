package com.hexagraph.cropchain.domain.repository

import com.hexagraph.cropchain.domain.model.RecentActivity
import kotlinx.coroutines.flow.Flow

interface RecentActivityRepository {
    suspend fun insertActivity(activity: RecentActivity): Long
    suspend fun updateActivity(activity: RecentActivity)
    fun getAllActivities(): Flow<List<RecentActivity>>
}