package com.alfsuace.localizationwiki.localization.domain

import org.koin.core.annotation.Single

@Single
class GetNearbyWikisUseCase(private val wikiLocalizationRepository: WikiLocalizationRepository) {

    suspend operator fun invoke(
        latitude: Double,
        longitude: Double
    ): Result<List<WikiLocalization>> {
        return wikiLocalizationRepository.getNearbyWikis(latitude, longitude)
    }

}