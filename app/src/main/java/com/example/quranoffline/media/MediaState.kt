package com.example.quranoffline.media

data class MediaState(
    val currentItem: PlaybackItem? = null,
    val isPlaying: Boolean = false,
    val isLoading: Boolean = false,
    val progress: Long = 0L,
    val duration: Long = 0L
)