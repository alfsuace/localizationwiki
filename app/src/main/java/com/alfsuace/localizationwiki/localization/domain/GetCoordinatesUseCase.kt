package com.alfsuace.localizationwiki.localization.domain

import com.alfsuace.localizationwiki.app.domain.ErrorApp
import com.alfsuace.localizationwiki.app.domain.TIME_CACHE
import org.koin.core.annotation.Single

@Single
class GetCoordinatesUseCase(private val geoCoordinatesRepository: GeoCoordinatesRepository) {

    suspend operator fun invoke(): Result<GeoCoordinates> {
        val repoResult = geoCoordinatesRepository.getCoordinates()

        repoResult.exceptionOrNull()?.let { error ->
            return Result.failure(error)
        }

        val coords = repoResult.getOrNull()!!
        val now = System.currentTimeMillis()

        return if (now - coords.timeStamp > TIME_CACHE) {
            Result.failure(ErrorApp.CacheExpiredErrorApp)
        } else {
            Result.success(coords)
        }
    }

}