package com.alfsuace.localizationwiki.localization.di

import com.alfsuace.localizationwiki.localization.data.remote.WikiApiService
import com.alfsuace.localizationwiki.localization.data.remote.WikiLocalizationApiRemoteSource
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import retrofit2.Retrofit

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

}