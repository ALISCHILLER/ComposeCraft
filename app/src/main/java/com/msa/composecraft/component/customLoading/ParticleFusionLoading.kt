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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.DrawScope
import kotlin.math.*

@Composable
fun ParticleFusionLoading(
    modifier: Modifier = Modifier,
    size: Dp = 120.dp,
    particleColor: Color = Color.Cyan,
    backgroundColor: Color = Color.Black,
    text: String = "در حال ورود به دنیای سایبری..."
) {
    val infiniteTransition = rememberInfiniteTransition()

    val angleAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = LinearEasing)
        )
    )

    val scaleAnim by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .background(backgroundColor)
                .graphicsLayer {
                    rotationZ = angleAnim
                    scaleX = scaleAnim
                    scaleY = scaleAnim
                }
        ) {
            Canvas(modifier = Modifier.matchParentSize()) {
                drawParticleFusion(this, angleAnim, particleColor)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = text,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}
@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun PreviewParticleFusionLoading() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF000000)),
        contentAlignment = Alignment.Center
    ) {
        ParticleFusionLoading()
    }
}
private fun DrawScope.drawParticleFusion(
    drawContext: DrawScope,
    angleAnim: Float,
    particleColor: Color
) = with(drawContext) {
    val canvasWidth = size.width
    val canvasHeight = size.height
    val radius = min(canvasWidth, canvasHeight) / 2f
    val center = Offset(canvasWidth / 2f, canvasHeight / 2f)

    // ذرات چرخان
    for (i in 0 until 30) {
        val angle = Math.toRadians((angleAnim + i * 12).toDouble())
        val r = radius * 0.9f
        val x = center.x + r * cos(angle).toFloat()
        val y = center.y + r * sin(angle).toFloat()
        drawCircle(
            color = particleColor.copy(alpha = 0.5f),
            radius = 3f,
            center = Offset(x, y)
        )
    }

    // هسته‌ی درخشان
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

    // چهارضلعی مرکزی
    val path = Path().apply {
        moveTo(center.x, center.y - radius / 2)
        lineTo(center.x + radius / 2, center.y)
        lineTo(center.x, center.y + radius / 2)
        lineTo(center.x - radius / 2, center.y)
        close()
    }

    drawPath(path, color = particleColor.copy(alpha = 0.7f), style = Stroke(width = 2f))
}