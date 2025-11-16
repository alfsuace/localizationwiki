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

class GetCoordinatesUseCaseTest {

    @RelaxedMockK
    private lateinit var geoCoordinatesRepository: GeoCoordinatesRepository

    lateinit var getCoordinatesUseCase: GetCoordinatesUseCase

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        getCoordinatesUseCase = GetCoordinatesUseCase(geoCoordinatesRepository)
    }

    @Test
    fun `gets null and should return unknown error`() = runTest {
        //given
        coEvery { geoCoordinatesRepository.getCoordinates() } returns Result.failure(ErrorApp.UnknownErrorApp())

        //when
        val coords = getCoordinatesUseCase.invoke()

        //then
        coVerify { geoCoordinatesRepository.getCoordinates() }
        assertEquals(ErrorApp.UnknownErrorApp(), coords.exceptionOrNull())
    }

    @Test
    fun `gets cache expired and should return cache expired error`() = runTest {
        //given
        coEvery { geoCoordinatesRepository.getCoordinates() } returns Result.failure(ErrorApp.CacheExpiredErrorApp())

        //when
        val coords = getCoordinatesUseCase.invoke()

        //then
        coVerify { geoCoordinatesRepository.getCoordinates() }
        assertEquals(ErrorApp.CacheExpiredErrorApp(), coords.exceptionOrNull())
    }

    @Test
    fun `gets coords and should return coords`() = runTest {
        //given
        val currentTime = System.currentTimeMillis()
        val mockCoords = GeoCoordinates(2.0, 3.0, currentTime)
        coEvery { geoCoordinatesRepository.getCoordinates() } returns Result.success(mockCoords)

        //when
        val coords = getCoordinatesUseCase.invoke()

        //then
        coVerify { geoCoordinatesRepository.getCoordinates() }
        assertEquals(mockCoords, coords.getOrNull())
    }
}