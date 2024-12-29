package com.msa.composecraft.component.drawLineC
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

/**
 * یک کامپوننت برای رسم منحنی Bezier.
 *
 * @param modifier Modifier برای تنظیمات layout و استایل.
 * @param points لیست نقاط برای رسم منحنی.
 * @param minPoint حداقل نقطه برای مقیاس‌گذاری (اختیاری).
 * @param maxPoint حداکثر نقطه برای مقیاس‌گذاری (اختیاری).
 * @param style سبک رسم منحنی (پر کردن، خطوط یا هر دو).
 */
@Composable
fun BezierCurve(
    modifier: Modifier,
    points: List<Float>,
    minPoint: Float? = null,
    maxPoint: Float? = null,
    style: BezierCurveStyle,
) {
    require(points.size > 1) { "حداقل دو نقطه برای رسم منحنی لازم است." }

    var size by remember { mutableStateOf(IntSize.Zero) }

    Canvas(
        modifier = modifier.onSizeChanged { size = it },
        onDraw = {
            if (size != IntSize.Zero) {
                drawBezierCurve(
                    size = size,
                    points = points,
                    fixedMinPoint = minPoint,
                    fixedMaxPoint = maxPoint,
                    style = style,
                )
            }
        },
    )
}

/**
 * تابع داخلی برای رسم منحنی Bezier.
 *
 * @param size اندازه Canvas.
 * @param points لیست نقاط برای رسم منحنی.
 * @param fixedMinPoint حداقل نقطه برای مقیاس‌گذاری (اختیاری).
 * @param fixedMaxPoint حداکثر نقطه برای مقیاس‌گذاری (اختیاری).
 * @param style سبک رسم منحنی.
 */
private fun DrawScope.drawBezierCurve(
    size: IntSize,
    points: List<Float>,
    fixedMinPoint: Float?,
    fixedMaxPoint: Float?,
    style: BezierCurveStyle,
) {
    val maxPoint = fixedMaxPoint ?: points.maxOrNull() ?: 0f
    val minPoint = fixedMinPoint ?: points.minOrNull() ?: 0f
    val total = maxPoint - minPoint
    val height = size.height
    val width = size.width
    val xSpacing = width / (points.size - 1F)
    var lastPoint: Offset? = null
    val path = Path()
    var firstPoint = Offset(0F, 0F)

    for (index in points.indices) {
        val x = index * xSpacing
        val y = height - height * ((points[index] - minPoint) / total)
        if (lastPoint != null) {
            buildCurveLine(path, lastPoint, Offset(x, y))
        }
        lastPoint = Offset(x, y)
        if (index == 0) {
            path.moveTo(x, y)
            firstPoint = Offset(x, y)
        }
    }

    fun closeWithBottomLine() {
        path.lineTo(width.toFloat(), height.toFloat())
        path.lineTo(0F, height.toFloat())
        path.lineTo(firstPoint.x, firstPoint.y)
    }

    when (style) {
        is BezierCurveStyle.Fill -> {
            closeWithBottomLine()
            drawPath(
                path = path,
                style = Fill,
                brush = style.brush,
            )
        }

        is BezierCurveStyle.CurveStroke -> {
            drawPath(
                path = path,
                brush = style.brush,
                style = style.stroke,
            )
        }

        is BezierCurveStyle.StrokeAndFill -> {
            drawPath(
                path = path,
                brush = style.strokeBrush,
                style = style.stroke,
            )
            closeWithBottomLine()
            drawPath(
                path = path,
                brush = style.fillBrush,
                style = Fill,
            )
        }
    }
}

/**
 * تابع داخلی برای ساخت خطوط منحنی Bezier.
 *
 * @param path مسیر رسم.
 * @param startPoint نقطه شروع.
 * @param endPoint نقطه پایان.
 */
private fun buildCurveLine(path: Path, startPoint: Offset, endPoint: Offset) {
    val firstControlPoint = Offset(
        x = startPoint.x + (endPoint.x - startPoint.x) / 2F,
        y = startPoint.y,
    )
    val secondControlPoint = Offset(
        x = startPoint.x + (endPoint.x - startPoint.x) / 2F,
        y = endPoint.y,
    )
    path.cubicTo(
        x1 = firstControlPoint.x,
        y1 = firstControlPoint.y,
        x2 = secondControlPoint.x,
        y2 = secondControlPoint.y,
        x3 = endPoint.x,
        y3 = endPoint.y,
    )
}

/**
 * سبک‌های مختلف برای رسم منحنی Bezier.
 */
sealed class BezierCurveStyle {

    /**
     * سبک پر کردن منحنی.
     *
     * @param brush براش برای پر کردن منحنی.
     */
    class Fill(val brush: Brush) : BezierCurveStyle()

    /**
     * سبک رسم خطوط منحنی.
     *
     * @param brush براش برای رسم خطوط.
     * @param stroke استایل خطوط.
     */
    class CurveStroke(
        val brush: Brush,
        val stroke: Stroke,
    ) : BezierCurveStyle()

    /**
     * سبک ترکیبی: هم خطوط و هم پر کردن منحنی.
     *
     * @param fillBrush براش برای پر کردن منحنی.
     * @param strokeBrush براش برای رسم خطوط.
     * @param stroke استایل خطوط.
     */
    class StrokeAndFill(
        val fillBrush: Brush,
        val strokeBrush: Brush,
        val stroke: Stroke,
    ) : BezierCurveStyle()
}


@Preview
@Composable
private fun BezierCurvePreview() {
    val points = listOf(10f, 20f, 30f, 40f, 50f)
    val brush = Brush.linearGradient(listOf(Color.Red, Color.Blue))

    BezierCurve(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        points = points,
        style = BezierCurveStyle.Fill(brush)
    )
}


