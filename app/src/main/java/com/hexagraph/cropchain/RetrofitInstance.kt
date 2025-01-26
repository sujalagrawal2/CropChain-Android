package com.hexagraph.cropchain

import com.hexagraph.cropchain.services.PinataService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://api.pinata.cloud/"

    val api: PinataService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PinataService::class.java)
    }
}