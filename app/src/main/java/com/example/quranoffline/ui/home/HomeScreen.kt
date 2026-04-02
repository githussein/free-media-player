package com.example.quranoffline.ui.home

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.quranoffline.AllReciter
import com.example.quranoffline.Books
import com.example.quranoffline.Chapters
import com.example.quranoffline.R
import com.example.quranoffline.Radio
import com.example.quranoffline.media.MediaViewModel
import com.example.quranoffline.ui.components.ComponentInfoItem
import com.example.quranoffline.ui.components.ComponentRadioPoster
import com.example.quranoffline.ui.components.ComponentScriptPoster
import com.example.quranoffline.ui.components.ComponentSectionHeader
import com.example.quranoffline.ui.components.ComposeReciterItem
import com.example.quranoffline.ui.components.shimmerEffect


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier,
    navController: NavController,
    homeDataViewModel: HomeDataViewModel = hiltViewModel(),
    mediaViewModel: MediaViewModel
) {
    val verticalScrollState = rememberScrollState()
    var showModal by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val suggestedRadios by homeDataViewModel.suggestedRadios.collectAsState()
    val suggestedReciters by homeDataViewModel.suggestedReciters.collectAsState()
    val isRadiosLoading by homeDataViewModel.isRadiosLoading.collectAsState()
    val isRecitersLoading by homeDataViewModel.isRecitersLoading.collectAsState()

    val currentLanguage = LocalContext.current.resources.configuration.locales[0].language
    LaunchedEffect(currentLanguage, homeDataViewModel) {
        homeDataViewModel.fetchSuggestedRadios()
        homeDataViewModel.fetchSuggestedReciters()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(verticalScrollState)
            .padding(top = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = stringResource(R.string.icon_play),
            tint = Color.Gray,
            modifier = Modifier
                .padding(end = 16.dp)
                .size(24.dp)
                .align(Alignment.End)
                .clickable { showModal = true }
        )

        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))

        ComponentSectionHeader(stringResource(R.string.radios)) {
            navController.navigate(Radio)
        }
        Spacer(modifier = Modifier.height(8.dp))

        val scrollState = rememberScrollState()

        Row(
            modifier = Modifier
                .horizontalScroll(scrollState)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (isRadiosLoading) {
                repeat(3) {
                    Box(
                        modifier = Modifier
                            .size(width = 150.dp, height = 200.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .shimmerEffect()
                    )
                }
            } else {
                val mediaState by mediaViewModel.mediaState.collectAsState()
                suggestedRadios.forEach { radio ->
                    val isItemPlaying = mediaState.isPlaying && 
                                       mediaState.currentItem is com.example.quranoffline.media.PlaybackItem.RadioItem && 
                                       (mediaState.currentItem as com.example.quranoffline.media.PlaybackItem.RadioItem).radioId == radio.id
                    
                    ComponentRadioPoster(
                        modifier = Modifier.clickable { mediaViewModel.playRadio(radio) },
                        stationName = radio.name,
                        isPlaying = isItemPlaying
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(32.dp))


        ComponentSectionHeader(stringResource(R.string.reciters)) {
            navController.navigate(AllReciter)
        }
        Spacer(modifier = Modifier.height(8.dp))

        if (isRecitersLoading) {
            repeat(3) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .fillMaxWidth()
                        .height(60.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .shimmerEffect()
                )
            }
        } else {
            suggestedReciters.forEach { reciter ->
                ComposeReciterItem(reciter) {
                    navController.navigate(com.example.quranoffline.Reciter(reciter.id.toString()))
                }
            }
        }
        Spacer(modifier = Modifier.height(32.dp))


        ComponentSectionHeader(stringResource(R.string.scripts)) {
            navController.navigate(com.example.quranoffline.Chapters)
        }
        Spacer(modifier = Modifier.height(8.dp))
        ComponentScriptPoster(
            modifier = Modifier,
            title = "${stringResource(R.string.quran)}\n",
            description = stringResource(R.string.quran_script_poster),
            painterResourceId = R.drawable.moshaf
        ) {
            navController.navigate(Chapters)
        }
        ComponentScriptPoster(
            modifier = Modifier,
            title = "${stringResource(R.string.hadith)}\n",
            description = stringResource(R.string.hadith_script_poster),
            painterResourceId = R.drawable.hadith
        ) {
            navController.navigate(Books)
        }
    }

    if (showModal) {
        ModalBottomSheet(
            onDismissRequest = { showModal = false }
        ) {
            InfoSheetContent(
                onItemClick = { url ->
                    openUrl(context, url)
                }
            )
        }
    }
}


@Composable
fun InfoSheetContent(onItemClick: (String?) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.info),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        Text(
            text = stringResource(R.string.app),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Column(
            modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                .padding(horizontal = 16.dp)
        ) {
            ComponentInfoItem(
                title = stringResource(R.string.about),
                icon = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                onClick = { onItemClick(null) }
            )
            HorizontalDivider(thickness = 0.3.dp)
            ComponentInfoItem(
                title = stringResource(R.string.contact_us),
                icon = Icons.Default.Email,
                onClick = { onItemClick("https://amrraafat89.wixsite.com/quranvoiceapp/contact") }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.resources),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Column(
            modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                .padding(horizontal = 16.dp)
        ) {
            ComponentInfoItem(
                title = stringResource(R.string.quran_script),
                subtitle = "quranapi.pages.dev",
                icon = Icons.AutoMirrored.Filled.ExitToApp,
                onClick = { onItemClick("https://quranapi.pages.dev") }
            )
            HorizontalDivider(thickness = 0.3.dp)

            ComponentInfoItem(
                title = stringResource(R.string.quran_recitations),
                subtitle = "mp3quran.net",
                icon = Icons.AutoMirrored.Filled.ExitToApp,
                onClick = { onItemClick("https://mp3quran.net/eng/api") }
            )

            HorizontalDivider(thickness = 0.3.dp)
            ComponentInfoItem(
                title = stringResource(R.string.quran_tafseer),
                subtitle = "api.quran-tafseer.co",
                icon = Icons.AutoMirrored.Filled.ExitToApp,
                onClick = { onItemClick("http://api.quran-tafseer.com/en/docs/") }
            )
            HorizontalDivider(thickness = 0.3.dp)

            ComponentInfoItem(
                title = stringResource(R.string.hadith_scripts),
                subtitle = "hadithapi.com",
                icon = Icons.AutoMirrored.Filled.ExitToApp,
                onClick = { onItemClick("https://hadithapi.com") }
            )

        }
    }
}


fun openUrl(context: Context, url: String?) {
    url?.let {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
        context.startActivity(intent)
    }
}
