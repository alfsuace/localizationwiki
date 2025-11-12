package com.alfsuace.localizationwiki.localization.data.remote

import com.alfsuace.localizationwiki.localization.domain.WikiLocalization

fun WikiLocalizationApiModel.toModel(): WikiLocalization {
    return WikiLocalization(
        title,
        thumbnail?.source,
        coordinates?.firstOrNull()?.lat ?: 0.0,
        coordinates?.firstOrNull()?.lon ?: 0.0,
        fullUrl
    )
}
