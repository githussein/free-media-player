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
import android.app.PendingIntent
import android.content.Intent
import com.example.quranoffline.MainActivity
import com.example.quranoffline.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.media3.session.MediaNotification
import androidx.media3.session.DefaultMediaNotificationProvider
import androidx.media3.session.CommandButton
import androidx.core.content.ContextCompat
import com.google.common.collect.ImmutableList
import androidx.media3.common.ForwardingPlayer


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

        player = ExoPlayer.Builder(this)
            .setAudioAttributes(
                androidx.media3.common.AudioAttributes.Builder()
                    .setUsage(androidx.media3.common.C.USAGE_MEDIA)
                    .setContentType(androidx.media3.common.C.CONTENT_TYPE_SPEECH)
                    .build(),
                true
            )
            .setHandleAudioBecomingNoisy(true)
            .build()

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

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        mediaSession = MediaSession.Builder(this, object : ForwardingPlayer(player) {
            private fun isRtl(): Boolean {
                return resources.configuration.layoutDirection == android.view.View.LAYOUT_DIRECTION_RTL
            }

            override fun seekToNext() {
                if (isRtl()) super.seekToPrevious() else super.seekToNext()
            }

            override fun seekToPrevious() {
                if (isRtl()) super.seekToNext() else super.seekToPrevious()
            }

            override fun seekToNextMediaItem() {
                if (isRtl()) super.seekToPreviousMediaItem() else super.seekToNextMediaItem()
            }

            override fun seekToPreviousMediaItem() {
                if (isRtl()) super.seekToNextMediaItem() else super.seekToPreviousMediaItem()
            }

            override fun hasNextMediaItem(): Boolean {
                return if (isRtl()) super.hasPreviousMediaItem() else super.hasNextMediaItem()
            }

            override fun hasPreviousMediaItem(): Boolean {
                return if (isRtl()) super.hasNextMediaItem() else super.hasPreviousMediaItem()
            }

            override fun isCommandAvailable(command: Int): Boolean {
                if (!isRtl()) return super.isCommandAvailable(command)
                return when (command) {
                    Player.COMMAND_SEEK_TO_NEXT, Player.COMMAND_SEEK_TO_NEXT_MEDIA_ITEM -> {
                        super.isCommandAvailable(Player.COMMAND_SEEK_TO_PREVIOUS) || super.isCommandAvailable(Player.COMMAND_SEEK_TO_PREVIOUS_MEDIA_ITEM)
                    }
                    Player.COMMAND_SEEK_TO_PREVIOUS, Player.COMMAND_SEEK_TO_PREVIOUS_MEDIA_ITEM -> {
                        super.isCommandAvailable(Player.COMMAND_SEEK_TO_NEXT) || super.isCommandAvailable(Player.COMMAND_SEEK_TO_NEXT_MEDIA_ITEM)
                    }
                    else -> super.isCommandAvailable(command)
                }
            }
        })
            .setSessionActivity(pendingIntent)
            .build()
            
        setupNotification()
    }

    private fun setupNotification() {
        val notificationManager = getSystemService(NotificationManager::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                getString(R.string.media_playback_channel_name),
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = getString(R.string.media_notification_channel_description)
            }
            notificationManager.createNotificationChannel(channel)
        }

        val notificationProvider = DefaultMediaNotificationProvider.Builder(this@MediaPlaybackService)
            .setChannelId(CHANNEL_ID)
            .setNotificationId(101)
            .build()
        
        setMediaNotificationProvider(notificationProvider)
    }
    
    companion object {
        private const val CHANNEL_ID = "media_playback_channel"
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? =
        mediaSession

    override fun onTaskRemoved(rootIntent: android.content.Intent?) {
        val player = mediaSession?.player
        if (player == null || !player.playWhenReady || player.mediaItemCount == 0 || player.playbackState == Player.STATE_ENDED) {
            stopSelf()
        }
    }

    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
        super.onDestroy()
    }

    override fun onConfigurationChanged(newConfig: android.content.res.Configuration) {
        super.onConfigurationChanged(newConfig)
        // Refresh all media items in the playlist with updated localized strings
        for (i in 0 until player.mediaItemCount) {
            val tag = player.getMediaItemAt(i).localConfiguration?.tag as? PlaybackItem
            if (tag != null) {
                val updatedItem = createMediaItem(tag)
                player.replaceMediaItem(i, updatedItem)
            }
        }
    }

    private fun createMediaItem(item: PlaybackItem): MediaItem {
        return MediaItem.Builder()
            .setUri(item.url)
            .setMediaId(item.id)
            .setMediaMetadata(
                androidx.media3.common.MediaMetadata.Builder()
                    .setTitle(item.title)
                    .setArtist(if (item is PlaybackItem.SurahItem) item.reciterName else getString(R.string.live_radio))
                    .setArtworkUri(android.net.Uri.parse("android.resource://$packageName/${R.drawable.ic_splash_logo}"))
                    .build()
            )
            .setTag(item)
            .build()
    }

    fun setPlaylist(list: List<PlaybackItem>, startIndex: Int = 0) {
        playlist = list
        currentIndex = startIndex

        val mediaItems = list.map { createMediaItem(it) }

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