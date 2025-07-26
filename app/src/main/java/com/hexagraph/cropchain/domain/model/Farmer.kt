package com.hexagraph.cropchain.domain.model

import java.math.BigInteger

data class Farmer(
    val correctReportCount: Int = 0,
    val authPoint: Int = 0,
    val level: Int = 0,
    val totalImages: List<String> = emptyList(),
    val verifiedImages: List<String> = emptyList(),
    val unVerifiedImages: List<String> = emptyList()
)
