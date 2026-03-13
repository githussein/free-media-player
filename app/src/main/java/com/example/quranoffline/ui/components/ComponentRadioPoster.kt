package com.example.quranoffline.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Radio
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.quranoffline.R
import kotlin.math.abs

@Composable
fun ComponentRadioPoster(
    modifier: Modifier,
    stationName: String,
    isPlaying: Boolean = false,
    isHome: Boolean = true
) {
    val spiritualGradients = listOf(
        listOf(Color(0xFF9C27B0), Color(0xFF4A0F6F)), // Vibrant Amethyst to Deep Purple
        listOf(Color(0xFFBB86FC), Color(0xFF6200EE)), // Glowing Lavender
        listOf(Color(0xFF7E57C2), Color(0xFF311B92)), // Soft Indigo
        listOf(Color(0xFFD1C4E9), Color(0xFF4A0F6F)), // Mist to Deep
        listOf(Color(0xFFBA68C8), Color(0xFF7B1FA2)), // Orchid to Plum
        listOf(Color(0xFF9575CD), Color(0xFF4527A0)), // Periwinkle to Royal
    )

    val gradientIndex = abs(stationName.hashCode()) % spiritualGradients.size
    val brush = Brush.linearGradient(spiritualGradients[gradientIndex])

    Column(
        modifier = modifier.then(if (isHome) Modifier.width(170.dp) else Modifier),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ElevatedCard(
            modifier = Modifier.aspectRatio(if (isHome) 1.2f else 1f),
            shape = RoundedCornerShape(12.dp)
        ) {
            Box(modifier = Modifier.background(brush)) {
                Icon(
                    imageVector = Icons.Default.Radio,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                        .alpha(0.1f)
                        .graphicsLayer {
                            rotationZ = -15f
                            scaleX = 1.5f
                            scaleY = 1.5f
                        }
                )
                Box(
                    modifier = Modifier
                        .padding(12.dp)
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.3f))
                        .align(Alignment.Center)
                ) {
                    Icon(
                        imageVector = if (isPlaying) androidx.compose.material.icons.Icons.Default.Pause else androidx.compose.material.icons.Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp).align(Alignment.Center)
                    )
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        Text(
            text = stationName.removePrefix("Radio ").removePrefix("إذاعة ").trim(),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}