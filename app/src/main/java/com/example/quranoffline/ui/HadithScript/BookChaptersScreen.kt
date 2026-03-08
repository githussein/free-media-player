package com.example.quranoffline.ui.HadithScript

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.quranoffline.R
import com.example.quranoffline.ui.components.ComponentBookChapterItem
import com.example.quranoffline.ui.components.ComponentGradientBox
import com.example.quranoffline.ui.components.ComponentLoadingState

@Composable
fun BookChaptersScreen(
    modifier: Modifier,
    bookId: String,
    navController: NavController,
    viewModel: BookViewModel = hiltViewModel()
) {
    LaunchedEffect(bookId) {
        viewModel.fetchBookById(bookId)
        if (viewModel.bookList.value.isEmpty()) {
            viewModel.fetchBooks()
        }
    }

    val selectedBook by viewModel.bookList.collectAsState()
    val bookToDisplay = selectedBook.find { it.bookSlug == bookId }
    val resultState by viewModel.resultState.collectAsState()
    val bookData by viewModel.book.collectAsState()

    val state = resultState
    when (state) {
        BookResultState.Loading -> ComponentLoadingState()

        is BookResultState.Success, is BookResultState.BookSuccess -> LazyColumn(
            modifier = modifier.fillMaxSize()
        ) {
            item {
                ComponentGradientBox(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    height = 150.dp
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = com.example.quranoffline.util.HadithLocalizationHelper.getLocalizedBookName(bookToDisplay?.bookSlug.orEmpty(), bookToDisplay?.bookName.orEmpty()),
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Text(
                            text = com.example.quranoffline.util.HadithLocalizationHelper.getLocalizedWriterName(bookToDisplay?.writerName.orEmpty()),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Text(
                            text = "${bookToDisplay?.chapters_count.orEmpty()} ${stringResource(R.string.chapters)} - ${bookToDisplay?.hadiths_count.orEmpty()} ${stringResource(R.string.hadiths)}",
                            color = Color.White,
                            fontSize = 14.sp
                        )
                    }

                }
            }


            bookData?.chapters?.forEachIndexed { index, chapter ->
                item {
                    ComponentBookChapterItem(index = (index + 1).toString(), chapter = chapter) {
//                            navController.navigate(Hadith(id))
                    }
                }
            }
        }

        is BookResultState.Failure -> Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(stringResource(R.string.error_loading_data))
        }
    }
}