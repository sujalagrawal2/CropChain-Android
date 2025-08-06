package com.hexagraph.cropchain.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.hexagraph.cropchain.domain.model.CropImages
import kotlinx.coroutines.flow.Flow

@Dao
interface CropImageDao {

    @Insert
    suspend fun insertCropImage(cropImage: CropImages): Long

    @Query("SELECT * FROM cropImages WHERE cropId = :cropId ORDER BY date DESC")
    fun getCropImagesByCropId(cropId: Long): Flow<List<CropImages>>

    @Query("SELECT * FROM cropImages WHERE cropId = :cropId")
    suspend fun getCropImagesByCropIdSync(cropId: Long): List<CropImages>

    @Query("SELECT * FROM cropImages ORDER BY date DESC")
    fun getAllCropImages(): Flow<List<CropImages>>

    @Query("SELECT * FROM cropImages WHERE uploadedToPinata = -1 OR uploadedToPinata = 0")
    suspend fun getPendingPinataUploads(): List<CropImages>

    @Query("SELECT * FROM cropImages WHERE uploadedToPinata = 1")
    suspend fun getSuccessfulPinataUploads(): List<CropImages>

    @Query("SELECT * FROM cropImages WHERE id = :imageId")
    suspend fun getCropImageById(imageId: Long): CropImages?

    @Update
    suspend fun updateCropImage(cropImage: CropImages)

    @Query("UPDATE cropImages SET uploadProgress = :progress WHERE id = :imageId")
    suspend fun updateUploadProgress(imageId: Long, progress: Int)

    @Query("UPDATE cropImages SET uploadedToPinata = :status, url = :url WHERE id = :imageId")
    suspend fun updatePinataUploadStatus(imageId: Long, status: Int, url: String?)

    @Delete
    suspend fun deleteCropImage(cropImage: CropImages)

    @Query("DELETE FROM cropImages WHERE id = :imageId")
    suspend fun deleteCropImageById(imageId: Long)

    @Query("DELETE FROM cropImages WHERE cropId = :cropId")
    suspend fun deleteAllCropImagesByCropId(cropId: Long)

    // Count functions for validation
    @Query("SELECT COUNT(*) FROM cropImages WHERE cropId = :cropId")
    suspend fun getCropImagesCount(cropId: Long): Int

    @Query("SELECT COUNT(*) FROM cropImages WHERE cropId = :cropId AND uploadedToPinata = 1")
    suspend fun getUploadedCropImagesCount(cropId: Long): Int
}