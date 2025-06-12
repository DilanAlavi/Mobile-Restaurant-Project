package com.ucb.usecases

import com.ucb.data.LocationRepository
import com.ucb.domain.LocationCoordinates

class GetCurrentLocation (
    private val locationRepository: LocationRepository
) {
    suspend fun execute(): LocationCoordinates {
        return locationRepository.getCurrentLocation()
    }

    fun isLocationEnabled(): Boolean {
        return locationRepository.isLocationEnabled()
    }
}