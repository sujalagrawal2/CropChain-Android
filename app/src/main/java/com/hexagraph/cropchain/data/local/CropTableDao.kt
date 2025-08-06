package com.hexagraph.cropchain.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.hexagraph.cropchain.domain.model.Crop
import kotlinx.coroutines.flow.Flow

@Dao
interface CropTableDao {

    @Insert
    suspend fun insertCrop(crop: Crop): Long

    @Query("SELECT * FROM crops WHERE id = :cropId")
    suspend fun getCropById(cropId: Long): Crop?

    @Query("SELECT * FROM crops ORDER BY createdDate DESC")
    fun getAllCrops(): Flow<List<Crop>>

    @Query("SELECT * FROM crops WHERE uploadedToBlockChain = 0")
    suspend fun getCropsNotUploadedToBlockchain(): List<Crop>

    @Query("SELECT * FROM crops WHERE uploadedToBlockChain = 1")
    suspend fun getCropsUploadedToBlockchain(): List<Crop>

    @Update
    suspend fun updateCrop(crop: Crop)

    @Query("UPDATE crops SET reviewed = :reviewed, review = :review WHERE id = :cropId")
    suspend fun updateReviewStatus(cropId: Long, reviewed: Boolean, review: String?)

    @Query("UPDATE crops SET title = :title, description = :description, latitude = :latitude, longitude = :longitude, address = :address WHERE id = :cropId")
    suspend fun updateCropDetails(cropId: Long, title: String, description: String, latitude: Double, longitude: Double, address: String)

    @Query("UPDATE crops SET uploadedToBlockChain = :uploaded, transactionHash = :txHash WHERE id = :cropId")
    suspend fun updateBlockchainStatus(cropId: Long, uploaded: Boolean, txHash: String?)

    @Query("DELETE FROM crops WHERE id = :cropId")
    suspend fun deleteCrop(cropId: Long)
}
