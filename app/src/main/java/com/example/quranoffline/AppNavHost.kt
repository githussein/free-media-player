package com.example.quranoffline

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.quranoffline.media.MediaViewModel
import com.example.quranoffline.ui.ChapterScript.ChapterScriptScreen
import com.example.quranoffline.ui.ChapterScript.ChaptersScreen
import com.example.quranoffline.ui.HadithScript.BookChaptersScreen
import com.example.quranoffline.ui.HadithScript.BookScreen
import com.example.quranoffline.ui.Radio.RadioScreen
import com.example.quranoffline.ui.home.HomeScreen
import com.example.quranoffline.ui.reciters.AllRecitersScreen
import com.example.quranoffline.ui.reciters.ReciterScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    innerPadding: PaddingValues,
    mediaViewModel: MediaViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Home
    ) {
        composable<Home> {
            HomeScreen(
                modifier = Modifier.systemBarsPadding(),
                navController = navController,
                mediaViewModel = mediaViewModel
            )
        }

        composable<Radio> {
            RadioScreen(
                modifier = Modifier.systemBarsPadding(),
                mediaViewModel = mediaViewModel
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
                reciterId = reciterId,
                mediaViewModel = mediaViewModel
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