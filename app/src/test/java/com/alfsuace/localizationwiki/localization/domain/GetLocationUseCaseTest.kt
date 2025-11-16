package com.alfsuace.localizationwiki.localization.domain

import com.alfsuace.localizationwiki.app.domain.ErrorApp
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetLocationUseCaseTest {

    @RelaxedMockK
    private lateinit var locationRepository: LocationRepository
    lateinit var getLocationUseCase: GetLocationUseCase

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        getLocationUseCase = GetLocationUseCase(locationRepository)
    }

    @Test
    fun `Return location success`() = runTest {
        //given
        val currentTime = System.currentTimeMillis()
        val mockCoords = GeoCoordinates(2.0, 3.0, currentTime)
        coEvery { locationRepository.getCurrentLocation() } returns Result.success(mockCoords)

        //when
        val coords = getLocationUseCase.invoke()

        //then
        coVerify { locationRepository.getCurrentLocation() }
        assertEquals(mockCoords, coords.getOrNull())
    }

    @Test
    fun `Return location failure return unknown error`() = runTest {
        //given
        coEvery { locationRepository.getCurrentLocation() } returns Result.failure(ErrorApp.UnknownErrorApp())

        //when
        val coords = getLocationUseCase.invoke()

        //then
        coVerify { locationRepository.getCurrentLocation() }
        assertEquals(ErrorApp.UnknownErrorApp(), coords.exceptionOrNull())
    }

}