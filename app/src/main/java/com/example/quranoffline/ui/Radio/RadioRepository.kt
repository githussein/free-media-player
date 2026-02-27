package com.example.quranoffline.ui.Radio

import com.example.quranoffline.data.Mp3QuranApi
import com.example.quranoffline.data.RadioResponse
import dagger.Provides
import javax.inject.Inject

interface IRadioRepository {
    suspend fun getRadioStations(): RadioResponse
}

class RadioRepository @Inject constructor(
    private val mp3QuranApi: Mp3QuranApi
) : IRadioRepository {

    @Volatile
    private var cachedResponse: RadioResponse? = null

    override suspend fun getRadioStations(): RadioResponse {
        cachedResponse?.let { return it } // return cached if available

        val response = mp3QuranApi.getRadioStations()
        cachedResponse = response // store for future use
        return response
    }
}