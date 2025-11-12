package com.alfsuace.localizationwiki.localization.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WikiLocalizationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWikis(vararg wikiLocalizations: WikiLocalizationEntity)

    @Query("SELECT * FROM $WIKI_LOCALIZATION_TABLE")
    suspend fun getAllWikis(): List<WikiLocalizationEntity>

    @Query("DELETE FROM $WIKI_LOCALIZATION_TABLE")
    suspend fun deleteAllWikis()

}