package com.alfsuace.localizationwiki.localization.data

import com.alfsuace.localizationwiki.localization.data.local.GeoCoordinatesLocalDataSource
import com.alfsuace.localizationwiki.localization.domain.GeoCoordinates
import com.alfsuace.localizationwiki.localization.domain.GeoCoordinatesRepository
import org.koin.core.annotation.Single

@Single
class GeoCoordinatesDataRepository(
    private val geoCoordinatesLocalDataSource: GeoCoordinatesLocalDataSource
) : GeoCoordinatesRepository {

    override suspend fun saveCoordinates(coordinates: GeoCoordinates) {
        geoCoordinatesLocalDataSource.saveCoordinates(coordinates)
    }

    override suspend fun getCoordinates(): Result<GeoCoordinates> {
        return geoCoordinatesLocalDataSource.getCoordinates()
    }

}