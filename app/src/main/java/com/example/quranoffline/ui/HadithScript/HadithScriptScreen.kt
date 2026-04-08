package com.example.quranoffline.ui.HadithScript

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quranoffline.R
import com.example.quranoffline.data.Hadith
import com.example.quranoffline.ui.components.ComponentLoadingState
import com.example.quranoffline.ui.theme.uthmanicFontFamily

@Composable             
fun HadithScriptScreen(
    modifier: Modifier,
    bookSlug: String,
    chapterId: String,
    viewModel: BookViewModel = hiltViewModel()
) {
    LaunchedEffect(bookSlug, chapterId) {
        viewModel.fetchHadiths(bookSlug, chapterId)
        if (viewModel.book.value == null) {
            viewModel.fetchBookById(bookSlug)
        }
    }

    val resultState by viewModel.resultState.collectAsState()

    when (resultState) {
        BookResultState.Loading -> ComponentLoadingState()

        is BookResultState.HadithSuccess -> {
            val response = (resultState as BookResultState.HadithSuccess).response
            val bookData by viewModel.book.collectAsState()
            val currentChapter = bookData?.chapters?.find { it.chapterNumber == chapterId }
            
            LazyColumn(
                modifier = modifier.fillMaxSize()
            ) {
                item {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = com.example.quranoffline.util.HadithLocalizationHelper.getLocalizedBookName(bookSlug, bookSlug),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        
                        currentChapter?.let { chapter ->
                            val chapterName = if (com.example.quranoffline.util.LocaleHelper.getApiLanguage() == "ar") 
                                chapter.chapterArabic else chapter.chapterEnglish
                            
                            Text(
                                text = chapterName,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(top = 4.dp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                items(response.hadiths.data, key = { it.id }) { hadith ->
                    HadithItem(hadith)
                }
            }
        }

        is BookResultState.Failure -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(stringResource(R.string.error_loading_data))
            }
        }
        else -> {}
    }
}

@Composable
fun HadithItem(hadith: Hadith) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "${stringResource(R.string.hadith)} ${hadith.hadithNumber}",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )
        
        hadith.hadithArabic?.let {
            Text(
                text = it,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                style = TextStyle(
                    fontFamily = uthmanicFontFamily,
                    fontSize = 22.sp,
                    textAlign = TextAlign.Right
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        hadith.englishNarrator?.let {
            Text(
                text = it,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                style = MaterialTheme.typography.bodySmall.copy(textAlign = TextAlign.Left),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary
            )
        }

        hadith.hadithEnglish?.let {
            Text(
                text = it,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                style = MaterialTheme.typography.bodyMedium.copy(textAlign = TextAlign.Left),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)
    }
}
