package com.hexagraph.cropchain.data.remote

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface PinataService {
    @Multipart
    @POST("pinning/pinFileToIPFS")
    suspend fun uploadImage(
        @Header("Authorization") authorization: String,
        @Part file: MultipartBody.Part
    ): Response<PinataResponse>
}

data class PinataResponse(
    val IpfsHash: String,
    val PinSize: Int,
    val Pimestamp: String
)