package com.alfsuace.localizationwiki.localization.domain

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SaveGeoCoordinatesUseCaseTest {

    @RelaxedMockK
    private lateinit var geoCoordinatesRepository: GeoCoordinatesRepository
    lateinit var saveGeoCoordinatesUseCase: SaveGeoCoordinatesUseCase

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        saveGeoCoordinatesUseCase = SaveGeoCoordinatesUseCase(geoCoordinatesRepository)
    }

    @Test
    fun `should save geo coordinates`() = runTest {
        //given
        val currentTime = System.currentTimeMillis()
        val mockCoords = GeoCoordinates(2.0, 3.0, currentTime)
        val slot = slot<GeoCoordinates>()

        coEvery { geoCoordinatesRepository.saveCoordinates(mockCoords) } returns Unit

        // When
        saveGeoCoordinatesUseCase(mockCoords)

        // Then
        coVerify(exactly = 1) { saveGeoCoordinatesUseCase.invoke(capture(slot)) }
        assertEquals(mockCoords, slot.captured)
    }

}