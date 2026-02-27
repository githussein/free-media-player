package com.example.quranoffline.di

import com.example.quranoffline.ui.Radio.IRadioRepository
import com.example.quranoffline.ui.Radio.RadioRepository
import com.example.quranoffline.ui.reciters.IRecitersRepository
import com.example.quranoffline.ui.reciters.RecitersRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindRadioRepository(
        radioRepository: RadioRepository
    ): IRadioRepository

    @Binds
    abstract fun bindRecitersRepository(
        recitersRepository: RecitersRepository
    ): IRecitersRepository
}