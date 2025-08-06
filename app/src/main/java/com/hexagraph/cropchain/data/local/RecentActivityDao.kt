package com.hexagraph.cropchain.data.local

import androidx.room.*
import com.hexagraph.cropchain.domain.model.RecentActivity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentActivityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivity(activity: RecentActivity): Long

    @Update
    suspend fun updateActivity(activity: RecentActivity)

    @Query("SELECT * FROM RecentActivity ORDER BY id DESC")
    fun getAllActivities(): Flow<List<RecentActivity>>
}