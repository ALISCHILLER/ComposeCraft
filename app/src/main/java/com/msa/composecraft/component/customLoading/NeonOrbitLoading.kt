package com.msa.composecraft.component.customLoading

import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.*

@Composable
fun NeonOrbitLoading(
    modifier: Modifier = Modifier,
    size: Dp = 160.dp,
    orbitColor: Color = Color(0xFF00FFFF),  // Cyan neon
    coreColor: Color = Color(0xFF8A2BE2),   // BlueViolet neon
    text: String = "در حال اتصال به هسته سایبری..."
) {
    val infiniteTransition = rememberInfiniteTransition()

    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing)
        )
    )

    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
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
                .drawBehind {
                    val radius = min(this.size.width, this.size.height) / 2f
                    val center = Offset(this.size.width / 2f,this.size.height / 2f)

                    // افکت نورانی نئون در اطراف
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(orbitColor.copy(alpha = 0.7f), Color.Transparent),
                            center = center,
                            radius = radius * 1.2f
                        ),
                        center = center,
                        radius = radius * 1.2f
                    )

                    // دایره‌ی مرکزی هسته
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(coreColor, Color.Transparent),
                            center = center,
                            radius = radius * 0.6f
                        ),
                        radius = radius * 0.6f,
                        center = center,
                        blendMode = BlendMode.Screen
                    )

                    // ذرات مداری در حال چرخش
                    for (i in 0 until 12) {
                        val angle = Math.toRadians((rotation + i * 30).toDouble())
                        val orbitRadius = radius * 0.9f
                        val x = center.x + orbitRadius * cos(angle).toFloat()
                        val y = center.y + orbitRadius * sin(angle).toFloat()
                        drawCircle(
                            color = orbitColor.copy(alpha = 0.8f),
                            radius = 6f,
                            center = Offset(x, y)
                        )
                    }

                    // حلقه‌ی دور هسته
                    drawCircle(
                        color = orbitColor.copy(alpha = 0.3f),
                        center = center,
                        radius = radius * pulse,
                        style = Stroke(width = 2f)
                    )
                }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = text,
            fontSize = 17.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun PreviewNeonOrbitLoading() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        NeonOrbitLoading()
    }
}
