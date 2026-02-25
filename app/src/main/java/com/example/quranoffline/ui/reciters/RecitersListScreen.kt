package com.example.quranoffline.ui.reciters

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.quranoffline.ui.components.ComponentLoadingState
import com.example.quranoffline.ui.components.ComposeReciterItem

@Composable
fun AllRecitersScreen(
    modifier: Modifier,
    navController: NavController,
    viewModel: ReciterViewModel = hiltViewModel()
) {
    val resultState by viewModel.resultState.collectAsState()

    when (resultState) {
        RecitationsResultState.Idle -> Text("idle")

        RecitationsResultState.Loading -> ComponentLoadingState()

        is RecitationsResultState.Success -> LazyColumn(
            modifier = modifier.fillMaxSize()
        ) {
            item {
                Text(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp), text = "The Holy Quran", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }

            (resultState as RecitationsResultState.Success).response.reciters.forEach { reciter ->
                item {
                    ComposeReciterItem(reciter) {
                        navController.navigate(Reciter(reciter.id.toString()))
                    }
                }
            }

        }

        is RecitationsResultState.Failure -> Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text("error loading data\nplease try again later")
        }
    }

}
