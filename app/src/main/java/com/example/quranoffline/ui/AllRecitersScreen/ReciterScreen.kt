package com.example.quranoffline.ui.AllRecitersScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quranoffline.MediaControllerViewModel
import com.example.quranoffline.data.Reciter
import com.example.quranoffline.data.Surah

@Composable
fun ReciterScreen(
    modifier: Modifier,
    reciterId: String,
    showMediaPlayer: () -> Unit,
    viewModel: ReciterViewModel = hiltViewModel()
) {

    LaunchedEffect(reciterId) {
        viewModel.fetchReciterById(reciterId)
        viewModel.fetchSurahList()
    }
    val reciter = viewModel.selectedReciter.value
    val surahList by viewModel.surahList.collectAsState()

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
            item { ReciterDropdownMenu(reciter = reciter) }
        }

        surahList.forEach {
            item {
                ComposeSurahItem(
                    it.surah,
                    it.server.orEmpty(),
                    showMediaPlayer = showMediaPlayer
                )
            }
        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReciterDropdownMenu(
    reciter: Reciter,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val moshafList = reciter.moshaf.map { it.name.substringBefore("-") }
    var selectedMoshaf by remember { mutableStateOf<String?>(null) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        TextField(
            value = selectedMoshaf ?: "Select a Moshaf",
            onValueChange = {}, // todo update available SurahList
            readOnly = true,

            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "arrow down icon"
                )
            },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .menuAnchor()
                .clickable { expanded = true }
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            moshafList.forEach { moshafName ->
                DropdownMenuItem(
                    text = { Text(text = moshafName) },
                    onClick = {
                        selectedMoshaf = moshafName
                        expanded = false
                    }
                )
            }
        }
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
private fun ComposeSurahItem(
    surah: Surah?,
    serverUrl: String,
    mediaPlayerViewModel: MediaControllerViewModel = hiltViewModel(),
    showMediaPlayer: () -> Unit
) {
    if (surah == null) return

    val isPlaying by mediaPlayerViewModel.isPlaying.collectAsState()

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    onClick = {
                        if (isPlaying && mediaPlayerViewModel.currentSurahId.value == surah.id) {
                            mediaPlayerViewModel.pauseMedia()
                        } else {
                            mediaPlayerViewModel.playMedia(serverUrl)
                        }
                        mediaPlayerViewModel.currentSurahId.value = surah.id
                        showMediaPlayer()
                    })
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(text = surah.id.toString(), color = Color.Gray)

            Text(
                text = surah.name,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1f),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Icon(
                modifier = Modifier
                    .border(4.dp, Color.Gray, CircleShape)
                    .padding(4.dp),
                imageVector = if (isPlaying && mediaPlayerViewModel.currentSurahId.value == surah.id) Icons.Default.Menu else Icons.Default.PlayArrow,
                contentDescription = if (isPlaying && mediaPlayerViewModel.currentSurahId.value == surah.id) "Pause icon" else "Play icon"
            )
        }

        HorizontalDivider(thickness = 0.3.dp)
    }
}

fun String.formatServerUrl(surahId: Int) = "${this}${String.format("%03d", surahId)}.mp3"