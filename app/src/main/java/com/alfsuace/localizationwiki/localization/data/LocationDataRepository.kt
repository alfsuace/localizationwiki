package com.alfsuace.localizationwiki.localization.data

import com.alfsuace.localizationwiki.localization.data.location.LocationClient
import com.alfsuace.localizationwiki.localization.domain.GeoCoordinates
import com.alfsuace.localizationwiki.localization.domain.LocationRepository
import org.koin.core.annotation.Single

@Single
class LocationDataRepository(private val locationClient: LocationClient) : LocationRepository {
    override suspend fun getCurrentLocation(): Result<GeoCoordinates> {
        return locationClient.getCurrentLocation()
    }

}