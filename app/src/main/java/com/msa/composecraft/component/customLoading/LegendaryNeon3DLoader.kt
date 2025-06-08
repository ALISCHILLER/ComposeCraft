package com.msa.composecraft.component.customLoading

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.*
import kotlin.random.Random

@Composable
fun LegendaryNeon3DLoader(
    modifier: Modifier = Modifier,
    size: Float = 200f,
    neonColors: List<Color> = listOf(
        Color(0xFF00FFFF),
        Color(0xFF8A2BE2),
        Color(0xFF00FF94),
        Color(0xFFFF00FF),
        Color(0xFF00CFFF)
    )
) {
    val infiniteTransition = rememberInfiniteTransition()

    // چرخش کلی دایره
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing)
        )
    )

    // پالس نور مرکزی
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    // انیمیشن ذرات نئون
    val particleCount = 25
    val particles = remember {
        List(particleCount) {
            Particle3D(
                angle = Random.nextFloat() * 360f,
                distance = size / 2 * (0.6f + Random.nextFloat() * 0.4f),
                radius = Random.nextFloat() * 4f + 2f,
                speed = 0.5f + Random.nextFloat(),
                color = neonColors.random()
            )
        }
    }
    val updatedParticles = remember { particles.toMutableList() }

    LaunchedEffect(rotation) {
        for (i in updatedParticles.indices) {
            val p = updatedParticles[i]
            var newAngle = (p.angle + p.speed) % 360f
            updatedParticles[i] = p.copy(angle = newAngle)
        }
    }

    Box(
        modifier = modifier
            .size(with(LocalDensity.current) { size.toDp() })
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size / 2, size / 2)
            val radiusOuter = size / 2.2f
            val radiusInner = radiusOuter * 0.75f

            // پس زمینه تیره گرد
            drawCircle(
                color = Color(0xFF0A0A0A),
                radius = radiusOuter + 20f,
                center = center,
                style = Fill
            )

            // حلقه اصلی نئون با سایه سه بعدی
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        neonColors[0].copy(alpha = 0.9f * pulse),
                        Color.Transparent
                    ),
                    center = center,
                    radius = radiusOuter + 30f
                ),
                radius = radiusOuter + 30f,
                center = center,
                style = Fill,
                blendMode = BlendMode.Plus
            )

            // حلقه در حال چرخش
            with(drawContext.canvas) {
                save()
                rotate(rotation, center.x, center.y)
                neonColors.forEachIndexed { index, color ->
                    val sweep = 45f + index * 10f
                    val startAngle = index * 70f
                    drawArc(
                        color = color.copy(alpha = 0.9f * pulse),
                        startAngle = startAngle,
                        sweepAngle = sweep,
                        useCenter = false,
                        topLeft = Offset(center.x - radiusOuter, center.y - radiusOuter),
                        size = Size(radiusOuter * 2, radiusOuter * 2),
                        style = Stroke(width = 15f, cap = StrokeCap.Round)
                    )
                }
                restore()
            }

            // پالس داخلی
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        neonColors[1].copy(alpha = 0.8f * pulse),
                        Color.Transparent
                    ),
                    center = center,
                    radius = radiusInner
                ),
                radius = radiusInner,
                center = center,
                style = Fill,
                blendMode = BlendMode.Plus
            )

            // ذرات نئون متحرک در اطراف حلقه
            updatedParticles.forEach { p ->
                val radian = Math.toRadians(p.angle.toDouble())
                val x = center.x + cos(radian) * p.distance
                val y = center.y + sin(radian) * p.distance

                drawCircle(
                    color = p.color.copy(alpha = 0.85f * pulse),
                    radius = p.radius,
                    center = Offset(x.toFloat(), y.toFloat()),
                    style = Fill,
                    blendMode = BlendMode.Screen
                )
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(p.color.copy(alpha = 0.45f * pulse), Color.Transparent),
                        center = Offset(x.toFloat(), y.toFloat()),
                        radius = p.radius * 5
                    ),
                    radius = p.radius * 5,
                    center = Offset(x.toFloat(), y.toFloat()),
                    blendMode = BlendMode.Plus
                )
            }
        }
    }
}

private data class Particle3D(
    val angle: Float,
    val distance: Float,
    val radius: Float,
    val speed: Float,
    val color: Color
)

@Preview(showBackground = true)
@Composable
fun PreviewLegendaryNeon3DLoader() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        LegendaryNeon3DLoader()
    }
}
