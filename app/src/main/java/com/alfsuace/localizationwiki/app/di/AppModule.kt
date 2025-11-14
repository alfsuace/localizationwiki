package com.alfsuace.localizationwiki.app.di

import com.google.gson.Gson
import kotlinx.serialization.json.Json
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@ComponentScan("com.alfsuace.localizationwiki")
class AppModule {

    @Single
    fun providerGson() = Gson()

    @Single
    fun provideJson(): Json {
        return Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        }
    }

}