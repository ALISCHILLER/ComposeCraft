package com.msa.composecraft.component.chart



import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

// کلاس داده‌ای برای نگهداری داده‌های نمودار دایره‌ای
data class PieChartData(
    val label: String, // برچسب بخش
    val value: Float, // مقدار بخش
    val color: Color // رنگ بخش
)

// کامپوننت اصلی برای نمایش نمودار دایره‌ای
@Composable
fun PieChart(
    data: List<PieChartData>, // لیست داده‌های نمودار
    modifier: Modifier = Modifier,
    textColor: Color = MaterialTheme.colorScheme.onSurface, // رنگ متن
    animationDuration: Int = 1000 // مدت زمان انیمیشن
) {
    // حالت برای انیمیشن نمودار
    val animationProgress = remember { Animatable(0f) }
    LaunchedEffect(data) {
        animationProgress.animateTo(1f, animationSpec = tween(durationMillis = animationDuration))
    }

    // اندازه‌گیری متن برای نمایش برچسب‌ها
    val textMeasurer = rememberTextMeasurer()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(16.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // محاسبه مجموع مقادیر
            val totalValue = data.sumOf { it.value.toDouble() }.toFloat()

            // مختصات مرکز و شعاع نمودار
            val center = Offset(size.width / 2, size.height / 2)
            val radius = size.width.coerceAtMost(size.height) / 2 * 0.8f

            // ترسیم بخش‌های نمودار
            var startAngle = 0f
            data.forEach { item ->
                val sweepAngle = (item.value / totalValue) * 360f * animationProgress.value
                drawPieSlice(
                    center = center,
                    radius = radius,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    color = item.color
                )
                startAngle += sweepAngle
            }

            // ترسیم برچسب‌ها
            startAngle = 0f
            data.forEach { item ->
                val sweepAngle = (item.value / totalValue) * 360f * animationProgress.value
                val midAngle = startAngle + sweepAngle / 2
                val labelRadius = radius * 1.2f
                val labelPosition = Offset(
                    center.x + labelRadius * cos(Math.toRadians(midAngle.toDouble())).toFloat(),
                    center.y + labelRadius * sin(Math.toRadians(midAngle.toDouble())).toFloat()
                )
                val labelLayoutResult = textMeasurer.measure(
                    text = item.label,
                    style = TextStyle(color = textColor, fontSize = 12.sp)
                )
                drawText(
                    textLayoutResult = labelLayoutResult,
                    topLeft = Offset(
                        labelPosition.x - labelLayoutResult.size.width / 2,
                        labelPosition.y - labelLayoutResult.size.height / 2
                    )
                )
                startAngle += sweepAngle
            }
        }
    }
}

// ترسیم یک بخش از نمودار دایره‌ای
private fun DrawScope.drawPieSlice(
    center: Offset,
    radius: Float,
    startAngle: Float,
    sweepAngle: Float,
    color: Color
) {
    drawArc(
        color = color,
        startAngle = startAngle,
        sweepAngle = sweepAngle,
        useCenter = true,
        topLeft = Offset(center.x - radius, center.y - radius),
        size = Size(radius * 2, radius * 2)
    )
}

// پیش‌نمایش: نمایش نمودار دایره‌ای
@Preview(showBackground = true)
@Composable
fun PreviewPieChart() {
    val data = listOf(
        PieChartData("فروردین", 100f, Color(0xFF6200EE)),
        PieChartData("اردیبهشت", 200f, Color(0xFF03DAC6)),
        PieChartData("خرداد", 150f, Color(0xFFE91E63)),
        PieChartData("تیر", 300f, Color(0xFFFFC107)),
        PieChartData("مرداد", 250f, Color(0xFF4CAF50))
    )

    MaterialTheme {
        PieChart(
            data = data,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )
    }
}