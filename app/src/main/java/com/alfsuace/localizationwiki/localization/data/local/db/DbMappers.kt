package com.alfsuace.localizationwiki.localization.data.local.db

import com.alfsuace.localizationwiki.localization.domain.WikiLocalization

fun WikiLocalizationEntity.toModel(): WikiLocalization {
    return WikiLocalization(
        this.title,
        this.imageUrl,
        this.latitude,
        this.longitude,
        this.url
    )
}

fun WikiLocalization.toEntity(timeStamp: Long): WikiLocalizationEntity {
    return WikiLocalizationEntity(
        this.title,
        timeStamp,
        this.imageUrl,
        this.latitude,
        this.longitude,
        this.url
    )
}