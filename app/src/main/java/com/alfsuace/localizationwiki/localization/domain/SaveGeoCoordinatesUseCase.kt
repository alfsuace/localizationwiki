package com.alfsuace.localizationwiki.localization.domain

import org.koin.core.annotation.Single

@Single
class SaveGeoCoordinatesUseCase(private val geoRepository: GeoCoordinatesRepository) {

    suspend operator fun invoke(geoCoordinates: GeoCoordinates) {
        geoRepository.saveCoordinates(geoCoordinates)
    }

}