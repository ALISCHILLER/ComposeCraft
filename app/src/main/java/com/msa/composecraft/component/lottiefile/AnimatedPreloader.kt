package com.msa.componentcompose.ui.component.lottiefile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.msa.composecraft.R

/**
 * این کامپوننت یک صفحه بارگذاری با انیمیشن Lottie و افکت محو (Blur) ایجاد می‌کند.
 * @param modifier Modifier برای تنظیمات layout و استایل.
 */
@Composable
fun LoadingAnimate(modifier: Modifier = Modifier) {
    // پس‌زمینه با افکت محو (Blur)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                renderEffect = BlurEffect(90f, 90f) // تنظیم شعاع افکت محو
            }
            .background(Color.White.copy(alpha = 0.5f)) // رنگ پس‌زمینه با شفافیت
    )

    // محتوای پیش‌زمینه (انیمیشن Lottie)
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        AnimatedPreloader(
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.Center)
        )
    }
}

/**
 * این کامپوننت یک انیمیشن Lottie برای نمایش پیش‌بارگذاری ایجاد می‌کند.
 * @param modifier Modifier برای تنظیمات layout و استایل.
 */
@Composable
fun AnimatedPreloader(modifier: Modifier = Modifier) {
    // بارگذاری فایل Lottie
    val preloaderLottieComposition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.loading)
    )

    // کنترل پیشرفت انیمیشن
    val preloaderProgress by animateLottieCompositionAsState(
        composition = preloaderLottieComposition,
        iterations = LottieConstants.IterateForever, // تکرار بی‌نهایت
        isPlaying = true // انیمیشن همیشه در حال اجرا باشد
    )

    // نمایش انیمیشن Lottie
    LottieAnimation(
        composition = preloaderLottieComposition,
        progress = preloaderProgress,
        modifier = modifier.size(200.dp)
    )
}

/**
 * Preview برای کامپوننت LoadingAnimate و AnimatedPreloader.
 */
@Preview
@Composable
private fun PreviewLoadingAnimate() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Box {
            AnimatedPreloader(
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.Center)
            )
        }
    }
}