package com.alfsuace.localizationwiki.localization.domain

interface WikiLocalizationRepository {

    suspend fun getNearbyWikis(lat: Double, lon: Double): Result<List<WikiLocalization>>

}