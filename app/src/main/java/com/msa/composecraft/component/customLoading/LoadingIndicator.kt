package com.msa.composecraft.component.customLoading

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoadingIndicator(
    modifier: Modifier = Modifier,
    circleSize: Dp = 70.dp,
    strokeWidth: Dp = 8.dp,
    colors: List<Color> = listOf(Color(0xFF6200EE), Color(0xFF03DAC6), Color(0xFF6200EE)),
    loadingText: String? = "Loading...",
    textColor: Color = Color.White,
    shadowElevation: Dp = 10.dp,
    shadowColor: Color = Color(0xFF6200EE)
) {
    val infiniteTransition = rememberInfiniteTransition()

    // انیمیشن چرخش بی‌نهایت
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1300, easing = LinearEasing)
        )
    )

    // انیمیشن پالس سایه (درخشش)
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1300, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(circleSize)
                .shadow(
                    elevation = shadowElevation,
                    shape = CircleShape,
                    ambientColor = shadowColor.copy(alpha = pulseAlpha),
                    spotColor = shadowColor.copy(alpha = pulseAlpha)
                )
                .graphicsLayer {
                    rotationZ = rotation
                }
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val diameter = size.minDimension
                val stroke = strokeWidth.toPx()
                val radius = (diameter - stroke) / 2f

                val sweepGradient = Brush.sweepGradient(
                    colors,
                    center = Offset(diameter / 2, diameter / 2)
                )

                drawArc(
                    brush = sweepGradient,
                    startAngle = 0f,
                    sweepAngle = 270f,
                    useCenter = false,
                    topLeft = Offset(stroke / 2, stroke / 2),
                    size = Size(radius * 2, radius * 2),
                    style = Stroke(width = stroke, cap = StrokeCap.Round)
                )
            }
        }

        if (loadingText != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = loadingText,
                color = textColor,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
fun LoadingIndicatorPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212)),
        contentAlignment = Alignment.Center
    ) {
        LoadingIndicator()
    }
}
