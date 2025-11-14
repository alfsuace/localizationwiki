package com.alfsuace.localizationwiki.localization.domain

interface LocationRepository {
    suspend fun getCurrentLocation(): Result<GeoCoordinates>
}