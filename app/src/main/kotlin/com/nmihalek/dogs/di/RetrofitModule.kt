package com.nmihalek.dogs.di

import android.app.Application
import android.util.Log
import com.google.gson.Gson
import com.nmihalek.dogs.feature.breeddetails.data.PicturesService
import com.nmihalek.dogs.feature.breeds.data.BreedsService
import com.nmihalek.dogs.feature.common.data.hasNetwork
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

private const val BASE_URL: String = "https://dog.ceo/api/"

@Module
@InstallIn(SingletonComponent::class)
abstract class RetrofitModule private constructor() {
    companion object {

        @Provides
        @Singleton
        fun provideOkHttpClient(): OkHttpClient {
            return OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()
        }

        @Provides
        fun provideGson(): Gson = Gson()

        @Provides
        @Singleton
        fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit =
            Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .build()

        @Provides
        @Singleton
        fun provideBreedsService(retrofit: Retrofit): BreedsService =
            retrofit.create()

        @Provides
        @Singleton
        fun providePicturesService(retrofit: Retrofit): PicturesService =
            retrofit.create()
    }
}
