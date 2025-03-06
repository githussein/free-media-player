package com.example.quranoffline.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quranoffline.data.QSurah
import com.example.quranoffline.ui.theme.uthmanicFontFamily

@Composable
fun TranscriptChapterItem(
    index: String,
    chapter: QSurah,
    onChapterClick: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clickable(onClick = { onChapterClick("") }),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(index)
        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(text = chapter.surahNameArabicLong, style = TextStyle(fontFamily = uthmanicFontFamily, fontSize = 20.sp, fontWeight = FontWeight.Bold))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = chapter.surahName, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = chapter.surahNameTranslation, fontSize = 16.sp)
        }
    }

    HorizontalDivider(thickness = 0.5.dp, color = Color.Gray.copy(alpha = 0.3f), modifier = Modifier.padding(horizontal = 16.dp))
}