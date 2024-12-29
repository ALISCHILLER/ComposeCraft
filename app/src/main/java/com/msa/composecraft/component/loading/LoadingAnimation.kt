package com.msa.composecraft.component.loading

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

/**
 * این کامپوننت یک انیمیشن بارگذاری با سه دایره متحرک ایجاد می‌کند.
 * @param modifier Modifier برای تنظیمات layout و استایل.
 * @param circleSize اندازه هر دایره.
 * @param circleColor رنگ دایره‌ها.
 * @param spaceBetween فاصله بین دایره‌ها.
 * @param travelDistance مسافتی که دایره‌ها در انیمیشن طی می‌کنند.
 */
@Composable
fun LoadingAnimation(
    modifier: Modifier = Modifier,
    circleSize: Dp = 25.dp,
    circleColor: Color = Color.Red,
    spaceBetween: Dp = 10.dp,
    travelDistance: Dp = 20.dp
) {
    // ایجاد سه Animatable برای کنترل انیمیشن هر دایره
    val circles = listOf(
        remember { Animatable(initialValue = 0f) },
        remember { Animatable(initialValue = 0f) },
        remember { Animatable(initialValue = 0f) }
    )

    // شروع انیمیشن برای هر دایره با تأخیر
    circles.forEachIndexed { index, animatable ->
        LaunchedEffect(key1 = animatable) {
            delay(index * 100L) // تأخیر برای ایجاد اثر موجی
            animatable.animateTo(
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = keyframes {
                        durationMillis = 1200 // مدت زمان کل انیمیشن
                        0.0f at 0 with LinearOutSlowInEasing // شروع از پایین
                        1.0f at 300 with LinearOutSlowInEasing // رسیدن به بالا
                        0.0f at 600 with LinearOutSlowInEasing // بازگشت به پایین
                        0.0f at 1200 with LinearOutSlowInEasing // توقف در پایین
                    },
                    repeatMode = RepeatMode.Restart // تکرار انیمیشن
                )
            )
        }
    }

    // دریافت مقادیر انیمیشن هر دایره
    val circleValues = circles.map { it.value }
    // تبدیل مسافت به پیکسل
    val distance = with(LocalDensity.current) { travelDistance.toPx() }

    // ردیف برای نمایش دایره‌ها
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spaceBetween)
    ) {
        circleValues.forEach { value ->
            Box(
                modifier = Modifier
                    .size(circleSize)
                    .graphicsLayer {
                        // اعمال انیمیشن حرکت عمودی
                        translationY = -value * distance
                    }
                    .background(
                        color = circleColor,
                        shape = CircleShape
                    )
            )
        }
    }
}

/**
 * Preview برای کامپوننت LoadingAnimation.
 */
@Preview
@Composable
fun PreviewLoadingAnimation() {
    LoadingAnimation(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        circleSize = 30.dp,
        circleColor = Color.Blue,
        spaceBetween = 15.dp,
        travelDistance = 25.dp
    )
}