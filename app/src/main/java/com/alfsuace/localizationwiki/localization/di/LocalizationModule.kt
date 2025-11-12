package com.alfsuace.localizationwiki.localization.di

import com.alfsuace.localizationwiki.app.db.LocalizationDataBase
import com.alfsuace.localizationwiki.localization.data.local.db.WikiLocalizationDao
import com.alfsuace.localizationwiki.localization.data.remote.WikiApiService
import com.alfsuace.localizationwiki.localization.data.remote.WikiLocalizationApiRemoteSource
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import retrofit2.Retrofit

const val TIME_CACHE = 5 * 1000 * 60

@Module
class LocalizationModule {

    @Single
    fun provideWikiApiService(retrofit: Retrofit): WikiApiService {
        return retrofit.create(WikiApiService::class.java)
    }

    @Single
    fun provideWikiLocalizationApiRemoteSource(
        service: WikiApiService
    ): WikiLocalizationApiRemoteSource {
        return WikiLocalizationApiRemoteSource(service)
    }

    @Single
    fun provideWikiLocalizationDao(database: LocalizationDataBase): WikiLocalizationDao =
        database.wikiLocalizationDao()
}