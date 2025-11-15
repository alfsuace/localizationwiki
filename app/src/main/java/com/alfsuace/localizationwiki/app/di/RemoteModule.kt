package com.alfsuace.localizationwiki.app.di

import okhttp3.Dns
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.Inet4Address
import java.net.InetAddress

@Module
class RemoteModule {

    private val url = "https://en.wikipedia.org/w/"

    @Single
    fun provideLoggingInterceptor() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    fun headerInterceptor(): Interceptor = Interceptor { chain ->
        val request = chain.request()
            .newBuilder()
            .header(
                "User-Agent",
                "LocalizationWikiApp/1.0 Retrofit/3.0.0"
            )
            .build()
        chain.proceed(request)
    }

    fun ipv4OnlyDns(): Dns = Dns { hostname ->
        InetAddress.getAllByName(hostname).filterIsInstance<Inet4Address>()
    }

    @Single
    fun provideOkHttpClient(
        logginInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(logginInterceptor)
            .addInterceptor(headerInterceptor())
            .retryOnConnectionFailure(true)
            .dns(ipv4OnlyDns())
            .build()
    }

    @Single
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val retrofit = Retrofit
            .Builder()
            .baseUrl(url)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit
    }

}