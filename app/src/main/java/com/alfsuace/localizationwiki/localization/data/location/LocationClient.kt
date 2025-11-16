package com.alfsuace.localizationwiki.localization.data.location

import com.alfsuace.localizationwiki.app.domain.ErrorApp
import com.alfsuace.localizationwiki.app.extensions.toGeoCoordinates
import com.alfsuace.localizationwiki.localization.domain.GeoCoordinates
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.tasks.await
import org.koin.core.annotation.Single

@Single
class LocationClient(private val fusedLocationClient: FusedLocationProviderClient) {

    @Suppress("MissingPermission")
    suspend fun getCurrentLocation(): Result<GeoCoordinates> {
        return runCatching {
            val location = fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                CancellationTokenSource().token
            )
                .await()

            return Result.success(location.toGeoCoordinates())
        }.onFailure {
            return Result.failure(ErrorApp.UnknownErrorApp())
        }
    }

}