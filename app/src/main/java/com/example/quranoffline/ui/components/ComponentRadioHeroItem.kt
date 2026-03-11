package com.example.quranoffline.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Radio
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.abs

@Composable
fun ComponentRadioHeroItem(
    stationName: String,
    isPlaying: Boolean = false,
    onClick: () -> Unit
) {
    val premiumGradients = listOf(
        listOf(Color(0xFF0F2027), Color(0xFF203A43), Color(0xFF2C5364)), // Deep Blue Mesh
        listOf(Color(0xFF134E5E), Color(0xFF71B280)), // Emerald Sea
        listOf(Color(0xFF42275A), Color(0xFF734B6D)), // Plum
        listOf(Color(0xFF2C3E50), Color(0xFF4CA1AF)), // Modern Slate
        listOf(Color(0xFF1D2671), Color(0xFFC33764)), // Celestial
        listOf(Color(0xFF5614B0), Color(0xFFDBD65C)), // Cosmic
    )

    val gradientIndex = abs(stationName.hashCode()) % premiumGradients.size
    val colors = premiumGradients[gradientIndex]
    val brush = Brush.linearGradient(colors)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = colors.last().copy(alpha = 0.5f),
                spotColor = colors.last().copy(alpha = 0.5f)
            )
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush)
        ) {
            // Background Watermark Icon
            Icon(
                imageVector = Icons.Default.Radio,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(110.dp)
                    .offset(x = 20.dp, y = 20.dp)
                    .alpha(0.1f)
                    .graphicsLayer {
                        rotationZ = -20f
                    }
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top Row: Live Indicator
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(Color.Red)
                    )
                    Text(
                        text = "LIVE",
                        color = Color.White.copy(alpha = 0.8f),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }

                // Middle/Bottom Part
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stationName.removePrefix("Radio ").trim(),
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    // Compact Play Button
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}
