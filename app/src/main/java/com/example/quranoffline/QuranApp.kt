package com.example.quranoffline

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.quranoffline.media.MediaViewModel

@Composable
fun QuranApp() {

    val navController = rememberNavController()
    val mediaViewModel: MediaViewModel = hiltViewModel()

    val mediaState by mediaViewModel.mediaState.collectAsState()

    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = mediaState.currentItem != null,
                enter = slideInVertically { it },
                exit = slideOutVertically { it }
            ) {
                MediaController(
                    mediaState = mediaState,
                    onPlayPauseClick = {
                        if (mediaState.isPlaying) {
                            mediaViewModel.pause()
                        } else {
                            mediaViewModel.resume()
                        }
                    },
                    onNext = { mediaViewModel.next() },
                    onPrevious = { mediaViewModel.previous() },
                    onClose = { mediaViewModel.stop() }
                )
            }
        }
    ) { innerPadding ->

        AppNavHost(
            navController = navController,
            innerPadding = innerPadding,
            mediaViewModel = mediaViewModel
        )
    }
}