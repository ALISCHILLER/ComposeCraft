package com.msa.composecraft.component.chart

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

// کلاس داده‌ای برای نگهداری داده‌های میله‌ها
data class BarData(
    val label: String, // برچسب میله
    val value: Float, // مقدار میله
    val color: Color // رنگ میله
)

// کامپوننت اصلی برای نمایش نمودار میله‌ای
@Composable
fun BarChart(
    data: List<BarData>, // لیست داده‌های میله‌ها
    modifier: Modifier = Modifier,
    axisColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f), // رنگ محورها
    textColor: Color = MaterialTheme.colorScheme.onSurface, // رنگ متن
    animationDuration: Int = 1000 // مدت زمان انیمیشن
) {
    // حالت برای انیمیشن نمودار
    val animationProgress = remember { Animatable(0f) }
    LaunchedEffect(data) {
        animationProgress.animateTo(1f, animationSpec = tween(durationMillis = animationDuration))
    }

    // اندازه‌گیری متن برای نمایش برچسب‌ها و مقادیر
    val textMeasurer = rememberTextMeasurer()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // محاسبه مقیاس‌ها برای تبدیل مقادیر داده به مختصات صفحه
            val maxValue = data.maxOf { it.value }
            val barWidth = size.width / data.size
            val yScale = size.height / maxValue

            // ترسیم محور X و Y
            drawLine(
                color = axisColor,
                start = Offset(0f, size.height),
                end = Offset(size.width, size.height),
                strokeWidth = 2f
            )
            drawLine(
                color = axisColor,
                start = Offset(0f, 0f),
                end = Offset(0f, size.height),
                strokeWidth = 2f
            )

            // ترسیم میله‌ها و برچسب‌ها
            data.forEachIndexed { index, bar ->
                val barHeight = bar.value * yScale * animationProgress.value
                val barX = index * barWidth + barWidth / 4
                val barY = size.height - barHeight

                // ترسیم میله
                drawRect(
                    color = bar.color,
                    topLeft = Offset(barX, barY),
                    size = Size(barWidth / 2, barHeight)
                )

                // ترسیم برچسب میله
                val labelLayoutResult = textMeasurer.measure(
                    text = bar.label,
                    style = TextStyle(color = textColor, fontSize = 12.sp)
                )
                drawText(
                    textLayoutResult = labelLayoutResult,
                    topLeft = Offset(barX + barWidth / 4 - labelLayoutResult.size.width / 2, size.height + 8.dp.toPx())
                )

                // ترسیم مقدار میله
                val valueLayoutResult = textMeasurer.measure(
                    text = bar.value.toString(),
                    style = TextStyle(color = textColor, fontSize = 12.sp)
                )
                drawText(
                    textLayoutResult = valueLayoutResult,
                    topLeft = Offset(barX + barWidth / 4 - valueLayoutResult.size.width / 2, barY - 16.dp.toPx())
                )
            }
        }
    }
}

// پیش‌نمایش: نمایش نمودار میله‌ای
@Preview(showBackground = true)
@Composable
fun PreviewBarChart() {
    val data = listOf(
        BarData("فروردین", 100f, Color(0xFF6200EE)),
        BarData("اردیبهشت", 200f, Color(0xFF03DAC6)),
        BarData("خرداد", 150f, Color(0xFFE91E63)),
        BarData("تیر", 300f, Color(0xFFFFC107)),
        BarData("مرداد", 250f, Color(0xFF4CAF50))
    )

    MaterialTheme {
        BarChart(
            data = data,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )
    }
}