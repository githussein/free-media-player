package com.example.quranoffline.ui.Radio

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quranoffline.R
import com.example.quranoffline.media.MediaViewModel
import com.example.quranoffline.ui.components.ComponentLoadingState
import com.example.quranoffline.ui.components.ComponentRadioPoster

@Composable
fun RadioScreen(
    modifier: Modifier,
    mediaViewModel: MediaViewModel,
    radioViewModel: RadioViewModel = hiltViewModel()
) {
    val resultState by radioViewModel.resultState.collectAsState()

    LaunchedEffect(radioViewModel) {
        radioViewModel.fetchRadioStations()
    }

    when (resultState) {
        RadiosResultState.Loading -> ComponentLoadingState()

        is RadiosResultState.Success -> {
            val radioStations = (resultState as RadiosResultState.Success).response.radios
            
            Column(modifier = modifier.fillMaxSize()) {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = stringResource(R.string.radios),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(radioStations) { radio ->
                        ComponentRadioPoster(
                            modifier = Modifier.clickable { mediaViewModel.playRadio(radio) },
                            stationName = radio.name,
                            isHome = false
                        )
                    }
                }
            }
        }

        is RadiosResultState.Failure -> Text(
            modifier = Modifier.padding(16.dp),
            text = stringResource(R.string.error),
            color = MaterialTheme.colorScheme.error
        )
    }
}