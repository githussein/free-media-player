package com.example.quranoffline.ui.Radio

import com.example.quranoffline.data.Mp3QuranApi
import com.example.quranoffline.data.RadioResponse
<<<<<<< HEAD
import com.example.quranoffline.util.LocaleHelper
import javax.inject.Inject
import javax.inject.Singleton
=======
import javax.inject.Inject
>>>>>>> origin/feature/radio-streaming

interface IRadioRepository {
    suspend fun getRadioStations(): RadioResponse
}

<<<<<<< HEAD
@Singleton
=======
>>>>>>> origin/feature/radio-streaming
class RadioRepository @Inject constructor(
    private val mp3QuranApi: Mp3QuranApi
) : IRadioRepository {

<<<<<<< HEAD
    private val cachedResponses = mutableMapOf<String, RadioResponse>()

    override suspend fun getRadioStations(): RadioResponse {
        val language = LocaleHelper.getApiLanguage()
        cachedResponses[language]?.let { return it }

        val response = mp3QuranApi.getRadioStations(language = language)
        cachedResponses[language] = response
        return response
=======
    override suspend fun getRadioStations(): RadioResponse {
        return mp3QuranApi.getRadioStations()
>>>>>>> origin/feature/radio-streaming
    }
}