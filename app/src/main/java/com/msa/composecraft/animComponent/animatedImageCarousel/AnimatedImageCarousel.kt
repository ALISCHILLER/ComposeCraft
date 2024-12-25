//package com.msa.composecraft.animatedImageCarousel
//
//import androidx.compose.animation.core.Animatable
//import androidx.compose.animation.core.FastOutSlowInEasing
//import androidx.compose.animation.core.tween
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
//import androidx.compose.material3.carousel.rememberCarouselState
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.graphicsLayer
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.unit.dp
//import com.msa.composecraft.R
//
//// کامپوزبل اصلی که یک اسلایدشو با انیمیشن مقیاس‌دهی ایجاد می‌کند
//@Composable
//fun AnimatedImageCarousel(modifier: Modifier = Modifier) {
//    // استفاده از Scaffold برای اعمال Padding در اطراف کامپوزبل
//    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//        // نمایش اسلایدشو با Padding داخلی
//        ScaleAnimatedCarousel(
//            modifier = Modifier.padding(innerPadding)
//        )
//    }
//}
//
//// کامپوزبل که اسلایدشو را با انیمیشن مقیاس‌دهی نمایش می‌دهد
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ScaleAnimatedCarousel(
//    modifier: Modifier = Modifier
//) {
//    // لیست تصاویر برای نمایش در اسلایدشو
//    val items = listOf(
//        R.drawable.image_1,
//        R.drawable.image_2,
//        R.drawable.image_3,
//        R.drawable.image_4,
//        R.drawable.image_5,
//        R.drawable.image_6
//    )
//
//    // ایجاد انیماتور برای مقیاس‌دهی تصاویر
//    val animatedScale = remember { Animatable(1f) }
//
//    // استفاده از LaunchedEffect برای شروع انیمیشن مقیاس‌دهی
//    LaunchedEffect(Unit) {
//        // اجرای انیمیشن به صورت نامحدود
//        while (true) {
//            // انیمیشن افزایش مقیاس
//            animatedScale.animateTo(
//                targetValue = 1.2f,
//                animationSpec = tween(durationMillis = 3000, easing = FastOutSlowInEasing)
//            )
//            // انیمیشن کاهش مقیاس
//            animatedScale.animateTo(
//                targetValue = 1.1f,
//                animationSpec = tween(durationMillis = 3000, easing = FastOutSlowInEasing)
//            )
//        }
//    }
//
//    // کامپوزبل اسلایدشو که تصاویر را نمایش می‌دهد
//    HorizontalMultiBrowseCarousel(
//        state = rememberCarouselState { items.size },
//        modifier = modifier,
//        preferredItemWidth = 186.dp, // عرض مطلوب هر آیتم
//        itemSpacing = 8.dp, // فاصله بین آیتم‌ها
//        contentPadding = PaddingValues(horizontal = 16.dp) // Padding در اطراف محتوای اسلایدشو
//    ) { index ->
//        // نمایش هر تصویر در اسلایدشو با انیمیشن مقیاس‌دهی
//        Image(
//            painter = painterResource(id = items[index]), // بارگذاری تصویر از لیست items
//            modifier = Modifier
//                .height(205.dp) // ارتفاع هر تصویر
//                .maskClip(MaterialTheme.shapes.extraLarge) // تنظیم ماسک برای گرد کردن گوشه‌ها
//                .graphicsLayer(
//                    scaleX = animatedScale.value, // اعمال مقیاس افقی
//                    scaleY = animatedScale.value // اعمال مقیاس عمودی
//                ),
//            contentScale = ContentScale.Crop, // تنظیم نحوه نمایش تصویر
//            contentDescription = null // توضیحات محتوا (برای دسترسی بهتر)
//        )
//    }
//}
