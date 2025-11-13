package com.alfsuace.localizationwiki.localization.domain

interface GeoCoordinatesRepository {

    suspend fun saveCoordinates(coordinates: GeoCoordinates)
    suspend fun getCoordinates(): Result<GeoCoordinates>

}