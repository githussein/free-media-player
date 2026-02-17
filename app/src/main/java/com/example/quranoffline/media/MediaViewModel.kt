package com.example.quranoffline.media

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MediaViewModel @Inject constructor(
    private val playbackService: PlaybackService
) : ViewModel() {
    val mediaState: StateFlow<MediaState> = playbackService.mediaState

    fun play(item: PlaybackItem) {
        playbackService.play(item)
    }

    fun pause() = playbackService.pause()

    fun resume() = playbackService.resume()

    fun stop() = playbackService.stop()
}