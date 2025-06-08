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
import kotlin.random.Random

@Composable
fun LegendaryNeonLoader(
    modifier: Modifier = Modifier,
    width: Float = 400f,           // بزرگ‌تر شده
    height: Float = 28f,           // بزرگ‌تر شده
    waveAmplitude: Float = 18f,    // موج بلندتر
    waveLength: Float = 110f,      // طول موج بیشتر برای روان‌تر شدن موج
    speed: Float = 1.8f,           // سرعت موج کمی کمتر برای نمای بهتر
    baseColor: Color = Color(0xFF0D0D0D),
    neonColors: List<Color> = listOf(
        Color(0xFF00FFFF),
        Color(0xFF8A2BE2),
        Color(0xFF00FF94),
        Color(0xFFFF00FF),
        Color(0xFF00CFFF)
    ),
    particleCount: Int = 30,       // تعداد ذرات بیشتر برای پرشدن بیشتر فضای
    particleMaxRadius: Float = 5f, // حداکثر سایز ذرات بزرگ‌تر
    glowPulseSpeed: Int = 2200     // سرعت پالس درخشش قابل تنظیم
) {
    val infiniteTransition = rememberInfiniteTransition()

    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween((4000 / speed).toInt(), easing = LinearEasing)
        )
    )

    val glowPulse by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1.4f,
        animationSpec = infiniteRepeatable(
            animation = tween(glowPulseSpeed, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val particles = remember {
        List(particleCount) {
            Particle(
                x = Random.nextFloat() * width,
                y = Random.nextFloat() * height,
                radius = Random.nextFloat() * (particleMaxRadius - 2f) + 2f,
                speedX = (Random.nextFloat() - 0.5f) * 1.2f,
                speedY = (Random.nextFloat() - 0.5f) * 0.7f,
                color = neonColors.random()
            )
        }
    }

    val updatedParticles = remember { particles.toMutableList() }
    LaunchedEffect(phase) {
        for (i in updatedParticles.indices) {
            val p = updatedParticles[i]
            var newX = p.x + p.speedX
            var newY = p.y + p.speedY

            if (newX < 0f) newX = width
            if (newX > width) newX = 0f
            if (newY < 0f) newY = height
            if (newY > height) newY = 0f

            updatedParticles[i] = p.copy(x = newX, y = newY)
        }
    }

    Box(
        modifier = modifier
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .height(with(LocalDensity.current) { (height + waveAmplitude + 8).toDp() })
            .width(with(LocalDensity.current) { width.toDp() }),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val barWidth = width
            val barHeight = height
            val centerY = size.height / 2
            val cornerRadius = barHeight / 2
            val startX = (size.width - barWidth) / 2f

            // بک‌گراند مخملی تیره
            drawRoundRect(
                color = baseColor,
                size = Size(barWidth, barHeight),
                cornerRadius = CornerRadius(cornerRadius, cornerRadius),
                topLeft = Offset(startX, centerY - barHeight / 2)
            )

            // رسم موج‌های چندلایه رنگی با دقت بالاتر و لایه‌های بیشتر
            neonColors.forEachIndexed { index, color ->
                val phaseOffset = phase + index * PI.toFloat() / 3f
                val amplitudeMultiplier = 1f - index * 0.13f
                val alphaBase = 0.8f - index * 0.1f

                val path = Path().apply {
                    moveTo(startX, centerY)
                    var x = 0f
                    while (x <= barWidth) {
                        val y = centerY + sin((x / waveLength) * 2 * PI + phaseOffset) * waveAmplitude * amplitudeMultiplier
                        lineTo(startX + x, y.toFloat())
                        x += 0.8f // دقت بیشتر خط موج
                    }
                    lineTo(startX + barWidth, centerY + barHeight / 2 + 16)
                    lineTo(startX, centerY + barHeight / 2 + 16)
                    close()
                }

                drawPath(
                    path = path,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            color.copy(alpha = alphaBase * glowPulse),
                            color.copy(alpha = 0.3f * glowPulse),
                            Color.Transparent
                        ),
                        startY = centerY - waveAmplitude * 2,
                        endY = centerY + waveAmplitude * 5
                    ),
                    blendMode = BlendMode.Screen
                )
            }

            // Glow رنگی قوی‌تر روی خط اصلی
            drawRoundRect(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color.Transparent,
                        neonColors[0].copy(alpha = 0.85f * glowPulse),
                        Color.Transparent
                    )
                ),
                size = Size(barWidth, barHeight),
                topLeft = Offset(startX, centerY - barHeight / 2),
                cornerRadius = CornerRadius(cornerRadius, cornerRadius),
                blendMode = BlendMode.Plus
            )

            // ذرات نئون بزرگ‌تر و پراکنده با درخشش قوی‌تر
            updatedParticles.forEach { p ->
                val particleX = startX + p.x
                val particleY = centerY - barHeight / 2 + p.y
                drawCircle(
                    color = p.color.copy(alpha = 0.9f * glowPulse),
                    radius = p.radius,
                    center = Offset(particleX, particleY),
                    blendMode = BlendMode.Screen,
                    style = Fill
                )
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(p.color.copy(alpha = 0.6f * glowPulse), Color.Transparent),
                        center = Offset(particleX, particleY),
                        radius = p.radius * 7
                    ),
                    radius = p.radius * 7,
                    center = Offset(particleX, particleY),
                    blendMode = BlendMode.Plus
                )
            }
        }
    }
}

private data class Particle(
    val x: Float,
    val y: Float,
    val radius: Float,
    val speedX: Float,
    val speedY: Float,
    val color: Color
)

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun PreviewLegendaryNeonLoader() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        LegendaryNeonLoader()
    }
}
