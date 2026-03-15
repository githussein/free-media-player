package com.example.quranoffline.media

import android.content.ComponentName
import android.content.Context
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.quranoffline.data.Radio
import com.example.quranoffline.data.Surah
import com.example.quranoffline.data.SurahUi
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
<<<<<<< HEAD
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.lifecycle.viewModelScope

@HiltViewModel
class MediaViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private var controller: MediaController? = null
=======
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MediaViewModel @Inject constructor(
    @param:ApplicationContext private val context: Context
) : ViewModel() {

    private var controller: MediaController? = null

>>>>>>> origin/feature/radio-streaming
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
<<<<<<< HEAD
=======

>>>>>>> origin/feature/radio-streaming
                    override fun onIsPlayingChanged(isPlaying: Boolean) {
                        _mediaState.value = _mediaState.value.copy(isPlaying = isPlaying)

                        if (isPlaying) {
                            val index = controller?.currentMediaItemIndex ?: -1
                            if (index != -1 && index < currentPlaylist.size) {
                                _mediaState.value =
                                    _mediaState.value.copy(currentItem = currentPlaylist[index])
                            }
<<<<<<< HEAD
                            startProgressUpdate()
                        }
                    }

                    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                        val index = controller?.currentMediaItemIndex ?: return
                        if (index in currentPlaylist.indices) {
                            _mediaState.value =
                                _mediaState.value.copy(
                                    currentItem = currentPlaylist[index],
                                    duration = controller?.duration?.coerceAtLeast(0L) ?: 0L
                                )
=======
>>>>>>> origin/feature/radio-streaming
                        }
                    }

                    override fun onPlaybackStateChanged(state: Int) {
                        val loading = state == Player.STATE_BUFFERING
<<<<<<< HEAD
                        _mediaState.value = _mediaState.value.copy(
                            isLoading = loading,
                            duration = controller?.duration?.coerceAtLeast(0L) ?: 0L
                        )
=======
                        _mediaState.value = _mediaState.value.copy(isLoading = loading)
>>>>>>> origin/feature/radio-streaming
                    }
                })
            },
            ContextCompat.getMainExecutor(context)
        )
    }

<<<<<<< HEAD
=======

>>>>>>> origin/feature/radio-streaming
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

    fun playSurah(
        surah: Surah,
<<<<<<< HEAD
        reciterId: Int,
        reciterName: String,
        surahList: List<SurahUi>
    ) {
        val current = _mediaState.value.currentItem
        if (current is PlaybackItem.SurahItem && current.surahId == surah.id && current.reciterId == reciterId) {
            if (_mediaState.value.isPlaying) pause() else resume()
            return
        }

=======
        reciterName: String,
        surahList: List<SurahUi>
    ) {
>>>>>>> origin/feature/radio-streaming
        val playbackList = surahList.mapNotNull { surahUi ->
            val s = surahUi.surah
            val url = surahUi.server
            if (s != null && !url.isNullOrEmpty()) {
                PlaybackItem.SurahItem(
                    surahId = s.id,
<<<<<<< HEAD
                    reciterId = reciterId,
=======
>>>>>>> origin/feature/radio-streaming
                    title = s.name,
                    url = url,
                    reciterName = reciterName
                )
            } else null
        }

        val startIndex = playbackList.indexOfFirst { it.surahId == surah.id }
        if (startIndex == -1 || playbackList.isEmpty()) return

        setPlaylist(playbackList, startIndex)
        play(playbackList[startIndex])
    }

    fun playRadio(radio: Radio) {
<<<<<<< HEAD
        val current = _mediaState.value.currentItem
        if (current is PlaybackItem.RadioItem && current.radioId == radio.id) {
            if (_mediaState.value.isPlaying) pause() else resume()
            return
        }

=======
>>>>>>> origin/feature/radio-streaming
        val radioItem = PlaybackItem.RadioItem(
            radioId = radio.id,
            title = radio.name,
            url = radio.url
        )

        val mediaItem = MediaItem.Builder()
            .setUri(radioItem.url)
            .setMediaId(radioItem.id)
            .setTag(radioItem)
            .build()

        controller?.clearMediaItems()
        controller?.setMediaItem(mediaItem)
        controller?.prepare()
        controller?.play()

        _mediaState.value = _mediaState.value.copy(
            currentItem = radioItem,
            isPlaying = true
        )
    }

    fun pause() = controller?.pause()
<<<<<<< HEAD

    fun resume() = controller?.play()

=======
    fun resume() = controller?.play()
>>>>>>> origin/feature/radio-streaming
    fun stop() {
        controller?.stop()
        _mediaState.value = MediaState()
    }

    fun next() = controller?.seekToNextMediaItem()

    fun previous() = controller?.seekToPreviousMediaItem()
<<<<<<< HEAD

    fun seekTo(position: Long) {
        controller?.seekTo(position)
        _mediaState.value = _mediaState.value.copy(progress = position)
    }

    private fun startProgressUpdate() {
        viewModelScope.launch {
            while (isActive && _mediaState.value.isPlaying) {
                _mediaState.value = _mediaState.value.copy(
                    progress = controller?.currentPosition ?: 0L,
                    duration = controller?.duration?.coerceAtLeast(0L) ?: 0L
                )
                delay(1000)
            }
        }
    }
=======
>>>>>>> origin/feature/radio-streaming
}