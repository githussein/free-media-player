package com.example.quranoffline.media

data class MediaState(
    val currentItem: PlaybackItem? = null,
    val isPlaying: Boolean = false,
    val isLoading: Boolean = false
)