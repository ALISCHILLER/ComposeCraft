package com.msa.composecraft.component.customLoading

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.sin

@Composable
fun UltraNeonWaveLoading(
    modifier: Modifier = Modifier,
    width: Float = 320f,
    height: Float = 16f,
    waveAmplitude: Float = 10f,
    waveLength: Float = 80f,
    speed: Float = 1.8f,
    baseColor: Color = Color(0xFF121212),
    neonColors: List<Color> = listOf(
        Color(0xFF00FFFF),
        Color(0xFF8A2BE2),
        Color(0xFF00FF94),
        Color(0xFF00CFFF)
    )
) {
    val infiniteTransition = rememberInfiniteTransition()

    // انیمیشن فاز موج (حرکت موج)
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = (3000 / speed).toInt(),
                easing = LinearEasing
            )
        )
    )

    // انیمیشن پالس درخشش (Glow)
    val glowPulse by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = modifier
            .padding(horizontal = 24.dp, vertical = 12.dp)
            .height(with(LocalDensity.current) { (height + waveAmplitude).toDp() })
            .width(with(LocalDensity.current) { width.toDp() }),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val barWidth = width
            val barHeight = height
            val centerY = size.height / 2
            val cornerRadius = barHeight / 2

            // بک‌گراند تیره با گوشه‌های گرد
            drawRoundRect(
                color = baseColor,
                size = Size(barWidth, barHeight),
                cornerRadius = CornerRadius(cornerRadius, cornerRadius),
                topLeft = Offset((size.width - barWidth) / 2f, centerY - barHeight / 2)
            )

            // رسم چند موج نئون روی خط
            val startX = (size.width - barWidth) / 2f

            for ((index, neonColor) in neonColors.withIndex()) {
                val offsetPhase = phase + index * PI.toFloat() / 2f

                val path = Path().apply {
                    moveTo(startX, centerY)
                    var x = 0f
                    while (x <= barWidth) {
                        val y = centerY + sin((x / waveLength) * 2 * PI + offsetPhase) * waveAmplitude * (1 - index * 0.2f)
                        lineTo(startX + x, y.toFloat())
                        x += 1f
                    }
                    lineTo(startX + barWidth, centerY + barHeight / 2 + 10)
                    lineTo(startX, centerY + barHeight / 2 + 10)
                    close()
                }

                // Glow و رنگ نئون با شفافیت و پالس درخشش
                drawPath(
                    path = path,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            neonColor.copy(alpha = 0.9f * glowPulse),
                            neonColor.copy(alpha = 0.3f * glowPulse),
                            Color.Transparent
                        ),
                        startY = centerY - waveAmplitude * 2,
                        endY = centerY + waveAmplitude * 3
                    ),
                    style = Fill,
                    blendMode = BlendMode.Screen
                )
            }

            // درخشش مرکزی بیشتر (Glow تمرکز بیشتر روی وسط)
            drawRoundRect(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color.Transparent,
                        neonColors[0].copy(alpha = 0.7f * glowPulse),
                        Color.Transparent
                    )
                ),
                size = Size(barWidth, barHeight),
                topLeft = Offset(startX, centerY - barHeight / 2),
                cornerRadius = CornerRadius(cornerRadius, cornerRadius),
                blendMode = BlendMode.Plus
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun PreviewUltraNeonWaveLoading() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        UltraNeonWaveLoading()
    }
}
