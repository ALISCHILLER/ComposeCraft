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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.msa.composecraft.R

/**
 * یک دکمه سفارشی با قابلیت‌های متنوع.
 *
 * @param text متن دکمه.
 * @param onClick Callback برای کلیک روی دکمه.
 * @param modifier Modifier برای تنظیمات layout و استایل.
 * @param icon آیکون دکمه (اختیاری).
 * @param gradient گرادیان پس‌زمینه دکمه (اختیاری).
 * @param backgroundColor رنگ پس‌زمینه دکمه (اگر گرادیان تنظیم نشده باشد).
 * @param textColor رنگ متن دکمه.
 * @param shape شکل دکمه (پیش‌فرض: گوشه‌های گرد).
 * @param borderStroke حاشیه دکمه (اختیاری).
 * @param enabled وضعیت فعال/غیرفعال دکمه.
 * @param isLoading وضعیت لودینگ دکمه (اختیاری).
 * @param elevation ارتفاع سایه دکمه.
 */
@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    gradient: Brush? = null,
    backgroundColor: Color = Color.Blue,
    textColor: Color = Color.White,
    shape: Shape = RoundedCornerShape(8.dp),
    borderStroke: BorderStroke? = null,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    elevation: Dp = 4.dp,
) {
    // تعیین پس‌زمینه دکمه: اگر گرادیان تنظیم شده باشد از آن استفاده می‌شود، در غیر این‌صورت از رنگ پس‌زمینه.
    val buttonBackground = gradient ?: Brush.linearGradient(
        colors = listOf(backgroundColor, backgroundColor)
    )

    // انیمیشن شفافیت برای حالت غیرفعال
    val alpha by animateFloatAsState(
        targetValue = if (enabled) 1f else 0.5f,
        animationSpec = tween(durationMillis = 300)
    )

    // دکمه Material3 با قابلیت‌های سفارشی‌سازی شده
    Button(
        onClick = onClick,
        modifier = modifier
            .clip(shape) // اعمال شکل دکمه
            .alpha(alpha) // اعمال شفافیت
            .semantics { role = Role.Button }, // تنظیم نقش دکمه برای دسترسی‌پذیری
        enabled = enabled && !isLoading, // کنترل وضعیت فعال/غیرفعال
        shape = shape, // شکل دکمه
        border = borderStroke, // حاشیه دکمه
        elevation = ButtonDefaults.buttonElevation(defaultElevation = elevation), // ارتفاع سایه
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent, // رنگ پس‌زمینه شفاف
            disabledContainerColor = Color.Transparent, // رنگ پس‌زمینه شفاف در حالت غیرفعال
        ),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp), // فاصله داخلی
    ) {
        Box(
            modifier = Modifier
                .background(buttonBackground, shape) // اعمال پس‌زمینه
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            contentAlignment = Alignment.Center,
        ) {
            if (isLoading) {
                // نمایش نشانگر لودینگ در صورت فعال بودن حالت لودینگ
                CircularProgressIndicator(
                    color = textColor,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                // نمایش متن و آیکون دکمه
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    icon?.let {
                        Icon(
                            imageVector = it,
                            contentDescription = "Button Icon",
                            tint = textColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Text(
                        text = text,
                        color = textColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }
        }
    }
}

/**
 * Preview برای نمایش دکمه‌های سفارشی.
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
                    .verticalScroll(rememberScrollState()), // امکان اسکرول عمودی
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // دکمه ساده
                Text(text = "دکمه ساده:", style = MaterialTheme.typography.labelMedium)
                CustomButton(
                    text = "Click Me",
                    onClick = { println("Button Clicked!") },
                    modifier = Modifier.fillMaxWidth().height(100.dp), // عرض و ارتفاع دکمه
                    backgroundColor = Color.Blue,
                    textColor = Color.White,
                )

                // دکمه با آیکون
                Text(text = "دکمه با آیکون:", style = MaterialTheme.typography.labelMedium)
                CustomButton(
                    text = "Save",
                    onClick = { println("Saved!") },
                    modifier = Modifier.fillMaxWidth().height(100.dp),
                    icon = ImageVector.vectorResource(id = R.drawable.ic_save),
                    backgroundColor = Color.Green,
                    textColor = Color.White,
                )

                // دکمه با گرادیان
                Text(text = "دکمه با گرادیان:", style = MaterialTheme.typography.labelMedium)
                CustomButton(
                    text = "Gradient Button",
                    onClick = { println("Gradient Button Clicked!") },
                    modifier = Modifier.fillMaxWidth().height(100.dp),
                    gradient = Brush.horizontalGradient(listOf(Color.Red, Color.Yellow)),
                    textColor = Color.White,
                )

                // دکمه غیرفعال
                Text(text = "دکمه غیرفعال:", style = MaterialTheme.typography.labelMedium)
                CustomButton(
                    text = "Disabled",
                    onClick = { println("This won't be called!") },
                    modifier = Modifier.fillMaxWidth().height(100.dp),
                    backgroundColor = Color.Gray,
                    textColor = Color.White,
                    enabled = false,
                )

                // دکمه در حال لودینگ
                Text(text = "دکمه در حال لودینگ:", style = MaterialTheme.typography.labelMedium)
                CustomButton(
                    text = "Loading...",
                    onClick = { println("Loading...") },
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = Color.Blue,
                    textColor = Color.White,
                    isLoading = true,
                )

                // دکمه با حاشیه
                Text(text = "دکمه با حاشیه:", style = MaterialTheme.typography.labelMedium)
                CustomButton(
                    text = "Bordered Button",
                    onClick = { println("Bordered Button Clicked!") },
                    modifier = Modifier.fillMaxWidth().height(100.dp),
                    backgroundColor = Color.Transparent,
                    textColor = Color.Blue,
                    borderStroke = BorderStroke(2.dp, Color.Blue),
                )

                // دکمه با گوشه‌های گرد بزرگ
                Text(text = "دکمه با گوشه‌های گرد بزرگ:", style = MaterialTheme.typography.labelMedium)
                CustomButton(
                    text = "Rounded Button",
                    onClick = { println("Rounded Button Clicked!") },
                    modifier = Modifier.fillMaxWidth().height(100.dp),
                    backgroundColor = Color.Red,
                    textColor = Color.White,
                    shape = RoundedCornerShape(20.dp),
                )
            }
        }
    }
}