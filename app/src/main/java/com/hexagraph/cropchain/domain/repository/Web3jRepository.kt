package com.hexagraph.cropchain.domain.repository

interface Web3jRepository {

    val walletAddress: String

    val contractAddress : String

    suspend fun getVerifiedImage(): List<String>

}