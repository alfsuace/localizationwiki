package com.alfsuace.localizationwiki.localization.data.remote

import com.alfsuace.localizationwiki.app.extensions.apiCall
import com.alfsuace.localizationwiki.localization.domain.WikiLocalization
import org.koin.core.annotation.Single

@Single
class WikiLocalizationApiRemoteSource(
    private val service: WikiApiService
) {

    suspend fun getNearbyWikiLocalizations(lat: Double, lon: Double): Result<List<WikiLocalization>> {
        return apiCall {
            service.getNearbyArticles(ggscoord = "$lat|$lon")
        }.mapCatching { response ->
            val pages = response.query?.pages ?: emptyMap()
            pages.values.map { page ->
                page.toModel()
            }
        }
    }

}
