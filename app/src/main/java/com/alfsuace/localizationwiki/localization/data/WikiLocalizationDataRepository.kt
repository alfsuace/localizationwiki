package com.alfsuace.localizationwiki.localization.data

import com.alfsuace.localizationwiki.localization.data.remote.WikiLocalizationApiRemoteSource
import com.alfsuace.localizationwiki.localization.domain.WikiLocalization
import com.alfsuace.localizationwiki.localization.domain.WikiLocalizationRepository
import org.koin.core.annotation.Single

@Single
class WikiLocalizationDataRepository(
    private val wikiLocalizationApiRemoteSource: WikiLocalizationApiRemoteSource
) : WikiLocalizationRepository{

    override suspend fun getNearbyWikis(lat: Double, lon: Double): Result<List<WikiLocalization>> {
        return wikiLocalizationApiRemoteSource.getNearbyWikiLocalizations(lat, lon)
    }

}