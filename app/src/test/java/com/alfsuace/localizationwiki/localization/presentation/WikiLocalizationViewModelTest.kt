package com.alfsuace.localizationwiki.localization.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.alfsuace.localizationwiki.app.domain.ErrorApp
import com.alfsuace.localizationwiki.app.domain.TIME_CACHE
import com.alfsuace.localizationwiki.localization.domain.GeoCoordinates
import com.alfsuace.localizationwiki.localization.domain.GetCoordinatesUseCase
import com.alfsuace.localizationwiki.localization.domain.GetLocationUseCase
import com.alfsuace.localizationwiki.localization.domain.GetNearbyWikisUseCase
import com.alfsuace.localizationwiki.localization.domain.SaveGeoCoordinatesUseCase
import com.alfsuace.localizationwiki.localization.domain.WikiLocalization
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.slot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class WikiLocalizationViewModelTest {

    @RelaxedMockK
    private lateinit var getCoordinatesUseCase: GetCoordinatesUseCase

    @RelaxedMockK
    private lateinit var saveGeoCoordinatesUseCase: SaveGeoCoordinatesUseCase

    @RelaxedMockK
    private lateinit var getNearbyWikisUseCase: GetNearbyWikisUseCase

    @RelaxedMockK
    private lateinit var getLocationUseCase: GetLocationUseCase

    private lateinit var wikiLocalizationViewModel: WikiLocalizationViewModel

    @get:Rule
    var rule: InstantTaskExecutorRule = InstantTaskExecutorRule()


    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(Dispatchers.Unconfined)
        wikiLocalizationViewModel = WikiLocalizationViewModel(
            getCoordinatesUseCase,
            saveGeoCoordinatesUseCase,
            getNearbyWikisUseCase,
            getLocationUseCase
        )
    }

    @After
    fun onAfter() {
        Dispatchers.resetMain()
    }

    // --- Tests de getNearbyWikis ---

    @Test
    fun `when getNearbyWikis is called, should load wikis and update uiState on success`() =
        runTest {
            // Given
            val testCoords = GeoCoordinates(10.0, 20.0, System.currentTimeMillis())
            val mockWikis = listOf(
                WikiLocalization("Wiki A", null, 10.0, 20.0, null)
            )
            coEvery { getNearbyWikisUseCase.invoke(any(), any()) } returns Result.success(mockWikis)

            // When
            wikiLocalizationViewModel.getNearbyWikis(testCoords)
            advanceUntilIdle()

            // Then
            assertEquals(false, wikiLocalizationViewModel.uiState.value.isLoading)
            assertEquals(mockWikis, wikiLocalizationViewModel.uiState.value.wikis)
            assertEquals(testCoords, wikiLocalizationViewModel.uiState.value.coords)
            assertEquals(null, wikiLocalizationViewModel.uiState.value.error)
        }

    @Test
    fun `when getNearbyWikis is called, should set error and update uiState on failure`() =
        runTest {
            // Given
            val testCoords = GeoCoordinates(10.0, 20.0, System.currentTimeMillis())
            val mockError = ErrorApp.UnknownErrorApp
            coEvery { getNearbyWikisUseCase.invoke(any(), any()) } returns Result.failure(mockError)

            // When
            wikiLocalizationViewModel.getNearbyWikis(testCoords)
            advanceUntilIdle()

            // Then
            assertEquals(false, wikiLocalizationViewModel.uiState.value.isLoading)
            assertEquals(mockError, wikiLocalizationViewModel.uiState.value.error)
            assertEquals(
                emptyList<WikiLocalization>(),
                wikiLocalizationViewModel.uiState.value.wikis
            )
            assertEquals(testCoords, wikiLocalizationViewModel.uiState.value.coords)
        }

    // --- Tests de updatePermissionStatus ---

    @Test
    fun `when updatePermissionStatus is called with granted true, should update state and fetch coordinates`() =
        runTest {
            // Given
            val testCoords = GeoCoordinates(10.0, 20.0, System.currentTimeMillis())
            coEvery { getCoordinatesUseCase.invoke() } returns Result.failure(ErrorApp.UnknownErrorApp)
            coEvery { getLocationUseCase.invoke() } returns Result.success(testCoords)
            coEvery {
                getNearbyWikisUseCase.invoke(
                    any(),
                    any()
                )
            } returns Result.success(emptyList())

            // When
            wikiLocalizationViewModel.updatePermissionStatus(
                granted = true,
                showSettingsOnDenial = false
            )

            // Then
            assertEquals(true, wikiLocalizationViewModel.uiState.value.hasLocationPermission)
            assertEquals(false, wikiLocalizationViewModel.uiState.value.showGoToSettings)
            coVerify(exactly = 1) { getCoordinatesUseCase.invoke() }
        }

    @Test
    fun `when updatePermissionStatus is called with granted false and showSettings true, should update state and showGoToSettings`() =
        runTest {
            // When
            wikiLocalizationViewModel.updatePermissionStatus(
                granted = false,
                showSettingsOnDenial = true
            )

            // Then
            assertEquals(false, wikiLocalizationViewModel.uiState.value.hasLocationPermission)
            assertEquals(true, wikiLocalizationViewModel.uiState.value.showGoToSettings)
            coVerify(exactly = 0) { getCoordinatesUseCase.invoke() }
        }

    @Test
    fun `when updatePermissionStatus is called with granted false and showSettings false, should update state but not showGoToSettings`() =
        runTest {
            // When
            wikiLocalizationViewModel.updatePermissionStatus(
                granted = false,
                showSettingsOnDenial = false
            )

            // Then
            assertEquals(false, wikiLocalizationViewModel.uiState.value.hasLocationPermission)
            assertEquals(false, wikiLocalizationViewModel.uiState.value.showGoToSettings)
            coVerify(exactly = 0) { getCoordinatesUseCase.invoke() }
        }

    // --- Tests de fetchCoordinatesAndWikis (Lógica de caché) ---

    @Test
    fun `when fetchCoordinatesAndWikis is called and cache is valid, should use cached coords and load wikis`() =
        runTest {
            // Given
            val validTimeStamp = System.currentTimeMillis() - TIME_CACHE + 1000
            val cachedCoords = GeoCoordinates(1.0, 2.0, validTimeStamp)
            val mockWikis = listOf(WikiLocalization("a", "imgUrl", 2.0, 3.0, "url"))
            coEvery { getCoordinatesUseCase.invoke() } returns Result.success(cachedCoords)
            coEvery { getNearbyWikisUseCase.invoke(any(), any()) } returns Result.success(mockWikis)

            // When
            wikiLocalizationViewModel.fetchCoordinatesAndWikis()

            // Then
            coVerify(exactly = 1) { getCoordinatesUseCase.invoke() }
            coVerify(exactly = 0) { getLocationUseCase.invoke() }
            coVerify(exactly = 1) {
                getNearbyWikisUseCase.invoke(
                    cachedCoords.latitude,
                    cachedCoords.longitude
                )
            }
            assertEquals(cachedCoords, wikiLocalizationViewModel.uiState.value.coords)
            assertEquals(mockWikis, wikiLocalizationViewModel.uiState.value.wikis)
            assertEquals(null, wikiLocalizationViewModel.uiState.value.error)
            assertEquals(false, wikiLocalizationViewModel.uiState.value.isLoading)
        }

    @Test
    fun `when fetchCoordinatesAndWikis is called and cache is stale, should get location from GPS`() =
        runTest {
            // Given
            val staleTimeStamp = System.currentTimeMillis() - TIME_CACHE - 1000
            val staleCoords = GeoCoordinates(1.0, 2.0, staleTimeStamp)
            val newCoords = GeoCoordinates(3.0, 4.0, System.currentTimeMillis())
            val mockWikis = listOf(WikiLocalization("a", "imgUrl", 2.0, 3.0, "url"))
            coEvery { getCoordinatesUseCase.invoke() } returns Result.success(staleCoords)
            coEvery { getLocationUseCase.invoke() } returns Result.success(newCoords)
            coEvery { getNearbyWikisUseCase.invoke(any(), any()) } returns Result.success(mockWikis)

            // When
            wikiLocalizationViewModel.fetchCoordinatesAndWikis()

            // Then
            coVerify(exactly = 1) { getCoordinatesUseCase.invoke() }
            coVerify(exactly = 1) { getLocationUseCase.invoke() }
            coVerify(exactly = 1) { saveGeoCoordinatesUseCase.invoke(newCoords) }
            coVerify(exactly = 1) {
                getNearbyWikisUseCase.invoke(
                    newCoords.latitude,
                    newCoords.longitude
                )
            }
            assertEquals(newCoords, wikiLocalizationViewModel.uiState.value.coords)
            assertEquals(mockWikis, wikiLocalizationViewModel.uiState.value.wikis)
            assertEquals(null, wikiLocalizationViewModel.uiState.value.error)
            assertEquals(false, wikiLocalizationViewModel.uiState.value.isLoading)
        }

    @Test
    fun `when fetchCoordinatesAndWikis is called and no cache is found, should get location from GPS`() =
        runTest {
            // Given
            val newCoords = GeoCoordinates(3.0, 4.0, System.currentTimeMillis())
            val mockWikis = listOf(WikiLocalization("a", "imgUrl", 2.0, 3.0, "url"))
            coEvery { getCoordinatesUseCase.invoke() } returns Result.failure(ErrorApp.UnknownErrorApp)
            coEvery { getLocationUseCase.invoke() } returns Result.success(newCoords)
            coEvery { getNearbyWikisUseCase.invoke(any(), any()) } returns Result.success(mockWikis)

            // When
            wikiLocalizationViewModel.fetchCoordinatesAndWikis()

            // Then
            coVerify(exactly = 1) { getCoordinatesUseCase.invoke() }
            coVerify(exactly = 1) { getLocationUseCase.invoke() }
            coVerify(exactly = 1) { saveGeoCoordinatesUseCase.invoke(newCoords) }
            coVerify(exactly = 1) {
                getNearbyWikisUseCase.invoke(
                    newCoords.latitude,
                    newCoords.longitude
                )
            }
            assertEquals(newCoords, wikiLocalizationViewModel.uiState.value.coords)
            assertEquals(mockWikis, wikiLocalizationViewModel.uiState.value.wikis)
            assertEquals(null, wikiLocalizationViewModel.uiState.value.error)
            assertEquals(false, wikiLocalizationViewModel.uiState.value.isLoading)
        }

    // --- Tests de getLocationFromGps (Manejo de errores de GPS) ---

    @Test
    fun `when getLocationFromGps is success, should update uiState with coords and wikis`() =
        runTest {
            // Given
            val timeStamp = System.currentTimeMillis()
            val mockCoords = GeoCoordinates(10.0, 20.0, timeStamp)
            val mockWikis = listOf(WikiLocalization("a", "imgUrl", 2.0, 3.0, "url"))
            val mockError = ErrorApp.CacheExpiredErrorApp

            coEvery { getCoordinatesUseCase.invoke() } returns Result.failure(mockError)
            coEvery { getLocationUseCase.invoke() } returns Result.success(mockCoords)
            coEvery { saveGeoCoordinatesUseCase.invoke(mockCoords) } returns Unit
            coEvery { getNearbyWikisUseCase.invoke(any(), any()) } returns Result.success(mockWikis)

            // When
            wikiLocalizationViewModel.fetchCoordinatesAndWikis()

            // Then
            coVerify(exactly = 1) { getLocationUseCase.invoke() }
            coVerify(exactly = 1) { saveGeoCoordinatesUseCase.invoke(mockCoords) }
            coVerify(exactly = 1) {
                getNearbyWikisUseCase.invoke(
                    mockCoords.latitude,
                    mockCoords.longitude
                )
            }
            assertEquals(false, wikiLocalizationViewModel.uiState.value.isLoading)
            assertEquals(mockCoords, wikiLocalizationViewModel.uiState.value.coords)
            assertEquals(mockWikis, wikiLocalizationViewModel.uiState.value.wikis)
            assertEquals(null, wikiLocalizationViewModel.uiState.value.error)
        }

    @Test
    fun `when getLocationFromGps fails, should update uiState with error`() = runTest {
        // Given
        val mockError = ErrorApp.UnknownErrorApp
        coEvery { getCoordinatesUseCase.invoke() } returns Result.failure(mockError)
        coEvery { getLocationUseCase.invoke() } returns Result.failure(mockError)

        // When
        wikiLocalizationViewModel.fetchCoordinatesAndWikis()

        // Then
        coVerify(exactly = 1) { getLocationUseCase.invoke() }
        coVerify(exactly = 0) { saveGeoCoordinatesUseCase.invoke(any()) }
        coVerify(exactly = 0) { getNearbyWikisUseCase.invoke(any(), any()) }
        assertEquals(false, wikiLocalizationViewModel.uiState.value.isLoading)
        assertEquals(mockError, wikiLocalizationViewModel.uiState.value.error)
        assertEquals(emptyList<WikiLocalization>(), wikiLocalizationViewModel.uiState.value.wikis)
        assertEquals(null, wikiLocalizationViewModel.uiState.value.coords)
    }

    // --- Tests de saveGeoCoordinates ---

    @Test
    fun `when saveGeoCoordinates is called, should invoke saveGeoCoordinatesUseCase`() = runTest {
        // Given
        val mockCoords = GeoCoordinates(10.0, 20.0, System.currentTimeMillis())
        val slot = slot<GeoCoordinates>()

        // When
        wikiLocalizationViewModel.saveGeoCoordinates(mockCoords)

        // Then
        coVerify(exactly = 1) { saveGeoCoordinatesUseCase.invoke(capture(slot)) }
        assertEquals(mockCoords, slot.captured)
    }
}