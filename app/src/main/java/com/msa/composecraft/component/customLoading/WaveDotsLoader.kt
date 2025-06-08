package com.msa.composecraft.component.customLoading

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun WaveDotsGlowLoader(
    modifier: Modifier = Modifier,
    dotSize: Dp = 16.dp,
    spaceBetween: Dp = 12.dp,
    durationMillis: Int = 800,
    glowColor: Color = Color(0xFF00FFFF),
    colors: List<Color> = listOf(Color(0xFF00BCD4), Color(0xFF8BC34A))
) {
    val delays = listOf(0, 100, 200)

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spaceBetween)
    ) {
        delays.forEachIndexed { index, delay ->
            val infiniteTransition = rememberInfiniteTransition(label = "dot$index")

            val offsetY by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = -18f,
                animationSpec = infiniteRepeatable(
                    tween(durationMillis, delayMillis = delay, easing = FastOutSlowInEasing),
                    RepeatMode.Reverse
                )
            )

            val alpha by infiniteTransition.animateFloat(
                initialValue = 0.4f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    tween(durationMillis, delayMillis = delay, easing = LinearEasing),
                    RepeatMode.Reverse
                )
            )

            val brush = Brush.linearGradient(
                colors = colors,
                start = Offset.Zero,
                end = Offset(100f, 100f)
            )

            Box(
                modifier = Modifier
                    .graphicsLayer {
                        translationY = offsetY
                        shadowElevation = 8f
                        shape = CircleShape
                        clip = true
                    }
                    .background(brush, shape = CircleShape)
                    .size(dotSize)
                    .drawBehind {
                        drawCircle(
                            color = glowColor.copy(alpha = alpha),
                            radius = size.minDimension / 1.5f,
                            center = center,
                            blendMode = BlendMode.SrcOver
                        )
                    }
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
fun WaveDotsGlowLoaderPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212)),
        contentAlignment = Alignment.Center
    ) {
        WaveDotsGlowLoader()
    }
}
