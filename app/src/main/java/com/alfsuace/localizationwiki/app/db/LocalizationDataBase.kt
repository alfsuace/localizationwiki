package com.alfsuace.localizationwiki.app.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.alfsuace.localizationwiki.localization.data.local.db.WikiLocalizationDao
import com.alfsuace.localizationwiki.localization.data.local.db.WikiLocalizationEntity

@Database(entities = [WikiLocalizationEntity::class], version = 1, exportSchema = false)
abstract class LocalizationDataBase : RoomDatabase() {

    abstract fun wikiLocalizationDao(): WikiLocalizationDao

}