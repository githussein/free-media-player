package com.example.quranoffline.ui.HadithScript

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

    androidx.compose.runtime.LaunchedEffect(Unit) {
        viewModel.fetchBooks()
    }

    val state = resultState
    when (state) {
        BookResultState.Loading -> ComponentLoadingState()

        is BookResultState.Success -> LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 100.dp)
        ) {
            item {
                Text(
                    modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
                    text = stringResource(R.string.hadith),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            item {
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    text = stringResource(R.string.hadith_script_subtitle),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            state.response.books.forEach { book ->
                item {
                    ComponentBookItem(book = book) {
                        navController.navigate(com.example.quranoffline.BookChapters(book.bookSlug))
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

        else -> {}
    }
}