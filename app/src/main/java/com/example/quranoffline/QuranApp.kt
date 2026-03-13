package com.example.quranoffline

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.quranoffline.media.MediaViewModel

@Composable
fun QuranApp() {
    val navController = rememberNavController()
    val mediaViewModel: MediaViewModel = hiltViewModel()
    val mediaState by mediaViewModel.mediaState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            containerColor = androidx.compose.material3.MaterialTheme.colorScheme.background
        ) { innerPadding ->
            AppNavHost(
                navController = navController,
                innerPadding = innerPadding,
                mediaViewModel = mediaViewModel
            )
        }

        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
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
                    onSeek = { mediaViewModel.seekTo(it) }
                )
            }
        }
    }
}