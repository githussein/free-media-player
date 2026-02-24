package com.example.quranoffline.di

import com.example.quranoffline.data.Mp3QuranApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Mp3QuranModule {

    @Provides
    @Singleton
    fun provideMp3QuranApiService(): Mp3QuranApi =
        Retrofit.Builder()
            .baseUrl("https://www.mp3quran.net/api/v3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Mp3QuranApi::class.java)
}