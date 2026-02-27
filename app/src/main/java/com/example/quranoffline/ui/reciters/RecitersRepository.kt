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

    @Volatile
    private var cachedAllReciters: ReciterResponse? = null

    @Volatile
    private var cachedSurahList: SurahResponse? = null

    private val cachedRecitersById = mutableMapOf<String, ReciterResponse>()

    override suspend fun getAllReciters(): ReciterResponse {
        cachedAllReciters?.let { return it }
        val response = mp3QuranApi.getAllReciters()
        cachedAllReciters = response
        return response
    }

    override suspend fun getReciterById(reciterId: String): ReciterResponse {
        cachedRecitersById[reciterId]?.let { return it }
        val response = mp3QuranApi.getReciterById(reciterId = reciterId, language = "en")
        cachedRecitersById[reciterId] = response
        return response
    }

    override suspend fun getSurahList(): SurahResponse {
        cachedSurahList?.let { return it }
        val response = mp3QuranApi.getSurahList()
        cachedSurahList = response
        return response
    }
}