package com.example.quranoffline.ui.Radio

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.foundation.layout.Spacer
import com.example.quranoffline.ui.components.shimmerEffect
import com.example.quranoffline.ui.components.ComponentRadioHeroItem
import com.example.quranoffline.media.PlaybackItem
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.runtime.remember
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment

@Composable
fun RadioScreen(
    modifier: Modifier,
    mediaViewModel: MediaViewModel,
    radioViewModel: RadioViewModel = hiltViewModel()
) {
    val resultState by radioViewModel.resultState.collectAsState()
    val mediaState by mediaViewModel.mediaState.collectAsState()

    LaunchedEffect(Unit) {
        radioViewModel.fetchRadioStations()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when (resultState) {
            RadiosResultState.Loading -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    items(12) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .clip(RoundedCornerShape(24.dp))
                                .shimmerEffect()
                        )
                    }
                }
            }

            is RadiosResultState.Success -> {
                val radioStations = (resultState as RadiosResultState.Success).response.radios

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Column(modifier = Modifier.padding(bottom = 8.dp, top = 16.dp)) {
                            Text(
                                text = stringResource(R.string.radios),
                                style = MaterialTheme.typography.displaySmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = stringResource(R.string.radio_subtitle),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    itemsIndexed(radioStations) { index, radio ->
                        // Premium Entrance Animation - limited to first 10 items to avoid scrolling delay
                        val visibleState = remember { MutableTransitionState(false) }.apply { targetState = true }
                        val animationDelay = if (index < 10) index * 60 else 0
                        
                        val isItemPlaying = mediaState.isPlaying && 
                                           mediaState.currentItem is PlaybackItem.RadioItem && 
                                           (mediaState.currentItem as PlaybackItem.RadioItem).radioId == radio.id

                        AnimatedVisibility(
                            visibleState = visibleState,
                            enter = fadeIn(animationSpec = tween(500, delayMillis = animationDelay)) +
                                    slideInVertically(initialOffsetY = { it / 2 }, animationSpec = tween(500, delayMillis = animationDelay))
                        ) {
                            ComponentRadioHeroItem(
                                stationName = radio.name,
                                isPlaying = isItemPlaying,
                                onClick = { mediaViewModel.playRadio(radio) }
                            )
                        }
                    }
                    
                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }

            is RadiosResultState.Failure -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = stringResource(R.string.error),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}