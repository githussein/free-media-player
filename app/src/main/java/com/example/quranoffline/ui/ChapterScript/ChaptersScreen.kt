package com.example.quranoffline.ui.ChapterScript

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.quranoffline.ChapterScript
import com.example.quranoffline.R
import com.example.quranoffline.ui.components.ComponentLoadingState
import com.example.quranoffline.ui.components.TranscriptChapterItem

@Composable
fun ChaptersScreen(
    modifier: Modifier,
    navController: NavController,
    viewModel: ChaptersViewModel = hiltViewModel()
) {
    val resultState by viewModel.resultState.collectAsState()

    when (resultState) {
        ChaptersResultState.Loading -> ComponentLoadingState()

        is ChaptersResultState.Success -> LazyColumn(
            modifier = modifier.fillMaxSize()
        ) {
            item {
                Text(
                    modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
                    text = stringResource(R.string.the_holy_quran),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            item {
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    text = stringResource(R.string.quran_script_subtitle),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            (resultState as ChaptersResultState.Success).response.chaptersList.forEachIndexed { index, chapter ->
                item {
                    TranscriptChapterItem(
                        index = (index + 1).toString(),
                        chapter = chapter
                    ) {
                        navController.navigate(ChapterScript((index + 1).toString()))
                    }
                }
            }
        }

        is ChaptersResultState.ScriptSuccess -> {}

        is ChaptersResultState.Failure -> Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(stringResource(R.string.error_loading_data))
        }
    }

}
