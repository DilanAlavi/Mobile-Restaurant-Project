package com.ucb.domain

import kotlinx.serialization.Serializable

@Serializable
data class LocationCoordinates(
    val latitude: Double,
    val longitude: Double
)