package com.hexagraph.cropchain.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class LocationData(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val address: String = ""
)
