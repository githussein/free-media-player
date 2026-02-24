package com.example.quranoffline.ui.Radio

import com.example.quranoffline.data.Mp3QuranApi
import com.example.quranoffline.data.RadioResponse
import javax.inject.Inject

interface IRadioRepository {
    suspend fun getRadioStations(): RadioResponse
}

class RadioRepository @Inject constructor(
    private val mp3QuranApi: Mp3QuranApi
) : IRadioRepository {

    override suspend fun getRadioStations(): RadioResponse {
        return mp3QuranApi.getRadioStations()
    }
}