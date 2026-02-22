package com.example.quranoffline.media

sealed class PlaybackItem {

    abstract val id: String
    abstract val title: String
    abstract val url: String

    data class SurahItem(
        val surahId: Int,
        override val title: String,
        override val url: String,
        val reciterName: String,
    ) : PlaybackItem() {
        override val id: String = "surah_$surahId"
    }

    data class RadioItem(
        val radioId: Int,
        override val title: String,
        override val url: String
    ) : PlaybackItem() {
        override val id: String = "radio_$radioId"
    }
}