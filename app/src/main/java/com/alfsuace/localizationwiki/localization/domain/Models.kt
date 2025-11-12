package com.alfsuace.localizationwiki.localization.domain

data class WikiLocalization(
    val title: String,
    val imageUrl: String?,
    val latitude: Double,
    val longitude: Double,
    val url: String?
)

data class GeoCoordinates(
    val latitude: Double,
    val longitude: Double
)