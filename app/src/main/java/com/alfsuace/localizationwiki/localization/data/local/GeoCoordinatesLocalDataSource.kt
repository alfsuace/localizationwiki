package com.alfsuace.localizationwiki.localization.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.alfsuace.localizationwiki.app.domain.ErrorApp
import com.alfsuace.localizationwiki.localization.domain.GeoCoordinates
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Single

@Single
class GeoCoordinatesLocalDataSource(
    private val dataStore: DataStore<Preferences>,
    private val json: Json
) {

    private companion object {
        val GEO_COORDINATES_KEY = stringPreferencesKey("geo_coordinates")
    }

    suspend fun getCoordinates(): Result<GeoCoordinates> {
        return try {
            val geoJson = dataStore.data
                .map { preferences -> preferences[GEO_COORDINATES_KEY] }
                .first()
            if (geoJson != null) {
                val coordinates = json.decodeFromString<GeoCoordinates>(geoJson)
                Result.success(coordinates)
            } else {
                Result.failure(ErrorApp.DataErrorApp())
            }
        } catch (e: Exception) {
            Result.failure(ErrorApp.UnknownErrorApp())
        }
    }

    suspend fun saveCoordinates(geoCoordinates: GeoCoordinates) {
        clearCoordinates()
        dataStore.edit { preferences ->
            preferences[GEO_COORDINATES_KEY] = json.encodeToString(geoCoordinates)
        }
    }

    suspend fun clearCoordinates() {
        dataStore.edit { it.remove(GEO_COORDINATES_KEY) }
    }
}
