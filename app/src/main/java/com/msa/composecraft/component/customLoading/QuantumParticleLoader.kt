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
import androidx.compose.ui.unit.dp
import kotlin.math.*
import kotlin.random.Random

@Composable
fun QuantumParticleLoader(
    modifier: Modifier = Modifier,
    size: Float = 280f,
    particleCount: Int = 35,
    baseColor: Color = Color(0xFF101020),
    neonPalette: List<Color> = listOf(
        Color(0xFF00FFF7),
        Color(0xFF8E2DE2),
        Color(0xFF00FF94),
        Color(0xFFFF00FF),
        Color(0xFF00CFFF)
    )
) {
    val infiniteTransition = rememberInfiniteTransition()

    // فاز برای چرخه رنگی نرم‌تر و پیوسته‌تر
    val colorPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(16000, easing = LinearEasing))
    )

    // زمان کلی برای انیمیشن‌ها
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(10000, easing = LinearEasing))
    )

    // مرکز کانوَس
    val center = Offset(size / 2, size / 2)

    // ذرات با پارامترهای پیچیده‌تر و کنترل‌شده‌تر
    val particles = remember {
        List(particleCount) { index ->
            QuantumParticle(
                baseAngle = Random.nextFloat() * 360f,
                orbitRadius = Random.nextFloat() * size * 0.4f + size * 0.12f,
                speed = Random.nextFloat() * 0.5f + 0.35f,
                size = Random.nextFloat() * 6f + 4f,
                orbitRatio = 0.5f + Random.nextFloat() * 0.5f,
                baseColorIndex = index % neonPalette.size,
                wavePhase = Random.nextFloat() * 360f,
                waveAmplitude = Random.nextFloat() * 12f + 5f,
                orbitDirection = if (Random.nextBoolean()) 1 else -1
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
            // حلقه‌ی مرکزی با انیمیشن پیچیده‌تر ضخامت و شفافیت
            val centerRadius = size * 0.1f
            val baseStroke = size * 0.015f
            val strokeVariation = (sin(Math.toRadians(time * 6.0)) * 0.5f + 0.5f).toFloat() // 0..1

            val strokeWidth = baseStroke * (0.6f + 0.8f * strokeVariation)
            val alphaRing = 0.5f + 0.5f * strokeVariation

            val ringAngle = (time * 3) % 360f

            // حلقه اصلی
            drawArc(
                color = neonPalette[(colorPhase * neonPalette.size).toInt() % neonPalette.size].copy(alpha = alphaRing),
                startAngle = ringAngle,
                sweepAngle = 120f,
                useCenter = false,
                topLeft = Offset(center.x - centerRadius, center.y - centerRadius),
                size = Size(centerRadius * 2, centerRadius * 2),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // حلقه ثانویه با سرعت و جهت مخالف
            val ring2Radius = centerRadius * 1.7f
            val ring2StrokeWidth = strokeWidth / 2
            val ring2Alpha = alphaRing * 0.6f
            val ring2Angle = (360f - ringAngle * 1.8f) % 360f

            drawArc(
                color = neonPalette[((colorPhase * neonPalette.size).toInt() + 3) % neonPalette.size].copy(alpha = ring2Alpha),
                startAngle = ring2Angle,
                sweepAngle = 90f,
                useCenter = false,
                topLeft = Offset(center.x - ring2Radius, center.y - ring2Radius),
                size = Size(ring2Radius * 2, ring2Radius * 2),
                style = Stroke(width = ring2StrokeWidth, cap = StrokeCap.Round)
            )

            // رسم ذرات با حرکات ترکیبی: حرکت مداری + نوسانات کوانتومی نرم
            particles.forEach { particle ->
                val angle = (particle.baseAngle + time * particle.speed * particle.orbitDirection) % 360f
                val rad = Math.toRadians(angle.toDouble())

                // موقعیت اصلی روی بیضی
                val x = center.x + particle.orbitRadius * cos(rad).toFloat()
                val y = center.y + particle.orbitRadius * particle.orbitRatio * sin(rad).toFloat()

                // نوسان موج سینوسی نرم با فاز متفاوت برای هر ذره
                val wave = sin(Math.toRadians((time * 4 * particle.speed + particle.wavePhase).toDouble())).toFloat()
                val waveOffsetX = wave * particle.waveAmplitude
                val waveOffsetY = wave * particle.waveAmplitude * 0.5f

                val finalPos = Offset(x + waveOffsetX, y + waveOffsetY)

                // رنگ ذره با تغییر تدریجی رنگ‌ها
                val baseIndex = particle.baseColorIndex
                val nextIndex = (baseIndex + 1) % neonPalette.size
                val lerpFactor = ((colorPhase * neonPalette.size) % 1f)

                val particleColor = lerp(neonPalette[baseIndex], neonPalette[nextIndex], lerpFactor)

                // Glow نرم ذره
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(particleColor.copy(alpha = 0.75f), Color.Transparent),
                        center = finalPos,
                        radius = particle.size * 4f
                    ),
                    radius = particle.size * 4f,
                    center = finalPos,
                    blendMode = BlendMode.Plus
                )

                // خود ذره
                drawCircle(
                    color = particleColor,
                    radius = particle.size,
                    center = finalPos,
                    style = Fill,
                    blendMode = BlendMode.SrcOver
                )
            }
        }
    }
}

private data class QuantumParticle(
    val baseAngle: Float,
    val orbitRadius: Float,
    val speed: Float,
    val size: Float,
    val orbitRatio: Float,
    val baseColorIndex: Int,
    val wavePhase: Float,
    val waveAmplitude: Float,
    val orbitDirection: Int
)

@Preview(showBackground = true)
@Composable
fun PreviewQuantumParticleLoader() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        QuantumParticleLoader()
    }
}
