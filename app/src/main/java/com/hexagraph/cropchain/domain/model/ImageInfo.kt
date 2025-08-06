package com.hexagraph.cropchain.domain.model

data class ImageInfo(
    val id: Int = 0,
    val title : String = "",
    val description : String = "",
    val location : String = "",
    val ownerAddress: String="",
    val imageUrl: String="",
    val aiSol: String="",
    val reviewerAddress: String="",
    val reviewerSol: String="",
    val gotAI: Boolean=false,
    val reviewed: Boolean=false,
    val verified: Boolean=false,
    val verificationCount: Int=0,
    val trueCount: Int=0,
    val falseCount: Int=0
)
