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

class GetNearbyWikisUseCaseTest {

    @RelaxedMockK
    private lateinit var wikiLocalizationRepository: WikiLocalizationRepository
    lateinit var getNearbyWikisUseCase: GetNearbyWikisUseCase

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        getNearbyWikisUseCase = GetNearbyWikisUseCase(wikiLocalizationRepository)
    }

    @Test
    fun `Return wikis success`() = runTest {
        //given
        val currentTime = System.currentTimeMillis()
        val mockCoords = GeoCoordinates(2.0, 3.0, currentTime)
        val mockWikis = listOf(WikiLocalization("a", "imgUrl", 2.0, 3.0, "url"))
        coEvery { wikiLocalizationRepository.getNearbyWikis(any(), any()) } returns Result.success(
            mockWikis
        )

        //when
        val coords = getNearbyWikisUseCase.invoke(mockCoords.latitude, mockCoords.longitude)

        //then
        coVerify {
            wikiLocalizationRepository.getNearbyWikis(
                mockCoords.latitude,
                mockCoords.longitude
            )
        }
        assertEquals(mockWikis, coords.getOrNull())
    }

    @Test
    fun `wikis failure return failure`() = runTest {
        //given
        val currentTime = System.currentTimeMillis()
        val mockCoords = GeoCoordinates(2.0, 3.0, currentTime)
        val mockError = ErrorApp.UnknownErrorApp
        coEvery { wikiLocalizationRepository.getNearbyWikis(any(), any()) } returns Result.failure(
            mockError
        )

        //when
        val coords = getNearbyWikisUseCase.invoke(mockCoords.latitude, mockCoords.longitude)

        //then
        coVerify {
            wikiLocalizationRepository.getNearbyWikis(
                mockCoords.latitude,
                mockCoords.longitude
            )
        }
        assertEquals(mockError, coords.exceptionOrNull())
    }

}