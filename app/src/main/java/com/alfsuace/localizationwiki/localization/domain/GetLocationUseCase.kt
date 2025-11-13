package com.alfsuace.localizationwiki.localization.domain

import org.koin.core.annotation.Single

@Single
class GetLocationUseCase(private val locationRepository: LocationRepository) {

    suspend operator fun invoke(): Result<GeoCoordinates> {
        return locationRepository.getCurrentLocation()
    }

}