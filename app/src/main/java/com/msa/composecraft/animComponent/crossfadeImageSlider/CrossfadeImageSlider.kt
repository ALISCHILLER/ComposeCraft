package com.msa.composecraft.animComponent.crossfadeImageSlider

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.msa.composecraft.R

// کامپوزبل اصلی که برای نمایش انیمیشن کراس فید استفاده می‌شود
@Composable
fun CrossfadeImageSlider(modifier: Modifier = Modifier) {
    // Scaffold برای ایجاد ساختار صفحه و مدیریت padding
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        // فراخوانی کامپوزبل ImageCrossfade که محتوای انیمیشن کراس فید را نشان می‌دهد
        ImageCrossfade(
            modifier = Modifier.padding(innerPadding)
        )
    }
}

// کامپوزبل برای نمایش انیمیشن کراس فید و جابجایی تصاویر
@Composable
fun ImageCrossfade(modifier: Modifier = Modifier) {
    // لیستی از تصاویر برای نمایش
    val imageList = listOf(
        R.drawable.image_4,
        R.drawable.image_2,
        R.drawable.image_3
    )

    // متغیرهایی برای نگهداری وضعیت انیمیشن و تصویر فعلی
    var currentIndex by remember { mutableIntStateOf(0) } // ایندکس تصویر فعلی
    var animationSpec by remember { mutableStateOf<FiniteAnimationSpec<Float>>(tween(durationMillis = 1000)) } // مشخصه انیمیشن
    var animationDuration by remember { mutableIntStateOf(1000) } // مدت زمان انیمیشن (در میلی‌ثانیه)

    // ایجاد فضای عمودی برای نمایش کامپوننت‌ها
    Column(
        horizontalAlignment = Alignment.CenterHorizontally, // چینش افقی به صورت مرکز
        verticalArrangement = Arrangement.spacedBy(16.dp), // فاصله افقی بین کامپوننت‌ها
        modifier = modifier
            .fillMaxWidth() // پهنای کامل
            .wrapContentHeight() // ارتفاع به اندازه محتوای درون آن
            .padding(16.dp) // فاصله داخلی از تمامی لبه‌ها
    ) {
        // استفاده از Crossfade برای ایجاد انیمیشن تغییر تصویر
        Crossfade(
            targetState = currentIndex, // تغییر در targetState با استفاده از currentIndex
            animationSpec = animationSpec, // استفاده از مشخصه انیمیشن برای کنترل زمان انیمیشن
            label = "" // برای نامگذاری انیمیشن (برای استفاده در حالت دیباگینگ)
        ) { index ->
            // نمایش تصویر داخل کارت با لبه‌های گرد
            Card(
                shape = RoundedCornerShape(16.dp), // لبه‌های گرد
                modifier = Modifier
                    .fillMaxWidth() // کارت پهنای کامل بگیرد
                    .aspectRatio(1f) // نسبت ابعاد کارت 1:1 باشد
            ) {
                // نمایش تصویر با استفاده از painterResource
                Image(
                    painter = painterResource(id = imageList[index]), // دریافت تصویر از لیست
                    contentDescription = "", // توضیح تصویر (برای دسترسی بهینه)
                    contentScale = ContentScale.Crop, // برش تصویر برای پر کردن فضای کارت
                    modifier = Modifier.fillMaxSize() // تصویر باید فضای کارت را پر کند
                )
            }
        }

        // نمایش مدت زمان انیمیشن به کاربر
        Text("Animation Duration: ${animationDuration}ms")

        // استفاده از Slider برای تغییر مدت زمان انیمیشن
        Slider(
            value = animationDuration.toFloat(),
            onValueChange = { animationDuration = it.toInt() }, // تغییر مدت زمان انیمیشن
            valueRange = 300f..2000f, // بازه مقدارهای انیمیشن (از 300ms تا 2000ms)
            steps = 10, // گام‌های تنظیم مقدار انیمیشن
            modifier = Modifier.fillMaxWidth() // پهنای کامل برای Slider
        )

        // استفاده از Row برای ایجاد دو دکمه ناوبری
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp), // فاصله بین دکمه‌ها
            modifier = Modifier.fillMaxWidth() // پهنای کامل برای Row
        ) {
            // دکمه برای رفتن به تصویر قبلی
            Button(
                onClick = {
                    animationSpec = tween(durationMillis = animationDuration) // تغییر مشخصه انیمیشن
                    if (currentIndex > 0) currentIndex-- // اگر ایندکس بیشتر از 0 باشد، به تصویر قبلی برو
                },
                enabled = currentIndex > 0, // دکمه فقط زمانی فعال است که تصویر قبلی موجود باشد
                modifier = Modifier.weight(1f) // دکمه‌ها با وزن مساوی برای گنجاندن فضای برابر
            ) {
                Text("<") // علامت "کوچکتر"
            }

            // دکمه برای رفتن به تصویر بعدی
            Button(
                onClick = {
                    animationSpec = tween(durationMillis = animationDuration) // تغییر مشخصه انیمیشن
                    if (currentIndex < imageList.size - 1) currentIndex++ // اگر ایندکس کمتر از تعداد تصاویر باشد، به تصویر بعدی برو
                },
                enabled = currentIndex < imageList.size - 1, // دکمه فقط زمانی فعال است که تصویر بعدی موجود باشد
                modifier = Modifier.weight(1f) // دکمه‌ها با وزن مساوی برای گنجاندن فضای برابر
            ) {
                Text(">") // علامت "بزرگتر"
            }
        }

        // دکمه برای ایجاد انتقال تصادفی با انیمیشن خاص
        Button(
            onClick = {
                animationSpec = keyframes {
                    durationMillis = animationDuration // مدت زمان انیمیشن
                    0.4f at (animationDuration / 3) // تنظیم keyframe اول
                    0.8f at (2 * animationDuration / 3) // تنظیم keyframe دوم
                }
                currentIndex = (imageList.indices).random() // انتخاب تصادفی یک ایندکس از لیست تصاویر
            },
            modifier = Modifier.fillMaxWidth() // پهنای کامل برای دکمه
        ) {
            Text("Random Transition") // متن دکمه برای ایجاد انتقال تصادفی
        }
    }
}
