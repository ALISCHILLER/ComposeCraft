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
fun QuantumParticleLoaderDynamic(
    modifier: Modifier = Modifier,
    size: Float = 280f,
    particleCount: Int = 40,
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

    val colorPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(14000, easing = LinearEasing))
    )

    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(10000, easing = LinearEasing))
    )

    val center = Offset(size / 2, size / 2)

    // برای داینامیک‌تر شدن، پارامترهای ذرات رو با نویز و سینوس ترکیب می‌کنیم
    val particles = remember {
        List(particleCount) {
            QuantumParticleDynamic(
                baseAngle = Random.nextFloat() * 360f,
                baseOrbitRadius = Random.nextFloat() * size * 0.35f + size * 0.1f,
                baseSpeed = Random.nextFloat() * 0.6f + 0.2f,
                size = Random.nextFloat() * 8f + 3f,
                orbitRatio = 0.6f + Random.nextFloat() * 0.6f,
                baseColorIndex = it % neonPalette.size,
                direction = if (Random.nextBoolean()) 1 else -1,
                phaseOffset = Random.nextFloat() * 360f
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
            val centerRadius = size * 0.08f
            val ringStroke = size * 0.015f
            val ringAngle = (time * 3) % 360f

            // دو حلقه چرخان متفاوت
            drawArc(
                color = neonPalette[(colorPhase * neonPalette.size).toInt() % neonPalette.size].copy(alpha = 0.8f),
                startAngle = ringAngle,
                sweepAngle = 90f,
                useCenter = false,
                topLeft = Offset(center.x - centerRadius, center.y - centerRadius),
                size = Size(centerRadius * 2, centerRadius * 2),
                style = Stroke(width = ringStroke, cap = StrokeCap.Round),
                alpha = 0.9f
            )
            drawArc(
                color = neonPalette[((colorPhase * neonPalette.size).toInt() + 2) % neonPalette.size].copy(alpha = 0.5f),
                startAngle = (360f - ringAngle) % 360f,
                sweepAngle = 60f,
                useCenter = false,
                topLeft = Offset(center.x - centerRadius * 1.5f, center.y - centerRadius * 1.5f),
                size = Size(centerRadius * 3, centerRadius * 3),
                style = Stroke(width = ringStroke / 2, cap = StrokeCap.Round),
                alpha = 0.8f
            )

            particles.forEach { particle ->
                // برای هر ذره، موقعیت را با حرکت پیچیده‌تر حساب می‌کنیم:

                // تغییر زاویه با زمان، با جهت (ساعتگرد یا پادساعتگرد)
                val angle = (particle.baseAngle + particle.direction * time * particle.baseSpeed) % 360f

                // نوسان شعاعی نرم (شعاع متغیر)
                val dynamicOrbitRadius = particle.baseOrbitRadius + sin((time * 2f + particle.phaseOffset) * PI / 180).toFloat() * 15f

                val rad = Math.toRadians(angle.toDouble())

                // مختصات بیضوی
                val x = center.x + dynamicOrbitRadius * cos(rad).toFloat()
                val y = center.y + dynamicOrbitRadius * particle.orbitRatio * sin(rad).toFloat()

                // موج سینوسی کوچک برای جابجایی نرم (تکان‌های کوانتومی)
                val waveX = sin((time * 6f + particle.phaseOffset) * PI / 180).toFloat() * 4f
                val waveY = cos((time * 8f + particle.phaseOffset) * PI / 180).toFloat() * 4f

                val pos = Offset(x + waveX, y + waveY)

                // تغییر رنگ تدریجی ذرات
                val baseIndex = particle.baseColorIndex
                val nextIndex = (baseIndex + 1) % neonPalette.size
                val lerpFactor = ((colorPhase * neonPalette.size) % 1f)
                val particleColor = lerp(neonPalette[baseIndex], neonPalette[nextIndex], lerpFactor)

                // تغییر اندازه ذره به صورت نوسانی
                val dynamicSize = particle.size + sin((time * 10f + particle.phaseOffset) * PI / 180).toFloat() * 1.5f

                // Glow ذره
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(particleColor.copy(alpha = 0.7f), Color.Transparent),
                        center = pos,
                        radius = dynamicSize * 3
                    ),
                    radius = dynamicSize * 3,
                    center = pos,
                    blendMode = BlendMode.Plus
                )

                // خود ذره
                drawCircle(
                    color = particleColor,
                    radius = dynamicSize.coerceAtLeast(1f),
                    center = pos,
                    style = Fill,
                    blendMode = BlendMode.SrcOver
                )
            }
        }
    }
}

private data class QuantumParticleDynamic(
    val baseAngle: Float,
    val baseOrbitRadius: Float,
    val baseSpeed: Float,
    val size: Float,
    val orbitRatio: Float,
    val baseColorIndex: Int,
    val direction: Int, // جهت حرکت +1 یا -1
    val phaseOffset: Float // برای تغییر فاز نوسان ذرات
)

@Preview(showBackground = true)
@Composable
fun PreviewQuantumParticleLoaderDynamic() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        QuantumParticleLoaderDynamic()
    }
}
