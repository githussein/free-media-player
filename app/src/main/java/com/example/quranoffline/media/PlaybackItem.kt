package com.example.quranoffline.media

import java.util.Locale

sealed class PlaybackItem {

    abstract val id: String
    abstract val url: String
    abstract val titleEn: String
    abstract val titleAr: String?

    val title: String
        get() = if (Locale.getDefault().language == "ar") titleAr ?: titleEn else titleEn

    data class SurahItem(
        val surahId: Int,
        val reciterId: Int,
        override val titleEn: String,
        override val url: String,
        val reciterName: String,
        override val titleAr: String? = null
    ) : PlaybackItem() {
        override val id: String = "surah_${reciterId}_$surahId"
    }

    data class RadioItem(
        val radioId: Int,
        override val titleEn: String,
        override val url: String,
        override val titleAr: String? = null
    ) : PlaybackItem() {
        override val id: String = "radio_$radioId"
    }
}