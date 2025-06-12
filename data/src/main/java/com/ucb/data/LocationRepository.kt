package com.ucb.data

import com.ucb.domain.LocationCoordinates

class LocationRepository(
    private val locationDataSource: ILocationDataSource
) {
    fun isLocationEnabled(): Boolean = locationDataSource.isLocationEnabled()

    suspend fun getCurrentLocation(): LocationCoordinates {
        val location = locationDataSource.getCurrentLocation()
        return LocationCoordinates(
            latitude = location.latitude,
            longitude = location.longitude
        )
    }
}