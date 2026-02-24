package com.example.quranoffline.ui.RadioStationsScreen

import com.example.quranoffline.data.Mp3QuranApi
import com.example.quranoffline.data.RadioStationsResponse
import javax.inject.Inject

interface IRadioStationRepository {
    suspend fun getRadioStations(): RadioStationsResponse
}

class RadioStationsRepository @Inject constructor(
    private val mp3QuranApi: Mp3QuranApi
) : IRadioStationRepository {

    override suspend fun getRadioStations(): RadioStationsResponse {
        return mp3QuranApi.getRadioStations()
    }
}