package com.example.quranoffline.ui.Radio

import com.example.quranoffline.data.Mp3QuranApi
import com.example.quranoffline.data.RadioResponse
import com.example.quranoffline.util.LocaleHelper
import javax.inject.Inject
import javax.inject.Singleton

interface IRadioRepository {
    suspend fun getRadioStations(): RadioResponse
}

@Singleton
class RadioRepository @Inject constructor(
    private val mp3QuranApi: Mp3QuranApi
) : IRadioRepository {

    private val cachedResponses = mutableMapOf<String, RadioResponse>()

    override suspend fun getRadioStations(): RadioResponse {
        val language = LocaleHelper.getApiLanguage()
        cachedResponses[language]?.let { return it }

        val response = mp3QuranApi.getRadioStations(language = language)
        cachedResponses[language] = response
        return response
    }
}