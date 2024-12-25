package com.msa.composecraft.animComponent.verticalSliderHandle

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp



// این فایل شامل یک کامپوزابل برای مدیریت یک اسلایدر عمودی است که قابلیت تعامل کاربر را دارد.

// کامپوزابل اصلی که رابط کاربری اسلایدر را مدیریت می‌کند.
@Composable
fun VerticalSliderHandle(modifier: Modifier = Modifier) {
    // استفاده از Scaffold برای ساختار کلی صفحه
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        // نمایش کنترل اسلایدر
        SliderControl(
            modifier = Modifier.padding(innerPadding)
        )
    }
}

// کامپوزابل برای نمایش اسلایدر و کنترل مقادیر آن
@Composable
fun SliderControl(modifier: Modifier = Modifier) {
    // ذخیره مقدار فعلی اسلایدر در متغیر قابل مشاهده
    var valueLevel by remember { mutableIntStateOf(50) }
    val isMaxValue = valueLevel == 100

    // انیمیشن برای تغییر مقیاس در هنگام رسیدن به مقدار حداکثر
    val stretchScale by animateFloatAsState(
        targetValue = if (isMaxValue) 1.3f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessLow, dampingRatio = 0.5f)
    )
    val bounceScale by animateFloatAsState(
        targetValue = if (isMaxValue) 1.1f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMedium, dampingRatio = 0.8f)
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F7)) // رنگ پس‌زمینه
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // نمایش مقدار فعلی اسلایدر
            Text(
                text = "$valueLevel%",
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFF333333)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ردیف اسلایدرها با رنگ‌ها و استایل‌های مختلف
            Row {
                // اسلایدر استاندارد
                Slider(
                    currentValue = valueLevel,
                    stretchScale = stretchScale,
                    bounceScale = bounceScale,
                    onValueChange = { delta ->
                        valueLevel = (valueLevel + delta).coerceIn(0, 100) // محدود کردن مقادیر بین 0 و 100
                    }
                )

                // اسلایدر با رنگ‌های سفارشی
                Slider(
                    currentValue = valueLevel,
                    stretchScale = stretchScale,
                    bounceScale = bounceScale,
                    onValueChange = { delta ->
                        valueLevel = (valueLevel + delta).coerceIn(0, 100)
                    },
                    sliderColors = listOf(Color(0xFFFFA726), Color(0xFFFF7043)),
                    handleColors = listOf(Color(0xFFFFD54F), Color(0xFFFFA726))
                )

                // اسلایدر با رنگ‌های قرمز
                Slider(
                    currentValue = valueLevel,
                    stretchScale = stretchScale,
                    bounceScale = bounceScale,
                    onValueChange = { delta ->
                        valueLevel = (valueLevel + delta).coerceIn(0, 100)
                    },
                    sliderColors = listOf(Color(0xFFFFCDD2), Color(0xFFEF5350)),
                    handleColors = listOf(Color(0xFFFF5252), Color(0xFFD32F2F))
                )

                // اسلایدر با گرادیان رنگی چندتایی
                Slider(
                    currentValue = valueLevel,
                    stretchScale = stretchScale,
                    bounceScale = bounceScale,
                    onValueChange = { delta ->
                        valueLevel = (valueLevel + delta).coerceIn(0, 100)
                    },
                    sliderColors = listOf(Color.Red, Color.Yellow, Color.Green, Color.Blue),
                    handleColors = listOf(Color.Cyan, Color.Magenta)
                )
            }
        }
    }
}

// کامپوزابل برای ساخت یک اسلایدر با قابلیت‌های سفارشی
@Composable
fun Slider(
    currentValue: Int, // مقدار فعلی اسلایدر
    stretchScale: Float, // انیمیشن مقیاس ارتفاع
    bounceScale: Float, // انیمیشن مقیاس عرض
    onValueChange: (Int) -> Unit, // تابع تغییر مقدار
    sliderColors: List<Color> = listOf(Color(0xFF76C7C0), Color(0xFF50B8B3)), // رنگ‌های گرادیان اسلایدر
    handleColors: List<Color> = listOf(Color(0xFF76C7C0), Color(0xFF50B8B3)) // رنگ‌های گرادیان دستگیره
) {
    Box(
        modifier = Modifier
            .height(200.dp * stretchScale) // ارتفاع اسلایدر با انیمیشن
            .width(60.dp * bounceScale) // عرض اسلایدر با انیمیشن
            .background(
                color = Color(0xFFEFEFEF),
                shape = MaterialTheme.shapes.medium
            )
            .padding(8.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        // قسمت پرشده اسلایدر بر اساس مقدار
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(currentValue / 100f) // محاسبه نسبت پر شدن
                .clip(MaterialTheme.shapes.medium)
                .background(
                    brush = Brush.verticalGradient(
                        colors = sliderColors
                    )
                )
        )

        // افکت خاص برای مقدار حداکثر
        if (currentValue == 100) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .align(Alignment.TopCenter)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color(0x88FFFFFF))
                        )
                    )
            )
        }

        // دستگیره قابل تعامل اسلایدر
        Box(
            modifier = Modifier
                .size(40.dp * bounceScale) // اندازه دستگیره با انیمیشن
                .clip(CircleShape)
                .background(
                    brush = Brush.verticalGradient(
                        colors = handleColors
                    )
                )
                .pointerInput(Unit) {
                    // مدیریت حرکت کشیدن دستگیره
                    detectDragGestures { change, dragAmount ->
                        change.consume() // جلوگیری از دریافت رویداد توسط دیگر اجزا
                        val delta = (dragAmount / 5f).y.toInt()
                        onValueChange(-delta) // تغییر مقدار
                    }
                }
        )
    }
}
