package com.example.quranoffline.data

import retrofit2.http.GET
import retrofit2.http.Query

interface Mp3QuranApi {

    @GET("reciters")
    suspend fun getAllReciters(
        @Query("language") language: String = "eng"
    ): ReciterResponse

    @GET("reciters")
    suspend fun getReciterById(
        @Query("language") language: String = "eng",
        @Query("reciter") reciterId: String
    ): ReciterResponse

    @GET("suwar")
    suspend fun getSurahName(
        @Query("language") language: String = "eng",
    ): SurahResponse

    @GET("radios")
    suspend fun getRadioStations(
        @Query("language") language: String = "eng"
    ): RadioStationsResponse
}