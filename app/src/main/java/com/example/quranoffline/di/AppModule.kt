package com.example.quranoffline.di

import com.example.quranoffline.data.BookService
import com.example.quranoffline.data.QuranService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideQuranService(): QuranService {
        return QuranService
    }

    @Provides
    @Singleton
    fun provideBookService(): BookService {
        return BookService
    }
}