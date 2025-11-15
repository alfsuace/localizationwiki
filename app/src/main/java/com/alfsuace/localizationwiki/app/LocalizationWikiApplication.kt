package com.alfsuace.localizationwiki.app

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.alfsuace.localizationwiki.app.di.AppModule
import com.alfsuace.localizationwiki.app.di.LocalModule
import com.alfsuace.localizationwiki.app.di.RemoteModule
import com.alfsuace.localizationwiki.localization.di.LocalizationModule
import okhttp3.Dns
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module
import java.net.Inet4Address
import java.net.InetAddress

class LocalizationWikiApplication : Application(), ImageLoaderFactory {
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

    override fun newImageLoader(): ImageLoader {
        val okHttp = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor { chain ->
                chain.proceed(
                    chain.request().newBuilder()
                        .header("User-Agent", "LocalizationWikiApp/1.0 Retrofit/3.0.0")
                        .build()
                )
            }
            .dns(Dns { hostname ->
                InetAddress.getAllByName(hostname).filterIsInstance<Inet4Address>()
            })
            .retryOnConnectionFailure(true)
            .build()

        return ImageLoader.Builder(this)
            .okHttpClient(okHttp)
            .build()
    }

}