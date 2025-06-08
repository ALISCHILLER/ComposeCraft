package com.msa.composecraft.component.customLoading

import android.os.Build
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.sin
import kotlin.random.Random

// ✅ Particle باید public باشد تا در فایل قابل دسترسی باشد
data class ParticleNeonWave(
    val x: Float,
    val y: Float,
    val radius: Float,
    val speedX: Float,
    val speedY: Float,
    val color: Color
)

@Composable
fun QuantumNeonWaveLoading(
    modifier: Modifier = Modifier,
    width: Float = 400f,
    height: Float = 32f,
    waveAmplitude: Float = 20f,
    waveLength: Float = 120f,
    baseColor: Color = Color(0xFF121212),
    neonColors: List<Color> = listOf(
        Color(0xFF00FFFF),
        Color(0xFF8A2BE2),
        Color(0xFF00FF94),
        Color(0xFFFF00FF),
        Color(0xFF00CFFF)
    ),
    particleCount: Int = 40,
    particleMaxRadius: Float = 6f,
    glowPulseSpeed: Int = 2000
) {
    val infiniteTransition = rememberInfiniteTransition()

    // 🔁 انیمیشن چرخشی برای موج
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * PI.toFloat()),
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing)
        )
    )

    // 💫 درخشش مرکزی (Glow Pulse)
    val glowPulse by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(glowPulseSpeed, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    // 🌀 تولید ذرات نئونی
    val particles = remember(width, height, particleCount, particleMaxRadius, neonColors) {
        List(particleCount) {
            ParticleNeonWave(
                x = Random.nextFloat() * width,
                y = Random.nextFloat() * height,
                radius = Random.nextFloat() * (particleMaxRadius - 2f) + 2f,
                speedX = (Random.nextFloat() - 0.5f) * 1.2f,
                speedY = (Random.nextFloat() - 0.5f) * 0.7f,
                color = neonColors.random()
            )
        }.toMutableList()
    }

    // 🔄 به‌روزرسانی موقعیت ذرات
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
            .height((height + waveAmplitude + 8).dp)
            .width(width.dp)
    ) {
        // 🎨 Canvas اصلی لودینگ
        Canvas(modifier = Modifier.fillMaxSize()) {
            val barWidth = width
            val barHeight = height
            val centerY = size.height / 2
            val cornerRadius = barHeight / 2
            val startX = (size.width - barWidth) / 2f

            // 🔷 بک‌گراند مخملی
            drawRoundRect(
                color = baseColor,
                size = Size(barWidth, barHeight),
                cornerRadius = CornerRadius(cornerRadius, cornerRadius),
                topLeft = Offset(startX, centerY - barHeight / 2)
            )

            // 🌊 موج‌های چندلایه با گرادیانت
            neonColors.forEachIndexed { index, color ->
                val phaseOffset = phase + index * (PI.toFloat() / 3f)
                val amplitudeMultiplier = 1f - index * 0.13f
                val alphaBase = 0.8f - index * 0.1f

                val path = Path().apply {
                    moveTo(startX, centerY)
                    var x = 0f
                    while (x <= barWidth) {
                        val y = centerY + sin((x / waveLength) * 2 * PI.toFloat() + phaseOffset) * waveAmplitude * amplitudeMultiplier
                        lineTo(startX + x, y)
                        x += 0.8f
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

            // 💡 درخشش خط اصلی
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

            // 🌌 ذرات نئونی با درخشش
            updatedParticles.forEach { p ->
                val particleX = startX + p.x
                val particleY = centerY - barHeight / 2 + p.y

                drawCircle(
                    color = p.color.copy(alpha = 0.9f * glowPulse),
                    radius = p.radius,
                    center = Offset(particleX, particleY),
                    style = Fill,
                    blendMode = BlendMode.Screen
                )

                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            p.color.copy(alpha = 0.6f * glowPulse),
                            Color.Transparent
                        ),
                        center = Offset(particleX, particleY),
                        radius = p.radius * 7
                    ),
                    radius = p.radius * 7,
                    center = Offset(particleX, particleY),
                    blendMode = BlendMode.Plus
                )
            }
        }

        // 🌫️ Blur Effect (فقط API Level 26+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .drawWithContent {
                        drawContent()
                        drawRect(
                            brush = Brush.linearGradient(
                                colors = neonColors.map { it.copy(alpha = 0.3f) },
                                start = Offset.Zero,
                                end = Offset.Infinite
                            ),
                            topLeft = Offset.Zero,
                            size = size,
                            alpha = 0.6f * glowPulse
                        )
                    }
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun Preview_QuantumNeonWaveLoading() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        QuantumNeonWaveLoading()
    }
}