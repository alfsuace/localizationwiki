package com.alfsuace.localizationwiki.localization.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alfsuace.localizationwiki.app.domain.ErrorApp
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


    //Dale una vuelta a que coja las coordenadas del uistate
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

    fun getCoordinatesWithLocation() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch(Dispatchers.IO) {
            val result = getLocationUseCase.invoke()
            result.fold(
                onSuccess = { newGeoCoordinates ->
                    _uiState.update { uiState ->
                        uiState.copy(isLoading = false, coords = newGeoCoordinates)
                    }
                    saveGeoCoordinates(newGeoCoordinates)
                },
                onFailure = { newError ->
                    _uiState.update { uiState ->
                        uiState.copy(isLoading = false, error = newError as ErrorApp?)
                    }
                }
            )
        }
    }

    fun getCoordinates() {
        _uiState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch(Dispatchers.IO) {
            val result = getCoordinatesUseCase.invoke()
            result.fold(
                onSuccess = { newGeoCoordinates ->
                    _uiState.update { uiState ->
                        uiState.copy(isLoading = false, coords = newGeoCoordinates)
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

    fun saveGeoCoordinates(geoCoordinates: GeoCoordinates) {
        viewModelScope.launch(Dispatchers.IO) {
            saveGeoCoordinatesUseCase.invoke(geoCoordinates)
        }
    }


    data class WikiLocalizationUiState(
        val wikis: List<WikiLocalization> = emptyList(),
        val isLoading: Boolean = false,
        val coords: GeoCoordinates? = null,
        val error: ErrorApp? = null,
    )
}