package com.msa.composecraft.component.customLoading

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun NeonLinearLoading(
    modifier: Modifier = Modifier,
    height: Dp = 8.dp,
    baseColor: Color = Color.DarkGray,
    neonColor: Color = Color(0xFF00FFFF), // Cyan neon
    animationDuration: Int = 2000
) {
    val infiniteTransition = rememberInfiniteTransition()

    // مقدار حرکت افکت از چپ به راست به صورت 0f تا 1f
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(animationDuration, easing = LinearEasing)
        )
    )

    Box(
        modifier = modifier
            .height(height)
            .fillMaxWidth()
            .background(baseColor.copy(alpha = 0.3f))
            .drawBehind {
                val barWidth = size.width
                val barHeight = size.height

                // طول نوار نئون متحرک
                val neonWidth = barWidth / 3

                // موقعیت شروع نوار نئون که از چپ به راست حرکت می‌کند
                val neonStart = progress * (barWidth + neonWidth) - neonWidth

                // گرادیان خطی برای ایجاد افکت نئون
                val gradient = Brush.linearGradient(
                    colors = listOf(
                        neonColor.copy(alpha = 0f),
                        neonColor.copy(alpha = 0.8f),
                        neonColor.copy(alpha = 0f)
                    ),
                    start = Offset(neonStart, 0f),
                    end = Offset(neonStart + neonWidth, 0f)
                )

                // رسم مستطیل با گرادیان متحرک
                drawRect(
                    brush = gradient,
                    topLeft = Offset(0f, 0f),
                    size = size
                )
            }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewNeonLinearLoading() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 50.dp),
        verticalArrangement = Arrangement.Center
    ) {
        NeonLinearLoading()
    }
}
