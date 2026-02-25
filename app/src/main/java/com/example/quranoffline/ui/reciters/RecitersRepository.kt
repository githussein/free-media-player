package com.example.quranoffline.ui.reciters

import com.example.quranoffline.data.Mp3QuranApi
import com.example.quranoffline.data.ReciterResponse
import com.example.quranoffline.data.SurahResponse
import javax.inject.Inject


interface IRecitersRepository {
    suspend fun getAllReciters(): ReciterResponse
    suspend fun getReciterById(reciterId: String): ReciterResponse
    suspend fun getSurahList(): SurahResponse
}

class RecitersRepository @Inject constructor(
    private val mp3QuranApi: Mp3QuranApi
) : IRecitersRepository {
    override suspend fun getAllReciters(): ReciterResponse = mp3QuranApi.getAllReciters()
    override suspend fun getReciterById(reciterId: String) =
        mp3QuranApi.getReciterById(reciterId = reciterId, language = "en")

    override suspend fun getSurahList(): SurahResponse = mp3QuranApi.getSurahList()
}