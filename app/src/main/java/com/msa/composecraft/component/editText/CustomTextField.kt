package com.msa.composecraft.component.editText

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * یک فیلد متنی سفارشی با قابلیت‌های متنوع و پیشرفته.
 *
 * @param value مقدار متن ورودی.
 * @param onValueChange Callback برای تغییر مقدار متن.
 * @param modifier Modifier برای تنظیمات layout و استایل.
 * @param label برچسب فیلد (اختیاری).
 * @param placeholder متن placeholder (اختیاری).
 * @param icon آیکون فیلد (اختیاری).
 * @param errorMessage پیام خطا (اختیاری).
 * @param isError وضعیت خطا (true/false).
 * @param enabled وضعیت فعال/غیرفعال فیلد.
 * @param keyboardOptions تنظیمات کیبورد (مانند نوع کیبورد).
 * @param keyboardActions اقدامات کیبورد (مانند دکمه انجام).
 * @param singleLine آیا فیلد باید تک خطی باشد.
 * @param maxLines حداکثر تعداد خطوط (برای فیلدهای چندخطی).
 * @param shape شکل فیلد (پیش‌فرض: گوشه‌های گرد).
 * @param backgroundColor رنگ پس‌زمینه فیلد (اگر گرادیان تنظیم نشده باشد).
 * @param gradient گرادیان پس‌زمینه فیلد (اختیاری).
 * @param textColor رنگ متن ورودی.
 * @param placeholderColor رنگ placeholder.
 * @param errorColor رنگ خطا.
 * @param borderStroke حاشیه فیلد (اختیاری).
 * @param elevation ارتفاع سایه فیلد.
 * @param leadingIcon آیکون ابتدایی (اختیاری).
 * @param trailingIcon آیکون انتهایی (اختیاری).
 * @param isPassword آیا فیلد برای رمز عبور است.
 * @param maxLength حداکثر طول متن ورودی (اختیاری).
 * @param imeAction عمل IME (مانند Done, Next, Search).
 * @param textAlign تراز متن (چپ، راست، وسط).
 */
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    icon: ImageVector? = null,
    errorMessage: String? = null,
    isError: Boolean = false,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    maxLines: Int = 1,
    shape: Shape = RoundedCornerShape(8.dp),
    backgroundColor: Color = Color.White,
    gradient: Brush? = null,
    textColor: Color = Color.Black,
    placeholderColor: Color = Color.Gray,
    errorColor: Color = Color.Red,
    borderStroke: BorderStroke? = null,
    elevation: Dp = 4.dp,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isPassword: Boolean = false,
    maxLength: Int? = null,
    imeAction: ImeAction = ImeAction.Default,
    textAlign: TextAlign = TextAlign.Start,
) {
    var isFocused by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    val animatedElevation by animateDpAsState(
        targetValue = if (isFocused) elevation * 1.5f else elevation,
        animationSpec = tween(durationMillis = 300)
    )

    val backgroundBrush = gradient ?: Brush.linearGradient(
        colors = listOf(backgroundColor, backgroundColor)
    )

    val visualTransformation = if (isPassword && !passwordVisible) {
        PasswordVisualTransformation()
    } else {
        VisualTransformation.None
    }

    val trailingIconView = @Composable {
        if (isPassword) {
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = if (passwordVisible) "Hide Password" else "Show Password"
                )
            }
        } else {
            trailingIcon?.invoke()
        }
    }

    val layoutDirection = LocalLayoutDirection.current

    Surface(
        modifier = modifier
            .clip(shape)
            .onFocusChanged { isFocused = it.isFocused },
        shape = shape,
        color = Color.Transparent,
        shadowElevation = animatedElevation,
        border = borderStroke,
    ) {
        Column(
            modifier = Modifier
                .background(backgroundBrush, shape)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            label?.let {
                Text(
                    text = it,
                    color = if (isError) errorColor else textColor,
                    fontSize = 14.sp,
                    modifier = Modifier.alpha(if (isFocused || value.isNotEmpty()) 1f else 0.5f)
                )
            }

            TextField(
                value = value,
                onValueChange = { newValue ->
                    if (maxLength == null || newValue.length <= maxLength) {
                        onValueChange(newValue)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = enabled,
                singleLine = singleLine,
                maxLines = maxLines,
                textStyle = TextStyle(color = textColor, textAlign = textAlign),
                placeholder = placeholder?.let {
                    {
                        Text(
                            text = it,
                            color = placeholderColor,
                            fontSize = 14.sp,
                            textAlign = textAlign
                        )
                    }
                },
                leadingIcon = leadingIcon,
                trailingIcon = { trailingIconView() },
                visualTransformation = visualTransformation,
                keyboardOptions = keyboardOptions.copy(imeAction = imeAction),
                keyboardActions = keyboardActions,
                isError = isError,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                )
            )

            errorMessage?.let {
                if (isError) {
                    Text(
                        text = it,
                        color = errorColor,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }

            maxLength?.let {
                Text(
                    text = "${value.length}/$it",
                    color = if (value.length > it) errorColor else textColor,
                    fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CustomTextFieldPreview() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // فیلد متنی ساده
                CustomTextField(
                    value = "",
                    onValueChange = {},
                    label = "نام کاربری",
                    placeholder = "نام کاربری خود را وارد کنید",
                )

                // فیلد متنی با آیکون و خطا
                CustomTextField(
                    value = "",
                    onValueChange = {},
                    label = "رمز عبور",
                    placeholder = "رمز عبور خود را وارد کنید",
                    isPassword = true,
                    isError = true,
                    errorMessage = "رمز عبور باید حداقل ۸ کاراکتر باشد",
                )

                // فیلد متنی غیرفعال
                CustomTextField(
                    value = "",
                    onValueChange = {},
                    label = "ایمیل",
                    placeholder = "ایمیل خود را وارد کنید",
                    enabled = false,
                )

                // فیلد متنی با گرادیان
                CustomTextField(
                    value = "",
                    onValueChange = {},
                    label = "توضیحات",
                    placeholder = "توضیحات خود را وارد کنید",
                    gradient = Brush.horizontalGradient(listOf(Color.LightGray, Color.White)),
                )

                // فیلد متنی با محدودیت طول
                CustomTextField(
                    value = "",
                    onValueChange = {},
                    label = "یادداشت",
                    placeholder = "یادداشت خود را وارد کنید",
                    maxLength = 100,
                )
            }
        }
    }
}