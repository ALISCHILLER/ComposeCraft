@file:OptIn(ExperimentalMaterial3Api::class)

package com.msa.composecraft.component.editText



import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// انواع نمایش برای فیلد OTP
const val OTP_VIEW_TYPE_NONE = 0
const val OTP_VIEW_TYPE_UNDERLINE = 1
const val OTP_VIEW_TYPE_BORDER = 2

// کامپوزبل اصلی برای نمایش فیلد OTP
@Composable
fun OtpView(
    otpText: String, // متن وارد شده OTP
    modifier: Modifier = Modifier, // تغییرات نمای کلی
    charColor: Color = Color.Black, // رنگ متن
    containerColor: Color = charColor, // رنگ پس‌زمینه خانه‌ها
    selectedContainerColor: Color = charColor, // رنگ انتخابی پس‌زمینه خانه‌ها
    charBackground: Color = Color.Transparent, // پس‌زمینه کاراکترها
    charSize: TextUnit = 16.sp, // اندازه کاراکتر
    containerSize: Dp = charSize.value.dp * 2, // اندازه خانه‌ها
    containerRadius: Dp = 4.dp, // شعاع گوشه‌های خانه‌ها
    containerSpacing: Dp = 4.dp, // فاصله بین خانه‌ها
    otpCount: Int = 4, // تعداد خانه‌ها برای OTP
    type: Int = OTP_VIEW_TYPE_UNDERLINE, // نوع نمایش خانه‌ها (آندرلاین یا مرزی)
    enabled: Boolean = true, // فعال یا غیرفعال بودن فیلد
    password: Boolean = false, // آیا فیلد پسورد است؟
    passwordChar: String = "", // کاراکتر پسورد (مثل "•")
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), // تنظیمات کیبورد
    onOtpTextChange: (String) -> Unit // تابع برای بروزرسانی متن OTP
) {
    BasicTextField(
        modifier = modifier,
        value = otpText,
        onValueChange = {
            // محدود کردن طول متن OTP به تعداد مشخص شده
            if (it.length <= otpCount) {
                onOtpTextChange.invoke(it)
            }
        },
        enabled = enabled,
        keyboardOptions = keyboardOptions,
        decorationBox = {
            // ایجاد ردیف از خانه‌های OTP
            Row(horizontalArrangement = Arrangement.spacedBy(containerSpacing)) {
                repeat(otpCount) { index ->
                    CharView(
                        index = index,
                        otpCount = otpCount,
                        text = otpText,
                        charColor = charColor,
                        containerColor = containerColor,
                        highlightColor = selectedContainerColor,
                        charSize = charSize,
                        containerRadius = containerRadius,
                        containerSize = containerSize,
                        type = type,
                        charBackground = charBackground,
                        password = password,
                        passwordChar = passwordChar,
                    )
                }
            }
        }
    )
}

// کامپوزبل برای نمایش هر خانه OTP
@Composable
private fun CharView(
    index: Int,
    otpCount: Int,
    text: String,
    charColor: Color,
    highlightColor: Color,
    containerColor: Color,
    charSize: TextUnit,
    containerSize: Dp,
    containerRadius: Dp,
    type: Int = OTP_VIEW_TYPE_UNDERLINE,
    charBackground: Color = Color.Transparent,
    password: Boolean = false,
    passwordChar: String = ""
) {

    // تغییر رنگ پس‌زمینه بر اساس انتخاب خانه
    val containerColor2 =
        if (index == text.length || (index == otpCount - 1 && text.length == otpCount)) highlightColor else containerColor

    // اعمال تغییرات برای نمایش هر خانه
    val modifier = if (type == OTP_VIEW_TYPE_BORDER) {
        Modifier
            .size(containerSize)
            .border(
                width = 1.dp,
                color = containerColor2,
                shape = RoundedCornerShape(containerRadius)
            )
            .padding(bottom = 4.dp)
            .clip(RoundedCornerShape(containerRadius))
            .background(charBackground)
    } else Modifier
        .width(containerSize)
        .background(charBackground)

    // نمایش هر خانه به همراه کاراکتر OTP
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        val char = when {
            index >= text.length -> "" // اگر خانه خالی است
            password -> passwordChar // نمایش کاراکتر پسورد
            else -> text[index].toString() // نمایش کاراکتر عادی
        }
        Text(
            text = char,
            color = charColor,
            modifier = modifier.wrapContentHeight(),
            style = MaterialTheme.typography.bodyLarge,
            fontSize = charSize,
            textAlign = TextAlign.Center,
        )
        // نمایش آندرلاین در صورتی که نوع نمایش آندرلاین باشد
        if (type == OTP_VIEW_TYPE_UNDERLINE) {
            Spacer(modifier = Modifier.height(2.dp))
            Box(
                modifier = Modifier
                    .background(charColor)
                    .height(1.dp)
                    .width(containerSize)
            )
        }
    }
}

// پیش‌نمایش از فیلد OTP
@Preview
@Composable
private fun OtpFieldPreview() {
    var otpValue by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // فیلدهای OTP
        OtpView(
            otpText = otpValue,
            onOtpTextChange = {
                otpValue = it
                Log.d("otpValue Value", otpValue)
            },
            type = OTP_VIEW_TYPE_BORDER,
            password = true,
            containerSize = 48.dp,
            passwordChar = "•",
            otpCount = 5,
            containerRadius = 15.dp,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            charColor = Color.Black
        )

        // دکمه تایید OTP
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { /* Handle OTP Verification */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Verify")
        }
    }
}
