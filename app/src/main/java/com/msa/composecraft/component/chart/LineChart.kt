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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

// کلاس داده‌ای برای نگهداری نقاط داده
data class DataPoint(
    val x: Float, // مقدار محور X
    val y: Float // مقدار محور Y
)

// کامپوننت اصلی برای نمایش نمودار خطی
@Composable
fun LineChart(
    dataPoints: List<DataPoint>, // لیست نقاط داده
    modifier: Modifier = Modifier,
    lineColor: Color = MaterialTheme.colorScheme.primary, // رنگ خط نمودار
    pointColor: Color = MaterialTheme.colorScheme.secondary, // رنگ نقاط داده
    axisColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f), // رنگ محورها
    textColor: Color = MaterialTheme.colorScheme.onSurface, // رنگ متن
    animationDuration: Int = 1000 // مدت زمان انیمیشن
) {
    // حالت برای انیمیشن نمودار
    val animationProgress = remember { Animatable(0f) }
    LaunchedEffect(dataPoints) {
        animationProgress.animateTo(1f, animationSpec = tween(durationMillis = animationDuration))
    }

    // اندازه‌گیری متن برای نمایش مقادیر محورها
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
            val xScale = size.width / (dataPoints.maxOf { it.x } - dataPoints.minOf { it.x })
            val yScale = size.height / (dataPoints.maxOf { it.y } - dataPoints.minOf { it.y })

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

            // ترسیم مقادیر محور X
            dataPoints.forEach { point ->
                val xPos = (point.x - dataPoints.minOf { it.x }) * xScale
                val textLayoutResult = textMeasurer.measure(
                    text = point.x.toString(),
                    style = TextStyle(color = textColor, fontSize = 12.sp)
                )
                drawText(
                    textLayoutResult = textLayoutResult,
                    topLeft = Offset(xPos, size.height + 8.dp.toPx())
                )
            }

            // ترسیم مقادیر محور Y
            dataPoints.forEach { point ->
                val yPos = size.height - (point.y - dataPoints.minOf { it.y }) * yScale
                val textLayoutResult = textMeasurer.measure(
                    text = point.y.toString(),
                    style = TextStyle(color = textColor, fontSize = 12.sp)
                )
                drawText(
                    textLayoutResult = textLayoutResult,
                    topLeft = Offset(-32.dp.toPx(), yPos)
                )
            }

            // ترسیم خطوط اتصال نقاط داده
            val path = Path()
            dataPoints.forEachIndexed { index, point ->
                val xPos = (point.x - dataPoints.minOf { it.x }) * xScale
                val yPos = size.height - (point.y - dataPoints.minOf { it.y }) * yScale
                if (index == 0) {
                    path.moveTo(xPos, yPos)
                } else {
                    path.lineTo(xPos, yPos)
                }
            }
            drawPath(
                path = path,
                color = lineColor,
                style = Stroke(width = 4f, cap = StrokeCap.Round)
            )

            // ترسیم نقاط داده
            dataPoints.forEach { point ->
                val xPos = (point.x - dataPoints.minOf { it.x }) * xScale
                val yPos = size.height - (point.y - dataPoints.minOf { it.y }) * yScale
                drawCircle(
                    color = pointColor,
                    radius = 8f,
                    center = Offset(xPos, yPos)
                )
            }
        }
    }
}

// پیش‌نمایش: نمایش نمودار خطی
@Preview(showBackground = true)
@Composable
fun PreviewLineChart() {
    val dataPoints = listOf(
        DataPoint(1f, 10f),
        DataPoint(2f, 20f),
        DataPoint(3f, 15f),
        DataPoint(4f, 30f),
        DataPoint(5f, 25f)
    )

    MaterialTheme {
        LineChart(
            dataPoints = dataPoints,
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
        )
    }
}