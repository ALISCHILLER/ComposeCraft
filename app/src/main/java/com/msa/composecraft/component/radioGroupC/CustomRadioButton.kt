package com.msa.composecraft.component.radioGroupC


import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * این کامپوننت یک RadioButton کاملاً سفارشی ایجاد می‌کند.
 * @param selected وضعیت انتخاب (true اگر انتخاب شده باشد).
 * @param onClick تابعی که هنگام کلیک فراخوانی می‌شود.
 * @param modifier Modifier برای تنظیمات layout و استایل.
 * @param enabled فعال یا غیرفعال بودن دکمه.
 * @param interactionSource منبع تعامل برای مدیریت حالت‌هایی مانند فشار دادن.
 * @param colors رنگ‌های سفارشی برای دکمه.
 * @param shape شکل دکمه (پیش‌فرض: دایره).
 * @param size اندازه دکمه.
 * @param strokeWidth عرض خط دور دکمه.
 * @param animationEnabled فعال یا غیرفعال کردن انیمیشن.
 */
@Composable
fun CustomRadioButton(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    colors: RadioButtonColors = CustomRadioButtonDefaults.colors(),
    shape: Shape = CircleShape,
    size: Dp = 24.dp,
    strokeWidth: Dp = 2.dp,
    animationEnabled: Boolean = true
) {
    // انیمیشن برای تغییر اندازه دکمه هنگام فشار دادن
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed && animationEnabled) 0.9f else 1f,
        label = "RadioButtonScaleAnimation"
    )

    // رنگ‌های دکمه بر اساس وضعیت انتخاب و فعال بودن
    val radioColor = if (selected) colors.selectedColor else colors.unselectedColor
    val borderColor = if (selected) colors.selectedBorderColor else colors.unselectedBorderColor

    Box(
        modifier = modifier
            .size(size)
            .clip(shape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick
            )
            .scale(scale),
        contentAlignment = Alignment.Center
    ) {
        // خط دور دکمه
        Surface(
            shape = shape,
            color = Color.Transparent,
            border = BorderStroke(strokeWidth, borderColor),
            modifier = Modifier.matchParentSize()
        ) {}

        // دایره داخلی (فقط هنگام انتخاب نمایش داده می‌شود)
        if (selected) {
            Surface(
                shape = shape,
                color = radioColor,
                modifier = Modifier.size(size * 0.6f)
            ) {}
        }
    }
}

/**
 * رنگ‌های سفارشی برای CustomRadioButton.
 * @param selectedColor رنگ داخلی دکمه هنگام انتخاب.
 * @param unselectedColor رنگ داخلی دکمه هنگام عدم انتخاب.
 * @param selectedBorderColor رنگ خط دور دکمه هنگام انتخاب.
 * @param unselectedBorderColor رنگ خط دور دکمه هنگام عدم انتخاب.
 */
data class RadioButtonColors(
    val selectedColor: Color,
    val unselectedColor: Color,
    val selectedBorderColor: Color,
    val unselectedBorderColor: Color
)

/**
 * مقادیر پیش‌فرض برای CustomRadioButton.
 */
object CustomRadioButtonDefaults {
    @Composable
    fun colors(
        selectedColor: Color = MaterialTheme.colorScheme.primary,
        unselectedColor: Color = MaterialTheme.colorScheme.surface,
        selectedBorderColor: Color = MaterialTheme.colorScheme.primary,
        unselectedBorderColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
    ): RadioButtonColors {
        return RadioButtonColors(
            selectedColor = selectedColor,
            unselectedColor = unselectedColor,
            selectedBorderColor = selectedBorderColor,
            unselectedBorderColor = unselectedBorderColor
        )
    }
}

/**
 * Preview برای CustomRadioButton.
 */
@Preview(showBackground = true)
@Composable
private fun CustomRadioButtonPreview() {
    var selected by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CustomRadioButton(
            selected = selected,
            onClick = { selected = !selected },
            colors = CustomRadioButtonDefaults.colors(),
            size = 32.dp,
            strokeWidth = 2.dp,
            animationEnabled = true
        )

        Text(
            text = if (selected) "انتخاب شده" else "انتخاب نشده",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}