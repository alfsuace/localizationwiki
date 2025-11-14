package com.alfsuace.localizationwiki.app

import android.app.Application
import com.alfsuace.localizationwiki.app.di.AppModule
import com.alfsuace.localizationwiki.app.di.LocalModule
import com.alfsuace.localizationwiki.app.di.RemoteModule
import com.alfsuace.localizationwiki.localization.di.LocalizationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module

class LocalizationWikiApplication : Application(){

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@LocalizationWikiApplication)
            modules(
                AppModule().module,
                RemoteModule().module,
                LocalModule().module,
                LocalizationModule().module
            )
        }
    }

}