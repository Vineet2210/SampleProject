package com.vineet.shelfapp.networkmodule

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
@InstallIn(ViewModelComponent::class)
@Module

//Retrofit Instance
class NetworkModule {
    private val baseUrl="https://www.jsonkeeper.com"
    private val baseUrl2="https://api.first.org"

        @Provides
        @ViewModelScoped
        fun provideAPI(): ApiInterface {
            val okHttpClient = OkHttpClient.Builder()
            okHttpClient
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)

            return Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create()).client(okHttpClient.build()).build()
                .create(ApiInterface::class.java)
        }

    }