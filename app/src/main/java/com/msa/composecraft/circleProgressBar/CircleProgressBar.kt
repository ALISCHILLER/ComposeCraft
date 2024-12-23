package com.josephhowerton.composecanvas.common.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.msa.composecraft.ui.theme.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// کامپوننت اصلی CircleProgressIndicator برای نمایش پیشرفت به صورت دایره‌ای
@Composable
fun CircleProgressIndicator(
    progress: Double, // مقدار پیشرفت (بین ۰ تا ۱)
    strokeWidth: Float, // ضخامت خط پیشرفت
    size: Dp, // اندازه کلی کامپوننت
    progressColor: Color, // رنگ پیشرفت
    trackColor: Color, // رنگ پس‌زمینه خط
    modifier: Modifier = Modifier, // اصلاح‌کننده برای سفارشی‌سازی
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.wrapContentSize()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(5.dp) // فضای داخلی
                .size(size) // اندازه کامپوننت
        ) {
            // رسم دایره پیشرفت
            CircularProgress(
                progress = progress,
                progressColor = progressColor,
                trackColor = trackColor,
                strokeWidth = strokeWidth,
                modifier = Modifier.size(size)
            )
            // نمایش متن درصد پیشرفت
            Text(
                text = "${(progress * 100).toInt()}%", // درصد پیشرفت
                fontSize = 16.sp, // اندازه فونت
                fontWeight = FontWeight.SemiBold, // ضخامت فونت
                color = progressColor, // رنگ متن
                textAlign = TextAlign.Center, // تراز متن
                modifier = Modifier
                    .width(150.dp)
                    .padding(5.dp)
            )
        }
    }
}

// کامپوننت CircularProgress برای رسم پیشرفت و پس‌زمینه به صورت دایره‌ای
@Composable
fun CircularProgress(
    progress: Double, // مقدار پیشرفت
    strokeWidth: Float, // ضخامت خط
    progressColor: Color, // رنگ پیشرفت
    trackColor: Color, // رنگ پس‌زمینه خط
    modifier: Modifier = Modifier,
) {
    val validProgress = if (progress.isNaN()) 0.0 else progress // بررسی مقدار معتبر برای پیشرفت
    val sweepAngle = (360 * validProgress).toFloat() // زاویه دایره
    val animatedSweepAngle by remember { mutableStateOf(Animatable(sweepAngle)) } // انیمیشن زاویه

    // انیمیشن برای تغییر زاویه دایره
    LaunchedEffect(animatedSweepAngle) {
        animatedSweepAngle.animateTo(
            targetValue = sweepAngle,
            animationSpec = tween(durationMillis = 1000, easing = FastOutLinearInEasing) // تنظیم سرعت انیمیشن
        )
    }

    Canvas(modifier = modifier) {
        // رسم پس‌زمینه خط
        drawArc(
            color = trackColor,
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = false,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )

        // رسم خط پیشرفت
        drawArc(
            color = progressColor,
            startAngle = -90f,
            sweepAngle = animatedSweepAngle.value,
            useCenter = false,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )
    }
}

// کامپوننت DailyCalorieProgressIndicator برای نمایش میزان کالری مصرفی روزانه
@Composable
fun DailyCalorieProgressIndicator(
    size: Int, // اندازه کلی دایره
    breakfastProgress: Double, // میزان کالری صبحانه
    lunchProgress: Double, // میزان کالری ناهار
    snackProgress: Double, // میزان کالری میان‌وعده
    dinnerProgress: Double, // میزان کالری شام
    modifier: Modifier = Modifier,
) {
    val progress by remember { mutableDoubleStateOf(breakfastProgress + lunchProgress + snackProgress + dinnerProgress) } // مجموع پیشرفت‌ها

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier.wrapContentSize()) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(size.dp)) {
            // رسم دایره کالری روزانه
            DailyCalorieProgress(
                breakfastProgress = breakfastProgress,
                lunchProgress = lunchProgress,
                snackProgress = snackProgress,
                dinnerProgress = dinnerProgress,
                size = size,
                modifier = Modifier.size(size.dp)
            )

            // نمایش درصد پیشرفت
            Text(
                text = "${(progress * 100).toInt()}%", // محاسبه درصد
                fontSize = 16.sp, // اندازه فونت
                fontWeight = FontWeight.SemiBold, // ضخامت فونت
                color = BreakfastColor, // رنگ متن
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .width(150.dp)
                    .padding(5.dp)
            )
        }
    }
}

// کامپوننت DailyCalorieProgress برای رسم مقادیر مختلف کالری به صورت بخش‌های دایره‌ای
@Composable
private fun DailyCalorieProgress(
    modifier: Modifier = Modifier,
    breakfastProgress: Double,
    lunchProgress: Double,
    snackProgress: Double,
    dinnerProgress: Double,
    size: Int,
) {
    Column(
        modifier = modifier
            .wrapContentSize()
            .padding(2.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // محاسبه زاویه‌های مربوط به هر بخش
        val breakfastEndAngle = 360 * breakfastProgress
        val snackEndAngle = breakfastEndAngle + 360 * snackProgress
        val lunchEndAngle = snackEndAngle + 360 * lunchProgress
        val dinnerEndAngle = lunchEndAngle + 360 * dinnerProgress

        // انیمیشن‌ها برای هر بخش
        val breakfastAnimate by animateFloatAsState(
            targetValue = breakfastEndAngle.toFloat(),
            animationSpec = tween(durationMillis = 1000, easing = FastOutLinearInEasing), label = ""
        )
        val snackAnimate by animateFloatAsState(
            targetValue = snackEndAngle.toFloat(),
            animationSpec = tween(durationMillis = 1000, easing = FastOutLinearInEasing), label = ""
        )
        val lunchAnimate by animateFloatAsState(
            targetValue = lunchEndAngle.toFloat(),
            animationSpec = tween(durationMillis = 1000, easing = FastOutLinearInEasing), label = ""
        )
        val dinnerAnimate by animateFloatAsState(
            targetValue = dinnerEndAngle.toFloat(),
            animationSpec = tween(durationMillis = 1000, easing = FastOutLinearInEasing), label = ""
        )

        Canvas(modifier = Modifier.size(size.dp)) {
            // رسم پس‌زمینه
            drawArc(
                color = Gray50A,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = 20f)
            )

            // رسم هر بخش کالری
            drawArc(
                color = DinnerColor,
                startAngle = -90f + lunchEndAngle.toFloat(),
                sweepAngle = dinnerAnimate - lunchEndAngle.toFloat(),
                useCenter = false,
                style = Stroke(width = 20f)
            )

            drawArc(
                color = LunchColor,
                startAngle = -90f + snackEndAngle.toFloat(),
                sweepAngle = lunchAnimate - snackEndAngle.toFloat(),
                useCenter = false,
                style = Stroke(width = 20f)
            )

            drawArc(
                color = SnackColor,
                startAngle = -90f + breakfastEndAngle.toFloat(),
                sweepAngle = snackAnimate - breakfastEndAngle.toFloat(),
                useCenter = false,
                style = Stroke(width = 20f)
            )

            drawArc(
                color = BreakfastColor,
                startAngle = -90f,
                sweepAngle = breakfastAnimate,
                useCenter = false,
                style = Stroke(width = 20f)
            )
        }
    }
}



@Composable
@Preview(showBackground = true)
fun CircleProgressIndicatorPreview() {
    var f by remember { mutableStateOf(0.0) } // مقدار اولیه f

    // استفاده از LaunchedEffect برای به‌روزرسانی مقدار f هر 10 ثانیه
    LaunchedEffect(Unit) {
        while (true) {
            delay(2000) // 10 ثانیه تاخیر
            if (f < 1.0) { // اگر پیشرفت کمتر از 1 باشد
                f += 0.1 // اضافه کردن 0.1 به مقدار f
            }
        }
    }

    CircleProgressIndicator(
        progress = f, // استفاده از مقدار f
        strokeWidth = 20f,
        size = 100.dp,
        progressColor = FatColor,
        trackColor = FatColorTransparent
    )
}

@Composable
@Preview(showBackground = true)
private fun DailyCalorieProgressIndicatorPreview(){
    DailyCalorieProgressIndicator(
        breakfastProgress = .25,
        lunchProgress = .25,
        snackProgress = .10,
        dinnerProgress = .13,
        size = 100,
    )
}
