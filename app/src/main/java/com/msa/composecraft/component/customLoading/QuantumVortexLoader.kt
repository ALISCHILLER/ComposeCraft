package com.msa.composecraft.component.customLoading

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random
import androidx.compose.ui.graphics.lerp // ✅ امپورت صحیح

@Composable
fun QuantumVortexLoader(
    modifier: Modifier = Modifier,
    size: Float = 300f,
    particleCount: Int = 50,
    baseColor: Color = Color(0xFF0A0A1E),
    neonPalette: List<Color> = listOf(
        Color(0xFF00FFFF),
        Color(0xFF8A2BE2),
        Color(0xFF00FF94),
        Color(0xFFFF00FF),
        Color(0xFF00CFFF)
    )
) {
    val infiniteTransition = rememberInfiniteTransition()

    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(12000, easing = LinearEasing))
    )

    val colorPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(10000, easing = LinearEasing))
    )

    val center = Offset(size / 2, size / 2)

    val particles = remember {
        List(particleCount) {
            ParticleVortexLoader(
                angle = Random.nextFloat() * 360f,
                distance = Random.nextFloat() * size * 0.4f + size * 0.1f,
                speed = Random.nextFloat() * 0.5f + 0.2f,
                colorIndex = it % neonPalette.size,
                direction = if (Random.nextBoolean()) 1f else -1f,
                pulse = Random.nextFloat() * 100f,
                size = Random.nextFloat() * 6f + 2f // ✅ اندازه به عنوان پارامتر
            )
        }
    }

    Box(
        modifier = modifier
            .size(with(LocalDensity.current) { size.toDp() })
            .background(baseColor),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // 🌀 گرداب مرکزی
            val vortexRadius = size / 2
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.7f),
                        Color.Transparent
                    ),
                    center = center,
                    radius = vortexRadius
                ),
                radius = vortexRadius,
                center = center,
                blendMode = BlendMode.Plus
            )

            // 🌌 ذرات متحرک
            particles.forEach { p ->
                val angle = (p.angle + time * p.speed * p.direction) % 360f
                val distance = p.distance + sin((time + p.pulse) * PI.toFloat() / 180f) * 10f
                val rad = Math.toRadians(angle.toDouble()).toFloat() // ✅ استفاده صحیح از toRadians

                val x = center.x + distance * cos(rad)
                val y = center.y + distance * sin(rad)
                val pos = Offset(x, y)

                val baseIndex = (colorPhase * neonPalette.size).toInt().coerceIn(0 until neonPalette.size)
                val nextIndex = (baseIndex + 1) % neonPalette.size
                val lerpFactor = ((colorPhase * neonPalette.size) % 1f)
                val particleColor = lerp(neonPalette[baseIndex], neonPalette[nextIndex], lerpFactor) // ✅ استفاده صحیح از lerp

                // 🌠 Glow حول ذرات
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            particleColor.copy(alpha = 0.7f),
                            Color.Transparent
                        ),
                        center = pos,
                        radius = p.size * 3
                    ),
                    radius = p.size * 3,
                    center = pos,
                    blendMode = BlendMode.Plus
                )

                // 🧩 ذره اصلی
                drawCircle(
                    color = particleColor,
                    radius = p.size.coerceAtLeast(1f),
                    center = pos,
                    style = Fill
                )
            }

            // ⚡️ نور خطی مرکزی
            val path = Path().apply {
                moveTo(center.x, center.y)
                lineTo(center.x + 30f, center.y + 30f)
                lineTo(center.x + 60f, center.y)
                lineTo(center.x + 90f, center.y + 30f)
                lineTo(center.x + 120f, center.y)
                close()
            }

            drawPath(
                path = path,
                brush = Brush.linearGradient(
                    colors = listOf(Color.White, Color.Magenta, Color.Cyan),
                    start = Offset(center.x, center.y),
                    end = Offset(center.x + 120f, center.y)
                ),
                style = Stroke(width = 2f),
                alpha = 0.6f
            )
        }
    }
}

private data class ParticleVortexLoader(
    val angle: Float,
    val distance: Float,
    val speed: Float,
    val colorIndex: Int,
    val direction: Float,
    val pulse: Float,
    val size: Float
)
@Preview(showBackground = true)
@Composable
fun PreviewQuantumVortexLoader() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        QuantumVortexLoader()
    }
}


