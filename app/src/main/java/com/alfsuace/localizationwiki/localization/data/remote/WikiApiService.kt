package com.alfsuace.localizationwiki.localization.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WikiApiService {

    @GET("api.php")
    suspend fun getNearbyArticles(
        @Query("action") action: String = "query",
        @Query("generator") generator: String = "geosearch",
        @Query("prop") prop: String = "coordinates|pageimages|info",
        @Query("inprop") inprop: String = "url",
        @Query("ggscoord") ggscoord: String, // latitude,longitude
        @Query("ggsradius") radius: Int = 10000,
        @Query("ggslimit") limit: Int = 10,
        @Query("format") format: String = "json"
    ): Response<WikiResponse>

}