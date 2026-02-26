package com.example.quranoffline.ui.home

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.quranoffline.AllReciter
import com.example.quranoffline.Books
import com.example.quranoffline.Chapters
import com.example.quranoffline.R
import com.example.quranoffline.Radio
import com.example.quranoffline.data.Moshaf
import com.example.quranoffline.data.Reciter
import com.example.quranoffline.media.MediaViewModel
import com.example.quranoffline.ui.components.ComponentInfoItem
import com.example.quranoffline.ui.components.ComponentRadioPoster
import com.example.quranoffline.ui.components.ComponentScriptPoster
import com.example.quranoffline.ui.components.ComponentSectionHeader
import com.example.quranoffline.ui.components.ComposeReciterItem


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier,
    navController: NavController,
    mediaViewModel: MediaViewModel
) {
    val verticalScrollState = rememberScrollState()
    var showModal by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val homeViewModel: HomeViewModel = hiltViewModel()
    val radios by homeViewModel.suggestedRadios.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(verticalScrollState)
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = "play icon",
            tint = Color.Gray,
            modifier = modifier
                .padding(end = 16.dp)
                .size(24.dp)
                .align(Alignment.End)
                .clickable { showModal = true }
        )

        Text("Tilawah App", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(24.dp))

        ComponentSectionHeader("Radios", "more>") {
            navController.navigate(Radio)
        }
        Spacer(modifier = Modifier.height(8.dp))

        val images = listOf(R.drawable.masjid1, R.drawable.masjid2, R.drawable.masjid3)
        val scrollState = rememberScrollState()

        Row(
            modifier = Modifier
                .horizontalScroll(scrollState)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            radios.forEachIndexed { index, radio ->
                ComponentRadioPoster(
                    modifier = modifier
                        .fillMaxSize()
                        .clickable { mediaViewModel.playRadio(radio) },
                    stationName = radio.name,
                    imageId = images.getOrElse(index) { R.drawable.masjid1 }
                )
            }
        }
        Spacer(modifier = modifier.height(32.dp))


        ComponentSectionHeader("Reciters", "more") {
            navController.navigate(AllReciter)
        }
        Spacer(modifier = Modifier.height(8.dp))

        ComposeReciterItem(
            Reciter(
                id = 1,
                name = "Mahmoud Al-Hussary",
                letter = "M",
                moshaf = Moshaf.generateRandomMoshafList()
            )
        ) {
            navController.navigate(com.example.quranoffline.Reciter("118"))
        }
        ComposeReciterItem(
            Reciter(
                id = 1,
                name = "Mishary Alafasi",
                letter = "M",
                moshaf = Moshaf.generateRandomMoshafList()
            )
        ) {
            navController.navigate(com.example.quranoffline.Reciter("123"))
        }
        ComposeReciterItem(
            Reciter(
                id = 1,
                name = "Mohamemed Al-Minshawi",
                letter = "M",
                moshaf = Moshaf.generateRandomMoshafList()
            )
        ) {
            navController.navigate(com.example.quranoffline.Reciter("112"))
        }
        Spacer(modifier = modifier.height(32.dp))


        ComponentSectionHeader("Quran & Hadith Scripts", null) {}
        Spacer(modifier = Modifier.height(8.dp))
        ComponentScriptPoster(
            modifier = modifier,
            title = "Quran\n",
            description = "with Arabic script and \nEnglish translation",
            painterResourceId = R.drawable.moshaf
        ) {
            navController.navigate(Chapters)
        }
        ComponentScriptPoster(
            modifier = modifier,
            title = "Hadith\n",
            description = "with Arabic script and \nEnglish translation",
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
            "Information",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        Text("App", color = Color.Gray, modifier = Modifier.padding(horizontal = 16.dp))
        Spacer(modifier = Modifier.height(8.dp))
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color.Gray.copy(alpha = 0.3f))
                .padding(horizontal = 16.dp)
        ) {
            ComponentInfoItem(
                title = "About",
                icon = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                onClick = { onItemClick(null) }
            )
            HorizontalDivider(thickness = 0.3.dp)
            ComponentInfoItem(
                title = "Contact us",
                icon = Icons.Default.Email,
                onClick = { onItemClick("https://amrraafat89.wixsite.com/quranvoiceapp/contact") }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Resources", color = Color.Gray, modifier = Modifier.padding(horizontal = 16.dp))
        Spacer(modifier = Modifier.height(8.dp))
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color.Gray.copy(alpha = 0.3f))
                .padding(horizontal = 16.dp)
        ) {
            ComponentInfoItem(
                title = "Quran API",
                subtitle = "quranapi.pages.dev",
                icon = Icons.AutoMirrored.Filled.ExitToApp,
                onClick = { onItemClick("https://quranapi.pages.dev") }
            )
            HorizontalDivider(thickness = 0.3.dp)
            ComponentInfoItem(
                title = "MP3 Quran",
                subtitle = "mp3quran.net",
                icon = Icons.AutoMirrored.Filled.ExitToApp,
                onClick = { onItemClick("https://mp3quran.net/eng/api") }
            )
            HorizontalDivider(thickness = 0.3.dp)
            ComponentInfoItem(
                title = "Quran Tafseer",
                subtitle = "api.quran-tafseer.co",
                icon = Icons.AutoMirrored.Filled.ExitToApp,
                onClick = { onItemClick("http://api.quran-tafseer.com/en/docs/") }
            )
            HorizontalDivider(thickness = 0.3.dp)
            ComponentInfoItem(
                title = "Hadith API",
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

