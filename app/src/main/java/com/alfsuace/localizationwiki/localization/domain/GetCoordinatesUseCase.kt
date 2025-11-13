package com.alfsuace.localizationwiki.localization.domain

import com.alfsuace.localizationwiki.app.domain.ErrorApp
import com.alfsuace.localizationwiki.app.domain.TIME_CACHE
import org.koin.core.annotation.Single

@Single
class GetCoordinatesUseCase(private val geoCoordinatesRepository: GeoCoordinatesRepository) {

    suspend operator fun invoke(): Result<GeoCoordinates> {
        val coords = geoCoordinatesRepository.getCoordinates().getOrNull()
        val now = System.currentTimeMillis()

        return if (coords == null) {
            Result.failure(ErrorApp.UnknownErrorApp)
        } else if (now - coords.timeStamp > TIME_CACHE) {
            Result.failure(ErrorApp.CacheExpiredErrorApp)
        } else {
            Result.success(coords)
        }
    }

}