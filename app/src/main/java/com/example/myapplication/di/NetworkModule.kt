package com.example.myapplication.di

import com.example.myapplication.data.remote.api.StackExchangeApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object
NetworkModule {

    private const val BASE_URL = "https://api.stackexchange.com/"

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        // Logs request/response bodies in debug builds for easier debugging
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()) // JSON → Kotlin data classes
            .build()

    @Provides
    @Singleton
    fun provideStackExchangeApi(retrofit: Retrofit): StackExchangeApi =
        retrofit.create(StackExchangeApi::class.java)
}