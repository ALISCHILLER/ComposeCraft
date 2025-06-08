package com.msa.composecraft.component.customLoading

import androidx.compose.ui.unit.Dp
import kotlin.random.Random
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.*

data class Particle3DGlobe(
    var angleH: Float, // افقی - 0 تا 2π
    var angleV: Float, // عمودی - 0 تا π
    var radius: Float,
    val speedH: Float,
    val speedV: Float,
    val color: Color
)

@Composable
fun QuantumNeonGlobeLoading(
    modifier: Modifier = Modifier,
    size: Dp = 280.dp,
    baseColor: Color = Color(0xFF0A0A12),
    neonColors: List<Color> = listOf(
        Color(0xFF00FFFF),
        Color(0xFF8A2BE2),
        Color(0xFF00FF94),
        Color(0xFFFF00FF),
        Color(0xFF00CFFF)
    ),
    particleCount: Int = 60,
    glowPulseSpeed: Int = 2200
) {
    val infiniteTransition = rememberInfiniteTransition()

    // انیمیشن ضربان نور نئون
    val glowPulse by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(glowPulseSpeed, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val density = LocalDensity.current
    val canvasSizePx = with(density) { size.toPx() }
    val globeRadius = canvasSizePx / 2.5f

    // ذخیره ذرات روی کره (زاویه‌ها به رادیان)
    val particles = remember(particleCount) {
        List(particleCount) {
            Particle3DGlobe(
                angleH = Random.nextFloat() * 2 * PI.toFloat(),
                angleV = Random.nextFloat() * PI.toFloat(),
                radius = Random.nextFloat() * 4f + 2f,
                speedH = (Random.nextFloat() - 0.5f) * 0.015f,
                speedV = (Random.nextFloat() - 0.5f) * 0.007f,
                color = neonColors.random()
            )
        }.toMutableList()
    }

    // چرخش کره بر اساس انیمیشن و لمس کاربر
    var rotationH by remember { mutableStateOf(0f) }
    var rotationV by remember { mutableStateOf(PI.toFloat() / 2f) } // نیمه بالایی رو اول می‌بینیم

    // برای هندل درگ چرخش
    val dragModifier = Modifier.pointerInput(Unit) {
        detectDragGestures { change, dragAmount ->
            rotationH += dragAmount.x * 0.005f
            rotationV += dragAmount.y * 0.005f
            rotationV = rotationV.coerceIn(0.1f, PI.toFloat() - 0.1f) // محدود کردن به بالای کره
            change.consume()
        }
    }

    // به‌روزرسانی ذرات برای حرکت روی سطح کره
    LaunchedEffect(Unit) {
        while (true) {
            particles.forEach { p ->
                p.angleH = (p.angleH + p.speedH) % (2 * PI.toFloat())
                p.angleV = (p.angleV + p.speedV).coerceIn(0.05f, PI.toFloat() - 0.05f)
            }
            kotlinx.coroutines.delay(16L) // حدود 60fps
        }
    }

    Box(
        modifier = modifier
            .size(size)
            .background(baseColor)
            .then(dragModifier),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasSizePx = size.toPx()
            val centerX = canvasSizePx / 2f
            val centerY = canvasSizePx / 2f

            // بک‌گراند گرادیانت کروی (نئونی ملایم)
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        baseColor,
                        baseColor.copy(alpha = 0.3f),
                        neonColors.random().copy(alpha = 0.1f),
                        Color.Black
                    ),
                    center = Offset(centerX, centerY),
                    radius = globeRadius * 3
                ),
                radius = globeRadius * 3,
                center = Offset(centerX, centerY)
            )

            // کره زمین با موج نئون
            val waveCount = 5
            val waveAmplitude = globeRadius * 0.06f
            val waveLength = globeRadius * 0.8f
            val time = glowPulse * 2 * PI.toFloat()

            for (wave in 0 until waveCount) {
                val angleOffset = wave * (2 * PI.toFloat() / waveCount)
                val path = Path()
                val steps = 60

                for (i in 0..steps) {
                    val angle = i.toFloat() / steps * 2 * PI.toFloat()
                    // شعاع موج برای هر زاویه با سینوس
                    val r = globeRadius + sin(angle * waveLength + time + angleOffset) * waveAmplitude
                    // محاسبه مختصات دایره‌ای سه‌بعدی روی صفحه دوبعدی با چرخش‌های عمودی و افقی
                    val x3d = r * sin(angle) * cos(rotationV) + centerX
                    val y3d = r * cos(angle) + centerY + r * sin(angle) * sin(rotationV)

                    if (i == 0) path.moveTo(x3d, y3d)
                    else path.lineTo(x3d, y3d)
                }
                path.close()

                drawPath(
                    path = path,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            neonColors[wave % neonColors.size].copy(alpha = 0.6f * glowPulse),
                            Color.Transparent
                        ),
                        startY = centerY - globeRadius,
                        endY = centerY + globeRadius
                    ),
                    style = Fill,
                    blendMode = BlendMode.Plus
                )
            }

            // ذرات نئون روی کره
            particles.forEach { p ->
                // تبدیل زاویه کروی به مختصات کارتزین سه‌بعدی
                val x3d = globeRadius * sin(p.angleV) * cos(p.angleH)
                val y3d = globeRadius * cos(p.angleV)
                val z3d = globeRadius * sin(p.angleV) * sin(p.angleH)

                // اعمال چرخش عمودی و افقی
                val rotatedX = x3d * cos(rotationH) - z3d * sin(rotationH)
                val rotatedZ = x3d * sin(rotationH) + z3d * cos(rotationH)
                val rotatedY = y3d * cos(rotationV) - rotatedZ * sin(rotationV)
                val projectedZ = y3d * sin(rotationV) + rotatedZ * cos(rotationV)

                // تبدیل به صفحه دوبعدی با پرسپکتیو ساده
                val perspective = 0.8f + (projectedZ / (globeRadius * 3))
                val screenX = centerX + rotatedX * perspective
                val screenY = centerY + rotatedY * perspective

                // اندازه ذره بر اساس فاصله (برای جلوه عمق)
                val particleRadius = p.radius * perspective * glowPulse

                if (projectedZ > 0) { // فقط ذرات نیم‌کره جلو
                    drawCircle(
                        color = p.color.copy(alpha = 0.8f * glowPulse),
                        radius = particleRadius,
                        center = Offset(screenX, screenY),
                        style = Fill,
                        blendMode = BlendMode.Screen
                    )
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                p.color.copy(alpha = 0.6f * glowPulse),
                                Color.Transparent
                            ),
                            center = Offset(screenX, screenY),
                            radius = particleRadius * 6
                        ),
                        radius = particleRadius * 6,
                        center = Offset(screenX, screenY),
                        blendMode = BlendMode.Plus
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun Preview_QuantumNeonGlobeLoading() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        QuantumNeonGlobeLoading()
    }
}
