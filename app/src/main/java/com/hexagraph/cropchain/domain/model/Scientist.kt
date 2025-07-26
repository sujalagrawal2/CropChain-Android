package com.hexagraph.cropchain.domain.model

data class Scientist(
    val authPoint: Int = 0,
    val correctReportCount: Int = 0,
    val level: Int = 0,
    val scientistId: Int = 0,
    val reviewedImages: List<String> = emptyList(),
    val verifiedImages: List<String> = emptyList()
)
