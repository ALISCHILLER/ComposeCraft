package com.msa.composecraft.component.customLoading



import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ShinySpinner(
    modifier: Modifier = Modifier,
    circleSize: Dp = 70.dp,
    strokeWidth: Dp = 8.dp,
    loadingText: String = "در حال بارگذاری...",
    colors: List<Color> = listOf(Color(0xFF00BCD4), Color(0xFF8BC34A), Color(0xFF00BCD4)),
    textColor: Color = Color.White
) {
    val infiniteTransition = rememberInfiniteTransition()

    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(1100, easing = LinearEasing))
    )

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            tween(1000, easing = FastOutSlowInEasing),
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
                .drawBehind {
                    val stroke = strokeWidth.toPx()
                    val radius = (size.minDimension - stroke) / 2f

                    drawArc(
                        brush = Brush.sweepGradient(colors),
                        startAngle = 0f,
                        sweepAngle = 270f,
                        useCenter = false,
                        topLeft = Offset(stroke / 2, stroke / 2),
                        size = Size(radius * 2, radius * 2),
                        style = Stroke(width = stroke, cap = StrokeCap.Round),
                        alpha = glowAlpha
                    )
                }
                .graphicsLayer { rotationZ = rotation }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = loadingText,
            color = textColor,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF101010)
@Composable
fun ShinySpinnerPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF101010)),
        contentAlignment = Alignment.Center
    ) {
        ShinySpinner()
    }
}
