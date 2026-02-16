package com.example.quranoffline.media

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlaybackService @Inject constructor(
    @param:ApplicationContext private val context: Context
) {

    private val player: ExoPlayer = ExoPlayer.Builder(context).build()
    private val _mediaState = MutableStateFlow(MediaState())
    val mediaState: StateFlow<MediaState> = _mediaState

    fun play(item: PlaybackItem) {
        player.stop()
        player.clearMediaItems()

        val mediaItem = MediaItem.fromUri(item.url)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
    }

    fun pause() {
        player.pause()
    }

    fun resume() {
        player.play()
    }

    fun stop() {
        player.stop()
        player.clearMediaItems()
    }

    fun isPlaying(): Boolean = player.isPlaying

    fun release() {
        player.release()
    }
}