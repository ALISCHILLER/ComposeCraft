package com.msa.composecraft.component.customProgress

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun QuantumPulseLinearLoading(
    modifier: Modifier = Modifier,
    height: Dp = 24.dp,
    backgroundColor: Color = Color.LightGray.copy(alpha = 0.1f),
    gradientColors: List<Color> = listOf(Color(0xFF00FFFF), Color(0xFF0051FF)),
    loadingText: String = "در حال بارگذاری...",
    textColor: Color = Color.White,
    showText: Boolean = true
) {
    val infiniteTransition = rememberInfiniteTransition()

    // 🔁 انیمیشن پیشرفت بی‌نهایت
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing)
        )
    )

    // 💫 Shimmer Effect متحرک
    val shimmerOffset by infiniteTransition.animateFloat(
        initialValue = -100f,
        targetValue = 100f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    // 🌀 Pulse Effect برای درخشش
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .clip(RoundedCornerShape(10.dp))
                .background(backgroundColor)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progress)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        Brush.linearGradient(
                            colors = gradientColors,
                            start = Offset.Zero,
                            end = Offset.Infinite
                        )
                    )
                    .drawWithContent {
                        drawContent()

                        val width = size.width
                        val shimmerWidth = width / 4
                        val xStart = shimmerOffset % width

                        // 💡 Shimmer Light Sweep
                        drawRect(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.White.copy(alpha = 0.4f),
                                    Color.Transparent
                                ),
                                start = Offset(xStart, 0f),
                                end = Offset(xStart + shimmerWidth, size.height)
                            ),
                            topLeft = Offset.Zero,
                            size = size
                        )

                        // 🌟 Glow Pulse
                        drawRect(
                            color = gradientColors.last().copy(alpha = pulseAlpha * 0.3f),
                            topLeft = Offset.Zero,
                            size = Size(width * progress, size.height)
                        )
                    }
            )
        }

        if (showText) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = loadingText,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
fun Preview_QuantumPulseLinearLoading() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        QuantumPulseLinearLoading(
            height = 24.dp,
            gradientColors = listOf(Color.Magenta, Color.Cyan),
            loadingText = "اتصال به شبکه کوانتومی",
            textColor = Color.White,
            showText = true
        )
    }
}