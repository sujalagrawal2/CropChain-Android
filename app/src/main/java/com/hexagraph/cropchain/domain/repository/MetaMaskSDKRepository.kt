package com.hexagraph.cropchain.domain.repository

import android.location.Location
import com.hexagraph.cropchain.domain.model.LocationData
import io.metamask.androidsdk.Ethereum

interface MetaMaskSDKRepository {

    val walletAddress: String

    val ethereum: Ethereum

    val contractAddress: String

    fun connect(onError: (String) -> Unit, onSuccess: (List<String>) -> Unit)

    suspend fun uploadImage(crops: String = "url", title: String = "", description: String = "", locationData: LocationData = LocationData()): Result<String>

    suspend fun verifyImage(url: String, choice: Boolean): Result<String>

    suspend fun reviewImage(url: String, solution: String): Result<String>

    fun getAccountBalance(address: String, callback: (String?) -> Unit)
}