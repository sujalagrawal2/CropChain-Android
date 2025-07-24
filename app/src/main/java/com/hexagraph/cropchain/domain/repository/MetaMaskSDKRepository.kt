package com.hexagraph.cropchain.domain.repository

import io.metamask.androidsdk.Ethereum

interface MetaMaskSDKRepository {

    val walletAddress: String

    val ethereum: Ethereum

    val contractAddress : String

    fun connect(onError: (String) -> Unit, onSuccess: (List<String>) -> Unit)

    suspend fun send(crops: String = "url"): Result<String>

    fun getAccountBalance(address: String, callback: (String?) -> Unit)
}