package com.ucb.data

import com.ucb.domain.LocationCoordinates

interface ILocationDataSource {
    suspend fun getCurrentLocation(): LocationCoordinates

    fun isLocationEnabled(): Boolean
}