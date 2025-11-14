package com.alfsuace.localizationwiki.localization.domain

import kotlinx.serialization.Serializable

data class WikiLocalization(
    val title: String,
    val imageUrl: String?,
    val latitude: Double,
    val longitude: Double,
    val url: String?
)

@Serializable
data class GeoCoordinates(
    val latitude: Double,
    val longitude: Double,
    val timeStamp: Long,
)