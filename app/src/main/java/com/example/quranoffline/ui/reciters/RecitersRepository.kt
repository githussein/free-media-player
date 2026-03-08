package com.example.quranoffline.ui.reciters

import com.example.quranoffline.data.Mp3QuranApi
import com.example.quranoffline.data.ReciterResponse
import com.example.quranoffline.data.SurahResponse
import com.example.quranoffline.util.LocaleHelper
import javax.inject.Inject
import javax.inject.Singleton


interface IRecitersRepository {
    suspend fun getAllReciters(): ReciterResponse
    suspend fun getReciterById(reciterId: String): ReciterResponse
    suspend fun getSurahList(): SurahResponse
}

@Singleton
class RecitersRepository @Inject constructor(
    private val mp3QuranApi: Mp3QuranApi
) : IRecitersRepository {

    private val cachedAllReciters = mutableMapOf<String, ReciterResponse>()
    private val cachedSurahList = mutableMapOf<String, SurahResponse>()
    private val cachedRecitersById = mutableMapOf<String, ReciterResponse>()

    override suspend fun getAllReciters(): ReciterResponse {

        val language = LocaleHelper.getApiLanguage()

        cachedAllReciters[language]?.let { return it }

        val response = mp3QuranApi.getAllReciters(language = language)

        cachedAllReciters[language] = response
        return response
    }

    override suspend fun getReciterById(reciterId: String): ReciterResponse {
        cachedRecitersById[reciterId]?.let { return it }
        val response = mp3QuranApi.getReciterById(reciterId = reciterId, language = "en")
        cachedRecitersById[reciterId] = response
        return response
    }

    override suspend fun getSurahList(): SurahResponse {

        val language = LocaleHelper.getApiLanguage()

        cachedSurahList[language]?.let { return it }

        val response = mp3QuranApi.getSurahList(language = language)

        cachedSurahList[language] = response
        return response
    }
}