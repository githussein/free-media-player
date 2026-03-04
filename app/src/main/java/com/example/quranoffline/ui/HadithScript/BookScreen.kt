package com.example.quranoffline.ui.HadithScript

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.quranoffline.BookChapters
import com.example.quranoffline.R
import com.example.quranoffline.ui.components.ComponentBookItem
import com.example.quranoffline.ui.components.ComponentLoadingState

@Composable
fun BookScreen(
    modifier: Modifier,
    navController: NavController,
    viewModel: BookViewModel = hiltViewModel()
) {
    val resultState by viewModel.resultState.collectAsState()

    when (resultState) {
        BookResultState.Loading -> ComponentLoadingState()

        is BookResultState.Success -> LazyColumn(
            modifier = modifier.fillMaxSize()
        ) {
            (resultState as BookResultState.Success).response.books.forEach { book ->
                item {
                    ComponentBookItem(book = book) {
                        navController.navigate(BookChapters(book.bookSlug))
                    }
                }
            }
        }

        is BookResultState.BookSuccess -> {}

        is BookResultState.Failure -> Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(stringResource(R.string.error_loading_data))
        }
    }
}