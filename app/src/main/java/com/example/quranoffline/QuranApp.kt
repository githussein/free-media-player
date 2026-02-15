package com.example.quranoffline

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.rememberNavController

@Composable
fun QuranApp() {
    val navController = rememberNavController()
    var isMediaPlayerVisible by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            if (isMediaPlayerVisible) {
                MediaController(
                    title = "Media Title",
                    description = "Media Description",
                    onPlayPauseClick = { },
                    onClose = { isMediaPlayerVisible = false }
                )
            }
        }
    ) { innerPadding ->
        AppNavHost(
            navController = navController,
            innerPadding = innerPadding,
            showMediaPlayer = { isMediaPlayerVisible = true }
        )
    }
}