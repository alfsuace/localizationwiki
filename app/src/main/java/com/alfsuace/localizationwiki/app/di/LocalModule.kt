package com.alfsuace.localizationwiki.app.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.alfsuace.localizationwiki.app.db.LocalizationDataBase
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@ComponentScan
class LocalModule {

    @Single
    fun provideDataBase(context: Context): LocalizationDataBase {
        val db = Room.databaseBuilder(
            context,
            LocalizationDataBase::class.java,
            "localization-db"
        )
        return db.build()
    }

    @Single
    fun provideDataStore(context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = {
                context.preferencesDataStoreFile("superhero_preferences")
            }
        )
    }
}