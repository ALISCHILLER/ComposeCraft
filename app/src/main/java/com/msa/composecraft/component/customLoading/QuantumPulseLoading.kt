package com.msa.composecraft.component.customLoading

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.*

@Composable
fun QuantumPulseLoading(
    modifier: Modifier = Modifier,
    size: Dp = 160.dp,
    particleColor: Color = Color(0xFF00FFFF),
    textColor: Color = Color.White,
    text: String = "اتصال به شبکه کوانتومی..."
) {
    val infiniteTransition = rememberInfiniteTransition()

    // 🔁 چرخش بی‌نهایت
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = LinearEasing)
        )
    )

    // 🌀 افکت تنفسی (Breathing)
    val scaleAnim by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    // 🎛️ ضخامت خط مرکزی
    val pulseStroke by infiniteTransition.animateFloat(
        initialValue = 2f,
        targetValue = 4f,
        animationSpec = infiniteRepeatable(
            animation = tween(700, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Column(
        modifier = modifier
            .background(Color.Black)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .graphicsLayer {
                    scaleX = scaleAnim
                    scaleY = scaleAnim
                    rotationZ = rotation
                }
        ) {
            Canvas(modifier = Modifier.matchParentSize()) {
                val canvasWidth = this.size.width
                val canvasHeight = this.size.height
                val radius = min(canvasWidth, canvasHeight) / 2f
                val center = Offset(canvasWidth / 2f, canvasHeight / 2f)

                // 🌠 ذرات متحرک دور مرکز
                for (i in 0 until 30) {
                    val angle = Math.toRadians((rotation + i * 12).toDouble())
                    val r = radius * (0.7f + 0.2f * sin(rotation / 100))
                    val x = center.x + r * cos(angle).toFloat()
                    val y = center.y + r * sin(angle).toFloat()
                    drawCircle(
                        color = particleColor.copy(alpha = 0.5f),
                        radius = 4f,
                        center = Offset(x, y)
                    )
                }

                // 🌟 هسته مرکزی با گرادیانت شعاعی
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(particleColor, Color.Transparent),
                        center = center,
                        radius = radius * 0.6f
                    ),
                    radius = radius * 0.6f,
                    center = center,
                    blendMode = BlendMode.Plus
                )

                // 🌕 دایره داخلی با افکت Pulse
                drawCircle(
                    color = particleColor.copy(alpha = 0.3f),
                    radius = radius * 0.3f,
                    center = center
                )

                // 🔺 مثلث مرکزی – برای ظاهر کوانتومی
                val trianglePath = Path().apply {
                    moveTo(center.x, center.y - radius * 0.3f)
                    lineTo(center.x + radius * 0.2f, center.y + radius * 0.1f)
                    lineTo(center.x - radius * 0.2f, center.y + radius * 0.1f)
                    close()
                }

                drawPath(
                    path = trianglePath,
                    color = particleColor,
                    style = Stroke(width = pulseStroke)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = text,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun Preview_QuantumPulseLoading() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        QuantumPulseLoading(
            particleColor = Color.Magenta,
            text = "اتصال به شبکه کوانتومی"
        )
    }
}