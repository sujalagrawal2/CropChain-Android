package com.hexagraph.cropchain.data.repository

import com.hexagraph.cropchain.data.local.RecentActivityDao
import com.hexagraph.cropchain.domain.model.RecentActivity
import com.hexagraph.cropchain.domain.repository.RecentActivityRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RecentActivityRepositoryImpl @Inject constructor(
    private val dao: RecentActivityDao
) : RecentActivityRepository {

    override suspend fun insertActivity(activity: RecentActivity): Long {
        return dao.insertActivity(activity)
    }

    override suspend fun updateActivity(activity: RecentActivity) {
        dao.updateActivity(activity)
    }

    override fun getAllActivities(): Flow<List<RecentActivity>> {
        return dao.getAllActivities()
    }
}