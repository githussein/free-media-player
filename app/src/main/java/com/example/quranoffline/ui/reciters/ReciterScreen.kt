package com.example.quranoffline.ui.reciters

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quranoffline.data.Moshaf
import com.example.quranoffline.data.Reciter
import com.example.quranoffline.data.Surah
import com.example.quranoffline.media.MediaState
import com.example.quranoffline.media.MediaViewModel
import com.example.quranoffline.media.PlaybackItem
import com.example.quranoffline.ui.theme.MediaControllerColors

@Composable
fun ReciterScreen(
    modifier: Modifier,
    reciterId: String,
    viewModel: ReciterViewModel = hiltViewModel(),
    mediaViewModel: MediaViewModel = hiltViewModel()
) {

    LaunchedEffect(reciterId) {
        viewModel.fetchReciterById(reciterId)
        viewModel.fetchSurahList()
    }

    val reciter = viewModel.selectedReciter.value
    val selectedMoshaf by viewModel.selectedMoshaf.collectAsState()
    val surahList by viewModel.surahList.collectAsState()
    val mediaState by mediaViewModel.mediaState.collectAsState()

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                text = reciter?.name.orEmpty(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        if (reciter?.moshaf != null && reciter.moshaf.size > 1) {
            item {
                ReciterDropdownMenu(
                    reciter = reciter,
                    selectedMoshaf = selectedMoshaf,
                    onMoshafSelected = { viewModel.selectMoshaf(it) }
                )
            }
        }

        surahList.forEach { surahUi ->
            item {
                ComposeSurahItem(
                    surah = surahUi.surah ?: return@item,
                    reciterId = reciter?.id ?: return@item,
                    mediaState = mediaState,
                    onMediaClick = { surah ->
                        mediaViewModel.playSurah(
                            surah = surah,
                            reciterId = reciter.id,
                            reciterName = reciter.name,
                            surahList = surahList
                        )
                    }
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReciterDropdownMenu(
    reciter: Reciter,
    selectedMoshaf: Moshaf?,
    onMoshafSelected: (Moshaf) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier
    ) {
        TextField(
            value = selectedMoshaf?.name ?: "Change Rewayah",
            textStyle = TextStyle(textAlign = TextAlign.Center),
            maxLines = 1,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null
                )
            },
            colors = TextFieldDefaults.colors(
                focusedTextColor = MediaControllerColors.OnSurface,
                unfocusedTextColor = MediaControllerColors.OnSurface,
                focusedContainerColor = MediaControllerColors.Surface,
                unfocusedContainerColor = MediaControllerColors.Surface,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .menuAnchor()
                .padding(horizontal = 16.dp)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(MediaControllerColors.OnSurfaceVariantColor)
                .padding(horizontal = 16.dp)
        ) {
            reciter.moshaf.forEach { moshaf ->
                DropdownMenuItem(
                    modifier = Modifier.clip(RoundedCornerShape(24.dp)),
                    text = {
                        Text(
                            text = moshaf.name,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    onClick = {
                        onMoshafSelected(moshaf)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun ComposeSurahItem(
    surah: Surah,
    reciterId: Int,
    mediaState: MediaState,
    onMediaClick: (Surah) -> Unit
) {
    val isPlaying = mediaState.isPlaying &&
            mediaState.currentItem is PlaybackItem.SurahItem &&
            mediaState.currentItem.surahId == surah.id &&
            mediaState.currentItem.reciterId == reciterId

    val containerColor =
        if (isPlaying)
            MaterialTheme.colorScheme.surfaceVariant
        else
            Color.Transparent

    Surface(
        color = containerColor,
        tonalElevation = if (isPlaying) 2.dp else 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onMediaClick(surah) }
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = surah.id.toString(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = surah.name,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Surface(
                shape = CircleShape,
                color = if (isPlaying)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.size(32.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = if (isPlaying)
                            Icons.Default.Pause
                        else
                            Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = if (isPlaying)
                            MaterialTheme.colorScheme.onPrimary
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

fun String.formatServerUrl(surahId: Int) = "${this}${String.format("%03d", surahId)}.mp3"