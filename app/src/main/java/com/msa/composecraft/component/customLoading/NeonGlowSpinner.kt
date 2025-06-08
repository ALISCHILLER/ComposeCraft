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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.graphicsLayer

@Composable
fun NeonGlowSpinner(
    modifier: Modifier = Modifier,
    circleSize: Dp = 80.dp,
    strokeWidth: Dp = 10.dp,
    loadingText: String = "در حال بارگذاری...",
    gradientColors: List<Color> = listOf(
        Color(0xFF00FFFF),
        Color(0xFF00FFAA),
        Color(0xFF00CFFF)
    ),
    textColor: Color = Color.White
) {
    val infiniteTransition = rememberInfiniteTransition()

    // 🔁 چرخش بی‌نهایت
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, easing = LinearEasing)
        )
    )

    // 💫 درخشش (Pulse)
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    // 🌀 مقیاس متغیر (Breathing Effect)
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            tween(700, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.size(circleSize)
        ) {
            Canvas(
                modifier = Modifier
                    .matchParentSize()
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        rotationZ = rotation
                    }
            ) {
                val strokePx = size.width / 8 // ضخامت نسبی
                val diameter = size.minDimension
                val radius = (diameter - strokePx) / 2f
                val center = Offset(diameter / 2, diameter / 2)

                // ⚡️ قوس دایره‌ای با گرادیانت
                drawArc(
                    brush = Brush.sweepGradient(colors = gradientColors, center = center),
                    startAngle = 0f,
                    sweepAngle = 270f,
                    useCenter = false,
                    topLeft = Offset(strokePx / 2, strokePx / 2),
                    size = Size(radius * 2, radius * 2),
                    style = Stroke(width = strokePx, cap = StrokeCap.Round),
                    alpha = glowAlpha
                )

                // 💫 درخشش مرکزی
                drawCircle(
                    color = gradientColors.first().copy(alpha = 0.3f * glowAlpha),
                    radius = radius * 1.3f,
                    center = center
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = loadingText,
            color = textColor,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
@Preview(showBackground = true, backgroundColor = 0xFF0D0D0D)
@Composable
fun NeonGlowSpinnerPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D0D0D)),
        contentAlignment = Alignment.Center
    ) {
        NeonGlowSpinner()
    }
}