package com.example.quranoffline

import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MediaControllerViewModel @Inject constructor() : ViewModel() {
    private val mediaPlayer = MediaPlayer()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying

    val currentSurahId = MutableStateFlow<Int?>(null)

    fun playMedia(url: String) {

        try {
            mediaPlayer.reset()
            _isPlaying.value = false
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                it.start()
                _isPlaying.value = true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    fun pauseMedia() {
        if (_isPlaying.value) {
            mediaPlayer.pause()
            _isPlaying.value = false
        }
    }

    fun stopMedia() {
        if (_isPlaying.value) {
            mediaPlayer.stop()
            _isPlaying.value = false
        }
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer.release()
    }
}