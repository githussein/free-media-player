package com.example.quranoffline.media

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
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

    private var playlist: List<PlaybackItem> = emptyList()
    private var currentIndex: Int = -1


    init {
        player.addListener(object : Player.Listener {

            override fun onPlaybackStateChanged(state: Int) {
                when (state) {
                    Player.STATE_BUFFERING -> {
                        _mediaState.value = _mediaState.value.copy(
                            isLoading = true
                        )
                    }

                    Player.STATE_READY -> {
                        _mediaState.value = _mediaState.value.copy(
                            isLoading = false,
                            isPlaying = player.isPlaying
                        )
                    }

                    Player.STATE_ENDED -> {
                        _mediaState.value = _mediaState.value.copy(
                            isPlaying = false
                        )
                    }
                }
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _mediaState.value = _mediaState.value.copy(
                    isPlaying = isPlaying
                )
            }
        })
    }

    fun play(item: PlaybackItem) {
        currentIndex = playlist.indexOf(item)

        _mediaState.value = MediaState(
            currentItem = item,
            isPlaying = false,
            isLoading = true
        )

        player.stop()
        player.clearMediaItems()

        val mediaItem = MediaItem.fromUri(item.url)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
    }

    fun setPlaylist(list: List<PlaybackItem>) {
        playlist = list
    }

    fun playNext() {
        if (currentIndex < playlist.lastIndex) {
            currentIndex++
            play(playlist[currentIndex])
        }
    }

    fun playPrevious() {
        if (currentIndex > 0) {
            currentIndex--
            play(playlist[currentIndex])
        }
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