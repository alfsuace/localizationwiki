package com.alfsuace.localizationwiki.localization.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alfsuace.localizationwiki.app.domain.ErrorApp
import com.alfsuace.localizationwiki.app.domain.TIME_CACHE
import com.alfsuace.localizationwiki.localization.domain.GeoCoordinates
import com.alfsuace.localizationwiki.localization.domain.GetCoordinatesUseCase
import com.alfsuace.localizationwiki.localization.domain.GetLocationUseCase
import com.alfsuace.localizationwiki.localization.domain.GetNearbyWikisUseCase
import com.alfsuace.localizationwiki.localization.domain.SaveGeoCoordinatesUseCase
import com.alfsuace.localizationwiki.localization.domain.WikiLocalization
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class WikiLocalizationViewModel(
    private val getCoordinatesUseCase: GetCoordinatesUseCase,
    private val saveGeoCoordinatesUseCase: SaveGeoCoordinatesUseCase,
    private val getNearbyWikisUseCase: GetNearbyWikisUseCase,
    private val getLocationUseCase: GetLocationUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(WikiLocalizationUiState())
    val uiState: StateFlow<WikiLocalizationUiState> = _uiState


    fun getNearbyWikis(geoCoordinates: GeoCoordinates) {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch(Dispatchers.IO) {
            val result =
                getNearbyWikisUseCase.invoke(geoCoordinates.latitude, geoCoordinates.longitude)
            result.fold(
                onSuccess = { newWikis ->
                    _uiState.update { uiState ->
                        uiState.copy(isLoading = false, wikis = newWikis, coords = geoCoordinates)
                    }
                },
                onFailure = { newError ->
                    _uiState.update { uiState ->
                        uiState.copy(isLoading = false, error = newError as ErrorApp?)
                    }
                }
            )
        }
    }

    fun updatePermissionStatus(granted: Boolean, showSettingsOnDenial: Boolean) {
        _uiState.update { it.copy(hasLocationPermission = granted) }
        if (granted) {
            fetchCoordinatesAndWikis()
        } else if (showSettingsOnDenial) {
            _uiState.update { it.copy(showGoToSettings = true) }
        }
    }

    fun fetchCoordinatesAndWikis() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch(Dispatchers.IO) {
            val cachedResult = getCoordinatesUseCase.invoke()
            cachedResult.fold(
                onSuccess = { cachedCoords ->
                    val isCacheStale =
                        System.currentTimeMillis() - cachedCoords.timeStamp > TIME_CACHE

                    if (isCacheStale) {
                        getLocationFromGps()
                    } else {
                        useCachedCoordsAndLoadWikis(cachedCoords)
                    }
                },
                onFailure = {
                    getLocationFromGps()
                }
            )
        }
    }

    private fun getLocationFromGps() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = getLocationUseCase.invoke()
            result.fold(
                onSuccess = { newGeoCoordinates ->
                    saveGeoCoordinates(newGeoCoordinates)
                    getNearbyWikis(newGeoCoordinates)
                },
                onFailure = { newError ->
                    _uiState.update { uiState ->
                        uiState.copy(isLoading = false, error = newError as ErrorApp?)
                    }
                }
            )
        }
    }

    private fun useCachedCoordsAndLoadWikis(cachedCoords: GeoCoordinates) {
        getNearbyWikis(cachedCoords)
    }


    fun saveGeoCoordinates(geoCoordinates: GeoCoordinates) {
        viewModelScope.launch(Dispatchers.IO) {
            saveGeoCoordinatesUseCase.invoke(geoCoordinates)
        }
    }

    data class WikiLocalizationUiState(
        val hasLocationPermission: Boolean = false,
        val showGoToSettings: Boolean = false,
        val wikis: List<WikiLocalization> = emptyList(),
        val isLoading: Boolean = false,
        val coords: GeoCoordinates? = null,
        val error: ErrorApp? = null,
    )

}