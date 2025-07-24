package com.hexagraph.cropchain.data.repository

import android.util.Log
import com.hexagraph.cropchain.data.remote.RetrofitInstance
import com.hexagraph.cropchain.data.remote.buildMultipartFile
import com.hexagraph.cropchain.domain.repository.PinataRepository
import jakarta.inject.Inject
import java.io.File

class PinataRepositoryImpl @Inject constructor() : PinataRepository {
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
        val apiSecret =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySW5mb3JtYXRpb24iOnsiaWQiOiI0YWE1MWYzOS03MmQ4LTQ5NTAtOGY2ZS1mMjcwNGY3YzA3YjAiLCJlbWFpbCI6InN1amFncmF3YWwxNzgzQGdtYWlsLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJwaW5fcG9saWN5Ijp7InJlZ2lvbnMiOlt7ImRlc2lyZWRSZXBsaWNhdGlvbkNvdW50IjoxLCJpZCI6IkZSQTEifSx7ImRlc2lyZWRSZXBsaWNhdGlvbkNvdW50IjoxLCJpZCI6Ik5ZQzEifV0sInZlcnNpb24iOjF9LCJtZmFfZW5hYmxlZCI6ZmFsc2UsInN0YXR1cyI6IkFDVElWRSJ9LCJhdXRoZW50aWNhdGlvblR5cGUiOiJzY29wZWRLZXkiLCJzY29wZWRLZXlLZXkiOiIzMzRmNzU3NWUxZmRkNjkxZGNlZSIsInNjb3BlZEtleVNlY3JldCI6IjdkNjI4NTU3NWJiMDhlMTEzYTgwOTFhZDkwNjBlNjA3NTE0ODA2YjY0YTI1YTFjYzYxMjVhZmU5YmQxNTI0ZjAiLCJleHAiOjE3Njk0MTAxMzZ9.1METZTl56XW3HGGs2QN243wa_k7UgmB_CXK8U-hP6no"

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

