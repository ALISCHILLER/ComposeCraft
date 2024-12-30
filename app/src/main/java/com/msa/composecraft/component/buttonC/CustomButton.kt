package com.msa.composecraft.component.buttonC

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.msa.composecraft.R

/**
 * یک کامپوننت Button سفارشی که قابلیت‌های مختلفی از جمله پشتیبانی از گرادیان، آیکون، لودینگ و استایل‌های متنی دارد.
 * این کامپوننت به گونه‌ای طراحی شده که به راحتی قابل استفاده و شخصی‌سازی باشد.
 *
 * @param text متن دکمه
 * @param onClick تابعی که هنگام کلیک روی دکمه اجرا می‌شود
 * @param modifier تنظیمات Modifier برای سفارشی‌سازی ظاهر و رفتار دکمه
 * @param icon آیکون اختیاری برای نمایش در کنار متن دکمه
 * @param gradient براش گرادیان اختیاری برای پس‌زمینه دکمه
 * @param backgroundColor رنگ پس‌زمینه دکمه (در صورتی که گرادیان تعریف نشده باشد)
 * @param textColor رنگ متن دکمه
 * @param textStyle استایل متنی دکمه
 * @param shape شکل دکمه (پیش‌فرض: گوشه‌های گرد)
 * @param borderStroke استایل حاشیه دکمه
 * @param enabled وضعیت فعال یا غیرفعال بودن دکمه
 * @param isLoading وضعیت نمایش نشانگر لودینگ
 * @param elevation مقدار سایه دکمه
 * @param iconSpacing فاصله بین آیکون و متن
 * @param loadingIndicatorSize اندازه نشانگر لودینگ
 */
@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    gradient: Brush? = null,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    textColor: Color = MaterialTheme.colorScheme.onPrimary,
    textStyle: TextStyle = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium),
    shape: Shape = RoundedCornerShape(8.dp),
    borderStroke: BorderStroke? = null,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    elevation: Dp = 4.dp,
    iconSpacing: Dp = 8.dp,
    loadingIndicatorSize: Dp = 24.dp
) {
    // ایجاد پس‌زمینه دکمه بر اساس گرادیان یا رنگ ساده
    val buttonBackground = gradient ?: Brush.linearGradient(
        colors = listOf(backgroundColor, backgroundColor)
    )

    // انیمیشن تغییر شفافیت برای حالت فعال و غیرفعال
    val alpha by animateFloatAsState(
        targetValue = if (enabled) 1f else 0.5f,
        animationSpec = tween(durationMillis = 300)
    )

    Button(
        onClick = onClick,
        modifier = modifier
            .clip(shape)
            .alpha(alpha)
            .semantics { role = Role.Button },
        enabled = enabled && !isLoading,
        shape = shape,
        border = borderStroke,
        elevation = ButtonDefaults.buttonElevation(defaultElevation = elevation),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues(horizontal = 0.dp, vertical = 0.dp),
    ) {
        Box(
            modifier = Modifier
                .background(buttonBackground, shape)
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                // نمایش نشانگر لودینگ در حالت لودینگ
                CircularProgressIndicator(
                    color = textColor,
                    modifier = Modifier.size(loadingIndicatorSize),
                    strokeWidth = 2.dp
                )
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(iconSpacing),
                ) {
                    // نمایش آیکون در صورت تعریف
                    icon?.let {
                        Icon(
                            imageVector = it,
                            contentDescription = null,
                            tint = textColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    // نمایش متن دکمه
                    Text(text = text, color = textColor, style = textStyle)
                }
            }
        }
    }
}

/**
 * پیش‌نمایش‌های مختلف برای بررسی ظاهر و عملکرد کامپوننت CustomButton.
 */
@Preview(showBackground = true)
@Composable
private fun CustomButtonPreview() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // پیش‌نمایش دکمه ساده
                CustomButton(
                    text = "Click Me",
                    onClick = { },
                    modifier = Modifier.fillMaxWidth(),
                )

                // پیش‌نمایش دکمه با آیکون
                CustomButton(
                    text = "Save",
                    onClick = { },
                    modifier = Modifier.fillMaxWidth(),
                    icon = ImageVector.vectorResource(id = R.drawable.ic_save),
                )

                // پیش‌نمایش دکمه با گرادیان
                CustomButton(
                    text = "Gradient",
                    onClick = { },
                    gradient = Brush.horizontalGradient(colors = listOf(Color.Red, Color.Yellow))
                )

                // پیش‌نمایش دکمه در حالت لودینگ
                CustomButton(
                    text = "Loading",
                    onClick = { },
                    isLoading = true
                )
            }
        }
    }
}
