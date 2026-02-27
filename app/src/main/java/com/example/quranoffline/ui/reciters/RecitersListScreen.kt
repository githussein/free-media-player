package com.example.quranoffline.ui.reciters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.quranoffline.Reciter
import com.example.quranoffline.ui.components.ComposeReciterItem

@Composable
fun ReciterListScreen(
    modifier: Modifier,
    navController: NavController,
    reciterViewModel: ReciterViewModel = hiltViewModel()
) {
    val resultState by reciterViewModel.resultState.collectAsState()

    LaunchedEffect(reciterViewModel) {
        reciterViewModel.fetchReciters()
    }


    when (resultState) {
        is RecitationsResultState.Loading -> {
            Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is RecitationsResultState.Failure -> {
            val message = (resultState as RecitationsResultState.Failure).e.message
            Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = message ?: "Error fetching reciters")
            }
        }

        is RecitationsResultState.Success -> {
            val reciters = (resultState as RecitationsResultState.Success).response.reciters
            LazyColumn(
                modifier = modifier.fillMaxSize()
            ) {
                item {
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = "The Holy Quran",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(reciters) { reciter ->
                    ComposeReciterItem(reciter) {
                        navController.navigate(Reciter(reciter.id.toString()))
                    }
                }
            }
        }
    }
}
