package com.example.quranoffline.media

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.example.quranoffline.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


@AndroidEntryPoint
class MediaPlaybackService : MediaSessionService() {

    private lateinit var player: ExoPlayer
    private var mediaSession: MediaSession? = null

    private var playlist: List<PlaybackItem> = emptyList()
    private var currentIndex: Int = 0

    private val _mediaState = MutableStateFlow(MediaState())
    val mediaState: StateFlow<MediaState> = _mediaState.asStateFlow()

    override fun onCreate() {
        super.onCreate()

        player = ExoPlayer.Builder(this).build()

        player.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                when (state) {
                    Player.STATE_BUFFERING -> _mediaState.value =
                        _mediaState.value.copy(isLoading = true)

                    Player.STATE_READY -> _mediaState.value = _mediaState.value.copy(
                        isPlaying = player.isPlaying,
                        isLoading = false
                    )

                    Player.STATE_ENDED -> if (currentIndex < playlist.lastIndex) playNext() else stop()
                }
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _mediaState.value = _mediaState.value.copy(isPlaying = isPlaying)
            }
        })

        mediaSession = MediaSession.Builder(this, player).build()

        startForegroundService()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? =
        mediaSession

    private fun startForegroundService() {
        val channelId = "media_playback_channel"
        val channelName = "Quran Playback"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Media playback controls"
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        // Build the notification
        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Quran Playback")
            .setContentText("Playing audio in background")
            .setSmallIcon(R.drawable.moshaf)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true) // Cannot be swiped away
            .build()

        startForeground(1, notification)
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, "media_channel")
            .setContentTitle("Quran Playback")
            .setContentText("Playing audio")
            .setSmallIcon(R.drawable.moshaf)
            .build()
    }

    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
        super.onDestroy()
    }

    fun setPlaylist(list: List<PlaybackItem>, startIndex: Int = 0) {
        playlist = list
        currentIndex = startIndex

        val mediaItems = list.map {
            MediaItem.Builder()
                .setUri(it.url)
                .setMediaId(it.id.toString())
                .setTag(it)
                .build()
        }

        player.setMediaItems(mediaItems, startIndex, 0L)
        player.prepare()
        player.play()
        _mediaState.value =
            MediaState(currentItem = list[startIndex], isPlaying = true, isLoading = true)
    }

    fun play(item: PlaybackItem) {
        val index = playlist.indexOfFirst { it.id == item.id }
        if (index == -1) return
        currentIndex = index
        player.seekTo(index, 0L)
        player.play()
        _mediaState.value = MediaState(currentItem = item, isPlaying = true)
    }

    fun playNext() {
        if (currentIndex < playlist.lastIndex) {
            currentIndex++
            val item = playlist[currentIndex]
            player.seekTo(currentIndex, 0L)
            player.play()
            _mediaState.value = MediaState(currentItem = item, isPlaying = true)
        }
    }

    fun playPrevious() {
        if (currentIndex > 0) {
            currentIndex--
            val item = playlist[currentIndex]
            player.seekTo(currentIndex, 0L)
            player.play()
            _mediaState.value = MediaState(currentItem = item, isPlaying = true)
        }
    }

    fun pause() = player.pause()

    fun resume() = player.play()

    fun stop() {
        player.stop()
        player.clearMediaItems()
        _mediaState.value = MediaState()
    }
}