package com.msa.composecraft.component.otp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun OtpInputField(
    modifier: Modifier = Modifier,
    otpText: String,
    otpLength: Int = 6,
    onOtpModified: (String, Boolean) -> Unit
) {
    LaunchedEffect(Unit) {
        if (otpText.length > otpLength) {
            throw IllegalArgumentException("OTP should be $otpLength digits")
        }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // راهنمای تعداد خونه‌ها
        Text(
            text = "کد $otpLength رقمی را وارد کنید",
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        BasicTextField(
            value = TextFieldValue(otpText, selection = TextRange(otpText.length)),
            onValueChange = {
                if (it.text.length <= otpLength) {
                    onOtpModified(it.text, it.text.length == otpLength)
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword,
                imeAction = ImeAction.Done
            ),
            decorationBox = {
                Row(horizontalArrangement = Arrangement.Center) {
                    repeat(otpLength) { index ->
                        CharacterContainer(
                            index = index,
                            text = otpText,
                            isFocused = index == otpText.length
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }
        )
    }
}

@Composable
private fun CharacterContainer(
    index: Int,
    text: String,
    isFocused: Boolean
) {
    val character = if (index < text.length) text[index].toString() else ""
    val cursorVisible = remember { mutableStateOf(true) }

    LaunchedEffect(isFocused) {
        if (isFocused) {
            while (true) {
                delay(800) // سرعت چشمک‌زدن کرسر
                cursorVisible.value = !cursorVisible.value
            }
        }
    }

    Box(
        modifier = Modifier
            .size(48.dp)
            .border(
                width = if (isFocused) 2.dp else 1.dp,
                color = if (isFocused) Color.Blue else Color.Gray,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = character,
            style = MaterialTheme.typography.headlineMedium,
            color = Color.Black
        )

        // نمایش کرسر در حالت فوکوس
        AnimatedVisibility(visible = isFocused && cursorVisible.value) {
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .height(24.dp)
                    .background(Color.Blue)
            )
        }
    }
}