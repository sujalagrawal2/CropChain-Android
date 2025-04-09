package com.hexagraph.cropchain.util

import android.util.Log
import com.hexagraph.cropchain.RetrofitInstance
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

suspend fun uploadImageToPinata(file: File): Result<String> {
    val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
    val filePart = MultipartBody.Part.createFormData("file", file.name, requestBody)
    val apiSecret =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySW5mb3JtYXRpb24iOnsiaWQiOiI0YWE1MWYzOS03MmQ4LTQ5NTAtOGY2ZS1mMjcwNGY3YzA3YjAiLCJlbWFpbCI6InN1amFncmF3YWwxNzgzQGdtYWlsLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJwaW5fcG9saWN5Ijp7InJlZ2lvbnMiOlt7ImRlc2lyZWRSZXBsaWNhdGlvbkNvdW50IjoxLCJpZCI6IkZSQTEifSx7ImRlc2lyZWRSZXBsaWNhdGlvbkNvdW50IjoxLCJpZCI6Ik5ZQzEifV0sInZlcnNpb24iOjF9LCJtZmFfZW5hYmxlZCI6ZmFsc2UsInN0YXR1cyI6IkFDVElWRSJ9LCJhdXRoZW50aWNhdGlvblR5cGUiOiJzY29wZWRLZXkiLCJzY29wZWRLZXlLZXkiOiIzMzRmNzU3NWUxZmRkNjkxZGNlZSIsInNjb3BlZEtleVNlY3JldCI6IjdkNjI4NTU3NWJiMDhlMTEzYTgwOTFhZDkwNjBlNjA3NTE0ODA2YjY0YTI1YTFjYzYxMjVhZmU5YmQxNTI0ZjAiLCJleHAiOjE3Njk0MTAxMzZ9.1METZTl56XW3HGGs2QN243wa_k7UgmB_CXK8U-hP6no"
    return try {
        val response = RetrofitInstance.api.uploadImage(
            authorization = "Bearer $apiSecret",
            file = filePart
        )
        if (response.isSuccessful) {
            response.body()?.let { pinataResponse ->
                Log.d("PR", pinataResponse.IpfsHash)
                Result.success(pinataResponse.IpfsHash)
            } ?: Result.failure(Exception("Response body is null"))
        } else {
            val errorMessage = response.errorBody()?.string() ?: "Unknown error"
            Log.e("PinataUpload", "Failed: $errorMessage")
            Result.failure(Exception(errorMessage))
        }
    } catch (e: Exception) {
        Log.e("PinataUpload", "Error: $e")
        Result.failure(e)
    }

}