package com.example.quranoffline.ui.Radio

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quranoffline.R
import com.example.quranoffline.data.Radio
import com.example.quranoffline.media.MediaViewModel
import com.example.quranoffline.ui.components.ComponentLoadingState
import com.example.quranoffline.ui.components.ComponentRadioHeroItem

@Composable
fun RadioScreen(
    modifier: Modifier,
    radioViewModel: RadioViewModel = hiltViewModel(),
    mediaViewModel: MediaViewModel = hiltViewModel()
) {
    val resultState by radioViewModel.resultState.collectAsState()
    val mediaState by mediaViewModel.mediaState.collectAsState()
    val searchQuery by radioViewModel.searchQuery.collectAsState()
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        radioViewModel.fetchRadioStations()
    }

    when (resultState) {
        RadiosResultState.Loading -> ComponentLoadingState()
        is RadiosResultState.Success -> Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(bottom = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(top = 32.dp),
                text = stringResource(R.string.radios),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                text = stringResource(R.string.radio_subtitle),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = searchQuery,
                onValueChange = { 
                    radioViewModel.onSearchQueryChanged(it)
                    radioViewModel.fetchRadioStations()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text(stringResource(R.string.search_radios)) },
                label = { Text(stringResource(R.string.search_radios)) },
                leadingIcon = { 
                    Icon(
                        Icons.Default.Search, 
                        contentDescription = stringResource(R.string.search_radios),
                        tint = MaterialTheme.colorScheme.primary
                    ) 
                },
                shape = RoundedCornerShape(100),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            val radioStations =
                (resultState as RadiosResultState.Success).response.radios

            RadioHeroStack(
                stations = radioStations,
                mediaState = mediaState,
                onRadioClick = { radio ->
                    mediaViewModel.playRadio(radio)
                }
            )
        }

        is RadiosResultState.Failure -> Text(stringResource(R.string.error))
    }
}

@Composable
fun RadioHeroStack(
    stations: List<Radio>,
    mediaState: com.example.quranoffline.media.MediaState,
    onRadioClick: (Radio) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        stations.forEach { station ->
            val isItemPlaying = mediaState.isPlaying && 
                               mediaState.currentItem is com.example.quranoffline.media.PlaybackItem.RadioItem && 
                               (mediaState.currentItem as com.example.quranoffline.media.PlaybackItem.RadioItem).radioId == station.id

            ComponentRadioHeroItem(
                stationName = station.name,
                isPlaying = isItemPlaying,
                onClick = { onRadioClick(station) }
            )
        }
    }
}