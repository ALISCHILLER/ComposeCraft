package com.msa.composecraft.component.customLoading

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.*
import kotlin.random.Random


data class NeonParticle(
    var theta: Float,   // زاویه طولی (0..2π)
    var phi: Float,     // زاویه عرضی (0..π)
    var baseRadius: Float,
    var speedTheta: Float,
    var speedPhi: Float,
    val color: Color
)
@Composable
fun NeonEarth3DLoader(
    modifier: Modifier = Modifier,
    particleCount: Int = 120,
    earthRadiusDp: Float = 140f,
    baseColor: Color = Color(0xFF121212),
    neonColors: List<Color> = listOf(
        Color(0xFF00FFFF),
        Color(0xFF8A2BE2),
        Color(0xFF00FF94),
        Color(0xFFFF00FF),
        Color(0xFF00CFFF)
    ),
    rotationSpeed: Float = 0.005f,
    animationDuration: Int = 30000
) {
    val density = LocalDensity.current
    val earthRadiusPx = with(density) { earthRadiusDp.dp.toPx() }

    // 🧠 ذرات با موقعیت تصادفی روی کره
    val particles = remember {
        List(particleCount) {
            NeonParticle(
                theta = Random.nextFloat() * 2f * PI.toFloat(),
                phi = Random.nextFloat() * PI.toFloat(),
                baseRadius = Random.nextFloat() * 4f + 2f,
                speedTheta = (Random.nextFloat() - 0.5f) * rotationSpeed,
                speedPhi = (Random.nextFloat() - 0.5f) * rotationSpeed * 0.3f,
                color = neonColors.random()
            )
        }.toMutableStateList()
    }

    val infiniteTransition = rememberInfiniteTransition()

    // 🌀 چرخش کلی کره
    val globalRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(animationDuration, easing = LinearEasing)
        )
    )

    // 💡 موقعیت تپ‌زنی
    var touchPoint by remember { mutableStateOf<Offset?>(null) }
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .size(earthRadiusDp.dp * 2)
            .background(baseColor)
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    touchPoint = offset
                    coroutineScope.launch {
                        delay(600)
                        touchPoint = null
                    }
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val cx = size.width / 2f
            val cy = size.height / 2f

            // 🌐 کره مرکزی با گرادیانت
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color.DarkGray, baseColor),
                    center = Offset(cx, cy),
                    radius = earthRadiusPx
                ),
                radius = earthRadiusPx,
                center = Offset(cx, cy)
            )

            // 🌟 هاله نورانی دور کره
            drawCircle(
                brush = Brush.radialGradient(
                    colors = neonColors.map { it.copy(alpha = 0.2f) },
                    center = Offset(cx, cy),
                    radius = earthRadiusPx * 1.3f
                ),
                radius = earthRadiusPx * 1.3f,
                center = Offset(cx, cy)
            )

            // 🚀 حرکت ذرات روی کره
            particles.forEach { p ->
                // 🔁 بروزرسانی زاویه‌ها
                p.theta = (p.theta + p.speedTheta) % (2f * PI.toFloat())
                p.phi = (p.phi + p.speedPhi).coerceIn(0.1f, PI.toFloat() - 0.1f)

                // 🌍 محاسبه مختصات 3D -> 2D
                val sinPhi = sin(p.phi.toDouble()).toFloat()
                val cosPhi = cos(p.phi.toDouble()).toFloat()
                val sinTheta = sin(p.theta.toDouble()).toFloat()
                val cosTheta = cos(p.theta.toDouble()).toFloat()

                val x3d = earthRadiusPx * sinPhi * cosTheta
                val y3d = earthRadiusPx * sinPhi * sinTheta
                val z3d = earthRadiusPx * cosPhi

                // 📏 محاسبه عمق (برای اندازه و شفافیت)
                val perspective = (z3d / earthRadiusPx + 1f) / 2f
                val particleSize = p.baseRadius * lerp(0.5f, 2f, perspective)
                var alpha = lerp(0.4f, 1f, perspective)

                // 🎯 تغییرات در صورت تپ‌زنی
                touchPoint?.let { tp ->
                    val dx = tp.x - (cx + x3d)
                    val dy = tp.y - (cy + y3d)
                    val dist = sqrt(dx * dx + dy * dy)

                    if (dist < 100f) {
                        val influence = (100f - dist) / 100f
                        alpha += influence * 0.4f
                    }
                }

                // 🌌 رسم ذره اصلی
                drawCircle(
                    color = p.color.copy(alpha = alpha.coerceIn(0.3f, 1f)),
                    center = Offset(cx + x3d, cy + y3d),
                    radius = particleSize
                )

                // 🌠 Glow دور ذره
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            p.color.copy(alpha = alpha * 0.6f),
                            Color.Transparent
                        ),
                        center = Offset(cx + x3d, cy + y3d),
                        radius = particleSize * 5
                    ),
                    radius = particleSize * 5,
                    center = Offset(cx + x3d, cy + y3d),
                    blendMode = BlendMode.Plus
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewNeonEarth3DLoader() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        NeonEarth3DLoader()
    }
}