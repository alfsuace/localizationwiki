package com.alfsuace.localizationwiki.localization.data.local.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

const val WIKI_LOCALIZATION_TABLE = "wiki_localization_table"
const val WIKI_LOCALIZATION_TITLE = "title"

@Entity(tableName = WIKI_LOCALIZATION_TABLE)
data class WikiLocalizationEntity(
    @PrimaryKey
    @ColumnInfo(name = WIKI_LOCALIZATION_TITLE)
    val title: String,

    @ColumnInfo(name = "time_stamp")
    val timeStamp: Long,

    @ColumnInfo(name = "image_url")
    val imageUrl: String?,

    @ColumnInfo(name = "latitude")
    val latitude: Double,

    @ColumnInfo(name = "longitude")
    val longitude: Double,

    @ColumnInfo(name = "url")
    val url: String?
)
