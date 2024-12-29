package com.msa.composecraft.component.fabC

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import kotlinx.coroutines.launch

// کامپوننت اصلی برای نمایش FAB با انیمیشن
@Composable
fun AnimatedFAB(
    isExpanded: Boolean, // حالت گسترش یافته یا جمع شده
    onClick: () -> Unit, // عملیات کلیک
    modifier: Modifier = Modifier,
    expandedColor: Color = MaterialTheme.colorScheme.primary, // رنگ در حالت گسترش یافته
    collapsedColor: Color = MaterialTheme.colorScheme.secondary, // رنگ در حالت جمع شده
    animationDuration: Int = 300 // مدت زمان انیمیشن
) {
    // حالت برای انیمیشن تغییر رنگ
    val colorProgress = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()

    // حالت برای انیمیشن تغییر اندازه
    val sizeProgress = remember { Animatable(if (isExpanded) 1f else 0f) }

    // اعمال انیمیشن‌ها بر اساس حالت
    LaunchedEffect(isExpanded) {
        coroutineScope.launch {
            colorProgress.animateTo(
                targetValue = if (isExpanded) 1f else 0f,
                animationSpec = tween(durationMillis = animationDuration)
            )
            sizeProgress.animateTo(
                targetValue = if (isExpanded) 1f else 0f,
                animationSpec = tween(durationMillis = animationDuration)
            )
        }
    }

    // محاسبه رنگ و اندازه بر اساس پیشرفت انیمیشن
    val fabColor = Color(
        red = lerp(collapsedColor.red, expandedColor.red, colorProgress.value),
        green = lerp(collapsedColor.green, expandedColor.green, colorProgress.value),
        blue = lerp(collapsedColor.blue, expandedColor.blue, colorProgress.value),
        alpha = lerp(collapsedColor.alpha, expandedColor.alpha, colorProgress.value)
    )
    val fabSize = lerp(56.dp, 64.dp, sizeProgress.value)

    FloatingActionButton(
        onClick = onClick,
        modifier = modifier.size(fabSize),
        shape = CircleShape,
        containerColor = fabColor
    ) {
        Icon(
            imageVector = if (isExpanded) Icons.Default.Close else Icons.Default.Add,
            contentDescription = if (isExpanded) "Close" else "Add",
            modifier = Modifier.size(24.dp)
        )
    }
}

// تابع کمکی برای محاسبه مقدار میانی (Lerp)
private fun lerp(start: Float, end: Float, progress: Float): Float {
    return start + (end - start) * progress
}

// پیش‌نمایش: نمایش FAB با انیمیشن
@Preview(showBackground = true)
@Composable
fun PreviewAnimatedFAB() {
    var isExpanded by remember { mutableStateOf(false) }

    MaterialTheme {
        AnimatedFAB(
            isExpanded = isExpanded,
            onClick = { isExpanded = !isExpanded },
            modifier = Modifier.padding(16.dp)
        )
    }
}