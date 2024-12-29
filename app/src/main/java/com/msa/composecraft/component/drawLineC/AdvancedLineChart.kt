package com.msa.composecraft.component.drawLineC

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

/**
 * یک کامپوننت پیشرفته برای رسم خطوط شکسته با انیمیشن‌های نرم و سفارشی‌سازی‌های گسترده.
 *
 * @param modifier Modifier برای تنظیمات layout و استایل.
 * @param points لیست نقاط برای رسم خطوط.
 * @param minPoint حداقل نقطه برای مقیاس‌گذاری (اختیاری).
 * @param maxPoint حداکثر نقطه برای مقیاس‌گذاری (اختیاری).
 * @param style سبک رسم خطوط.
 * @param animationSpec مشخصات انیمیشن برای رسم خطوط.
 * @param onDrawComplete Callback برای زمانی که رسم خطوط کامل می‌شود.
 */
@Composable
fun AdvancedLineChart(
    modifier: Modifier,
    points: List<Float>,
    minPoint: Float? = null,
    maxPoint: Float? = null,
    style: LineChartStyle,
    animationSpec: AnimationSpec<Float> = tween(durationMillis = 1000, easing = LinearEasing),
    onDrawComplete: () -> Unit = {},
) {
    require(points.size > 1) { "حداقل دو نقطه برای رسم خطوط لازم است." }

    var size by remember { mutableStateOf(IntSize.Zero) }
    val animatedProgress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        animatedProgress.animateTo(1f, animationSpec)
        onDrawComplete()
    }

    Canvas(
        modifier = modifier
            .onSizeChanged { size = it }
            .background(Color.White),
        onDraw = {
            if (size != IntSize.Zero) {
                drawLineChart(
                    size = size,
                    points = points,
                    fixedMinPoint = minPoint,
                    fixedMaxPoint = maxPoint,
                    style = style,
                    progress = animatedProgress.value,
                )
            }
        },
    )
}

/**
 * تابع داخلی برای رسم خطوط شکسته.
 *
 * @param size اندازه Canvas.
 * @param points لیست نقاط برای رسم خطوط.
 * @param fixedMinPoint حداقل نقطه برای مقیاس‌گذاری (اختیاری).
 * @param fixedMaxPoint حداکثر نقطه برای مقیاس‌گذاری (اختیاری).
 * @param style سبک رسم خطوط.
 * @param progress پیشرفت انیمیشن (بین 0 تا 1).
 */
private fun DrawScope.drawLineChart(
    size: IntSize,
    points: List<Float>,
    fixedMinPoint: Float?,
    fixedMaxPoint: Float?,
    style: LineChartStyle,
    progress: Float,
) {
    val maxPoint = fixedMaxPoint ?: points.maxOrNull() ?: 0f
    val minPoint = fixedMinPoint ?: points.minOrNull() ?: 0f
    val total = maxPoint - minPoint
    val height = size.height
    val width = size.width
    val xSpacing = width / (points.size - 1F)
    val path = Path()

    for (index in points.indices) {
        val x = index * xSpacing
        val y = height - height * ((points[index] - minPoint) / total)
        if (index == 0) {
            path.moveTo(x, y)
        } else {
            val prevX = (index - 1) * xSpacing
            val prevY = height - height * ((points[index - 1] - minPoint) / total)
            path.lineTo(prevX + (x - prevX) * progress, prevY + (y - prevY) * progress)
        }
    }

    when (style) {
        is LineChartStyle.LineStroke -> {
            drawPath(
                path = path,
                brush = style.brush,
                style = style.stroke,
            )
        }

        is LineChartStyle.DashedLineStroke -> {
            drawPath(
                path = path,
                brush = style.brush,
                style = Stroke(
                    width = style.stroke.width,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 5f), 0f)
                ),
            )
        }
    }
}

/**
 * سبک‌های مختلف برای رسم خطوط شکسته.
 */
sealed class LineChartStyle {

    /**
     * سبک رسم خطوط ساده.
     *
     * @param brush براش برای رسم خطوط.
     * @param stroke استایل خطوط.
     */
    class LineStroke(
        val brush: Brush,
        val stroke: Stroke,
    ) : LineChartStyle()

    /**
     * سبک رسم خطوط نقطه‌چین.
     *
     * @param brush براش برای رسم خطوط.
     * @param stroke استایل خطوط.
     */
    class DashedLineStroke(
        val brush: Brush,
        val stroke: Stroke,
    ) : LineChartStyle()
}

@Preview
@Composable
private fun AdvancedLineChartPreview() {
    val points = listOf(10f, 20f, 30f, 40f, 50f)
    val brush = Brush.linearGradient(listOf(Color.Red, Color.Blue))

    AdvancedLineChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        points = points,
        style = LineChartStyle.LineStroke(brush, Stroke(width = 4f)),
        onDrawComplete = { println("خطوط رسم شد!") }
    )
}