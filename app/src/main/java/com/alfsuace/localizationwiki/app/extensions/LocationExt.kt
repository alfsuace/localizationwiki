package com.alfsuace.localizationwiki.app.extensions

import android.location.Location
import com.alfsuace.localizationwiki.localization.domain.GeoCoordinates

fun Location.toGeoCoordinates() =
    GeoCoordinates(latitude = this.latitude, longitude = this.longitude, System.currentTimeMillis())
