package com.alfsuace.localizationwiki.localization.data.remote

import com.google.gson.annotations.SerializedName

data class WikiResponse(
    @SerializedName("query") val query: QueryResponse?
)

data class QueryResponse(
    @SerializedName("pages") val pages: Map<String, WikiLocalizationApiModel>?
)

data class WikiLocalizationApiModel(
    @SerializedName("pageid") val pageId: Long,
    @SerializedName("title") val title: String,
    @SerializedName("coordinates") val coordinates: List<Coordinate>?,
    @SerializedName("thumbnail") val thumbnail: Thumbnail?,
    @SerializedName("fullurl") val fullUrl: String?
)

data class Coordinate(
    @SerializedName("lat") val lat: Double,
    @SerializedName("lon") val lon: Double
)

data class Thumbnail(
    @SerializedName("source") val source: String
)
