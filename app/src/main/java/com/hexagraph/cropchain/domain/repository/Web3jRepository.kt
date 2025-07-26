package com.hexagraph.cropchain.domain.repository

import com.hexagraph.cropchain.domain.model.Farmer
import com.hexagraph.cropchain.domain.model.ImageInfo
import com.hexagraph.cropchain.domain.model.Scientist

interface Web3jRepository {

    val walletAddress: String

    val contractAddress: String


    suspend fun getOpenImages(): Result<List<String>>

    suspend fun getCloseImages(): Result<List<String>>

    suspend fun getFarmer(address: String?): Result<Farmer>

    suspend fun getScientist(address: String?): Result<Scientist>

    suspend fun getImageInfo(url: String): Result<ImageInfo>

    suspend fun getVerifiers(url: String): Result<List<String>>

}