package com.example.quranoffline

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.quranoffline.ui.AllRecitersScreen.AllRecitersScreen
import com.example.quranoffline.ui.AllRecitersScreen.ReciterScreen
import com.example.quranoffline.ui.ChapterScript.ChapterScriptScreen
import com.example.quranoffline.ui.ChapterScript.ChaptersScreen
import com.example.quranoffline.ui.HadithScript.BookChaptersScreen
import com.example.quranoffline.ui.HadithScript.BookScreen
import com.example.quranoffline.ui.HomeScreen
import com.example.quranoffline.ui.RadioStationsScreen.RadioStationsScreen
import com.example.quranoffline.ui.theme.QuranOfflineTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var isMediaPlayerVisible by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuranOfflineTheme {
                val navController = rememberNavController()


                Scaffold(
                    modifier = Modifier.fillMaxSize(),
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
                    NavHost(
                        navController = navController,
                        startDestination = Home
                    ) {
                        composable<Home> {
                            HomeScreen(
                                modifier = Modifier.systemBarsPadding(),
                                navController = navController
                            )
                        }

                        composable<RadioStations> {
                            RadioStationsScreen(
                                modifier = Modifier.systemBarsPadding()
                            )
                        }

                        composable<AllReciter> {
                            AllRecitersScreen(
                                modifier = Modifier.padding(innerPadding),
                                navController = navController
                            )
                        }

                        composable<Reciter> {
                            val reciterId = it.toRoute<Reciter>().reciterId
                            ReciterScreen(
                                modifier = Modifier.padding(innerPadding),
                                showMediaPlayer = { isMediaPlayerVisible = true },
                                reciterId = reciterId,
                            )
                        }

                        composable<Chapters> {
                            ChaptersScreen(
                                modifier = Modifier.padding(innerPadding),
                                navController = navController
                            )
                        }

                        composable<ChapterScript> {
                            val chapterId = it.toRoute<ChapterScript>().chapterId
                            ChapterScriptScreen(
                                modifier = Modifier.padding(innerPadding),
                                chapterId = chapterId
                            )
                        }

                        composable<Books> {
                            BookScreen(
                                modifier = Modifier.padding(innerPadding),
                                navController = navController
                            )
                        }

                        composable<BookChapters> {
                            BookChaptersScreen(
                                modifier = Modifier.padding(innerPadding),
                                navController = navController,
                                bookId = it.toRoute<BookChapters>().bookId
                            )
                        }
                    }
                }
            }
        }
    }

    fun showMediaPlayer() {
        isMediaPlayerVisible = true
    }
}

