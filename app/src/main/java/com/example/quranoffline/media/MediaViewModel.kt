package com.example.quranoffline.media

import android.content.ComponentName
import android.content.Context
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MediaViewModel @Inject constructor(
    @param:ApplicationContext private val context: Context
) : ViewModel() {

    private var controller: MediaController? = null

    private val _mediaState = MutableStateFlow(MediaState())
    val mediaState: StateFlow<MediaState> = _mediaState.asStateFlow()
    private var currentPlaylist: List<PlaybackItem> = emptyList()

    init {
        val sessionToken = SessionToken(
            context.applicationContext,
            ComponentName(context, MediaPlaybackService::class.java)
        )

        val controllerFuture =
            MediaController.Builder(context.applicationContext, sessionToken)
                .buildAsync()

        controllerFuture.addListener(
            {
                controller = controllerFuture.get()

                controller?.addListener(object : Player.Listener {

                    override fun onIsPlayingChanged(isPlaying: Boolean) {
                        _mediaState.value = _mediaState.value.copy(isPlaying = isPlaying)

                        if (isPlaying) {
                            val index = controller?.currentMediaItemIndex ?: -1
                            if (index != -1 && index < currentPlaylist.size) {
                                _mediaState.value =
                                    _mediaState.value.copy(currentItem = currentPlaylist[index])
                            }
                        }
                    }

                    override fun onPlaybackStateChanged(state: Int) {
                        val loading = state == Player.STATE_BUFFERING
                        _mediaState.value = _mediaState.value.copy(isLoading = loading)
                    }
                })
            },
            ContextCompat.getMainExecutor(context)
        )
    }


    fun setPlaylist(list: List<PlaybackItem>, startIndex: Int = 0) {
        currentPlaylist = list
        val mediaItems = list.map { item ->
            MediaItem.Builder()
                .setUri(item.url)
                .setMediaId(item.id)
                .setTag(item)
                .build()
        }

        controller?.setMediaItems(mediaItems, startIndex, 0L)
        controller?.prepare()
    }

    fun play(item: PlaybackItem) {
        val index = currentPlaylist.indexOfFirst { it.id == item.id }
        if (index != -1) {
            controller?.seekToDefaultPosition(index)
            controller?.play()
        }
    }

    fun pause() = controller?.pause()
    fun resume() = controller?.play()
    fun stop() {
        controller?.stop()
        _mediaState.value = MediaState()
    }

    fun next() = controller?.seekToNextMediaItem()

    fun previous() = controller?.seekToPreviousMediaItem()
}