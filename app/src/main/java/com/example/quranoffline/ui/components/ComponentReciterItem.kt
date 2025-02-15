package com.example.quranoffline.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quranoffline.Moshaf
import com.example.quranoffline.R
import com.example.quranoffline.Reciter

@Composable
fun ComposeReciterItem(reciter: Reciter, onReciterClick: (String) -> Unit) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clickable(onClick = { onReciterClick(reciter.id.toString()) }),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Image(
            painter = painterResource(id = R.drawable.mic),
            contentDescription = "photo of a mic",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(10.dp))
        )
        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier
                .padding(end = 16.dp)
                .weight(1f)
        ) {
            Text(text = reciter.name, fontSize = 16.sp, fontWeight = FontWeight.Bold)

//            reciter.moshaf.forEach {
//                Text(text = "(${it.surah_total}) ${it.name.substringBefore("-")}", maxLines = 1)
//            }
            Text(
                "${reciter.moshaf.size} Recitation${if (reciter.moshaf.size > 1) "s" else ""}",
                color = Color.Gray
            )
        }

        Icon(
            modifier = Modifier.padding(4.dp),
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "arrow icon"
        )
    }

    HorizontalDivider(thickness = 0.5.dp, color = Color.Gray.copy(alpha = 0.3f), modifier = Modifier.padding(horizontal = 16.dp))
}


@Preview(showBackground = true)
@Composable
fun ReciterItemPreview() {
    ComposeReciterItem(
        Reciter(
            id = 1,
            name = "Mahmoud Al-Hussary",
            letter = "M",
            moshaf = listOf(
                Moshaf(
                    id = 1,
                    name = "Murattal",
                    server = "",
                    surah_total = 114,
                    moshaf_type = 11,
                    surah_list = "1, 2, 3"
                )
            )
        )
    ) {}
}