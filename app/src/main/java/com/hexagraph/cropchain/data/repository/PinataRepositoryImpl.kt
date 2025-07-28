package com.hexagraph.cropchain.data.repository

import android.content.Context
import android.util.Log
import com.hexagraph.cropchain.R
import com.hexagraph.cropchain.data.remote.RetrofitInstance
import com.hexagraph.cropchain.data.remote.buildMultipartFile
import com.hexagraph.cropchain.domain.repository.PinataRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import java.io.File

class PinataRepositoryImpl @Inject constructor(@ApplicationContext private val context: Context) : PinataRepository {
    override suspend fun uploadImageToPinata(
        file: File,
        onProgress: (Int) -> Unit
    ): Result<String> {
        if (!file.exists() || !file.isFile) {
            val errorMessage = "File does not exist or is not a file: ${file.path}"
            Log.e("PinataUpload", errorMessage)
            return Result.failure(Exception(errorMessage))
        }
        val filePart = buildMultipartFile(file, onProgress)
        val apiSecret = context.getString(R.string.PINATA_SECRET_API_KEY)

        return try {
            Log.d("PinataUpload", "Attempting to upload file: ${file.name}")

            val response = RetrofitInstance.api.uploadImage(
                authorization = "Bearer $apiSecret",
                file = filePart
            )
            if (response.isSuccessful) {
                response.body()?.let { pinataResponse ->
                    Log.d(
                        "PinataUpload",
                        "Upload successful. IPFS Hash: ${pinataResponse.IpfsHash}"
                    )

                    Result.success(pinataResponse.IpfsHash)
                } ?: Result.failure(Exception("Response body is null"))
            } else {
                val errorBodyString = response.errorBody()?.string()
                val errorMessage = if (errorBodyString.isNullOrEmpty()) {
                    "Unknown error during upload. HTTP Status: ${response.code()}"
                } else {
                    "Upload failed. HTTP Status: ${response.code()}, Error: $errorBodyString"
                }
                Log.e("PinataUpload", errorMessage)
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            val exceptionMessage = e.message ?: "Exception occurred with no message"
            Log.e(
                "PinataUpload",
                "Error during Pinata upload: ${e::class.java.simpleName}: $exceptionMessage",
                e
            )
            Result.failure(e)
        }
    }
}

