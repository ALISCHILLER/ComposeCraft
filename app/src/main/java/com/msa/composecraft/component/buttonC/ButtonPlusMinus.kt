@file:OptIn(ExperimentalMaterial3Api::class)
package com.msa.composecraft.component.buttonC

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.Dp
import com.msa.composecraft.ui.theme.*


// ثابت‌ها برای مدیریت انیمیشن و محدودیت‌های درگ
private const val ICON_BUTTON_ALPHA_INITIAL = 0.3f
private const val DRAG_LIMIT_HORIZONTAL_DP = 72

// Extension Function برای تبدیل Dp به پیکسل
@Composable
private fun Dp.dpToPx() = with(LocalDensity.current) { this@dpToPx.toPx() }

// کلاس کامپوزبل برای ایجاد دکمه‌های شمارنده با قابلیت‌های پیشرفته
@Composable
fun CounterButtonNew(
    value: String, // مقدار فعلی شمارنده
    onValueDecreaseClick: () -> Unit, // عملیات کاهش مقدار
    onValueIncreaseClick: () -> Unit, // عملیات افزایش مقدار
    onValue: (String) -> Unit, // عملیات برای تغییر مقدار
    onValueClearClick: () -> Unit, // عملیات پاکسازی مقدار
    modifier: Modifier = Modifier // قابلیت تنظیمات ظاهری و ابعاد
) {
    Box(
        contentAlignment = Alignment.Center, // مرکز قرار دادن محتوا
        modifier = modifier
            .width(150.dp) // عرض کادر
            .height(60.dp) // ارتفاع کادر
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically, // چینش عمودی مرکز
            horizontalArrangement = Arrangement.Center // چینش افقی مرکز
        ) {
            val interactionSource = remember { MutableInteractionSource() }
            val isPressed by interactionSource.collectIsPressedAsState()

            // دکمه کاهش مقدار
            Icon(
                imageVector = Icons.Outlined.Remove, // آیکون کاهش مقدار
                contentDescription = "دکمه کاهش مقدار",
                tint = if (isPressed) barcolorlow else barcolorlow, // رنگ آیکون
                modifier = Modifier
                    .padding(horizontal = 3.dp) // فاصله افقی
                    .size(30.dp) // اندازه آیکون
                    .clip(RoundedCornerShape(8.dp)) // شکل گوشه‌های آیکون
                    .clickable { // عملکرد کلیک کاهش مقدار
                        onValue(maxOf(value.toInt() - 1, 0).toString())
                    }
                    .border(
                        border = BorderStroke(width = 1.dp, color = barcolorlow),
                        shape = RoundedCornerShape(8.dp)
                    )
            )

            // فیلد نمایش و ویرایش مقدار
            OutlinedTextField(
                modifier = Modifier
                    .padding(horizontal = 3.dp)
                    .weight(1.0f), // استفاده از فضای باقی‌مانده
                value = value,
                onValueChange = { newValue ->
                    if (newValue.isNotEmpty())
                        onValue(newValue)
                    else
                        onValue("0") // جلوگیری از مقدار خالی
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), // نوع ورودی عددی
                textStyle = Typography.labelSmall.copy(
                    textAlign = TextAlign.Center // متن وسط‌چین
                )
            )

            // دکمه افزایش مقدار
            Icon(
                imageVector = Icons.Outlined.Add, // آیکون افزایش مقدار
                contentDescription = "دکمه افزایش مقدار",
                tint = Color.White, // رنگ آیکون
                modifier = Modifier
                    .padding(horizontal = 3.dp) // فاصله افقی
                    .size(30.dp) // اندازه آیکون
                    .clip(RoundedCornerShape(8.dp)) // شکل گوشه‌های آیکون
                    .clickable { // عملکرد کلیک افزایش مقدار
                        onValue((value.toInt() + 1).toString())
                    }
                    .background(RedMain) // رنگ پس‌زمینه آیکون
            )
        }
    }
}

// پیش‌نمایش برای بررسی عملکرد و ظاهر کامپوننت CounterButtonNew
@Preview
@Composable
private fun ButtonPlusMinus() {
    Surface(
        modifier = Modifier.fillMaxSize(), // پر کردن تمام فضای موجود
        color = MaterialTheme.colorScheme.background // رنگ پس‌زمینه بر اساس تم
    ) {
        Column(
            modifier = Modifier.wrapContentSize(), // تنظیم اندازه برای محتوای داخلی
            verticalArrangement = Arrangement.Center, // چینش عمودی مرکز
            horizontalAlignment = Alignment.CenterHorizontally // چینش افقی مرکز
        ) {
            // مدیریت مقدار شمارنده با استفاده از متغیر قابل یادآوری
            var valueCounter by remember {
                mutableStateOf(0)
            }

            // استفاده از CounterButtonNew برای نمایش و مدیریت مقدار
            CounterButtonNew(
                value = valueCounter.toString(),
                onValueIncreaseClick = {
                    valueCounter += 1 // افزایش مقدار
                },
                onValueDecreaseClick = {
                    valueCounter = maxOf(valueCounter - 1, 0) // کاهش مقدار با جلوگیری از منفی شدن
                },
                onValueClearClick = {
                    valueCounter = 0 // پاکسازی مقدار
                },
                onValue = { valueCounter = it.toInt() } // به‌روزرسانی مقدار
            )
        }
    }
}
