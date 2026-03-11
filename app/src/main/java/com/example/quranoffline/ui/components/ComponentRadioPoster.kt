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
    val premiumGradients = listOf(
        listOf(Color(0xFF0F2027), Color(0xFF203A43), Color(0xFF2C5364)), // Deep Blue Mesh
        listOf(Color(0xFF134E5E), Color(0xFF71B280)), // Emerald Sea
        listOf(Color(0xFF42275A), Color(0xFF734B6D)), // Plum
        listOf(Color(0xFF2C3E50), Color(0xFF4CA1AF)), // Modern Slate
        listOf(Color(0xFF1D2671), Color(0xFFC33764)), // Celestial
        listOf(Color(0xFF5614B0), Color(0xFFDBD65C)), // Cosmic
    )

    val gradientIndex = abs(stationName.hashCode()) % premiumGradients.size
    val brush = Brush.linearGradient(premiumGradients[gradientIndex])

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
            text = stationName.removePrefix("Radio ").trim(),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}