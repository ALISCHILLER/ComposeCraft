package com.msa.composecraft.component.expandable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

/**
 * یک Expandable Component پیشرفته و کاملاً سفارشی‌سازی‌پذیر.
 *
 * @param modifier Modifier برای تنظیمات layout و استایل.
 * @param title عنوان کامپوننت.
 * @param titleFontSize اندازه فونت عنوان (پیش‌فرض: `MaterialTheme.typography.labelSmall.fontSize`).
 * @param titleFontWeight وزن فونت عنوان (پیش‌فرض: `FontWeight.Bold`).
 * @param titleColor رنگ عنوان (پیش‌فرض: `MaterialTheme.colorScheme.onSurface`).
 * @param icon آیکون نمایش داده شده در کنار عنوان (پیش‌فرض: `Icons.Default.ArrowDropDown`).
 * @param iconSize اندازه آیکون (پیش‌فرض: `24.dp`).
 * @param iconColor رنگ آیکون (پیش‌فرض: `MaterialTheme.colorScheme.onSurface`).
 * @param image تصویر نمایش داده شده در کنار عنوان (اختیاری).
 * @param imageSize اندازه تصویر (پیش‌فرض: `40.dp`).
 * @param backgroundColor رنگ پس‌زمینه کامپوننت (پیش‌فرض: `MaterialTheme.colorScheme.surface`).
 * @param padding padding داخلی کامپوننت (پیش‌فرض: `12.dp`).
 * @param elevation ارتفاع سایه (پیش‌فرض: `4.dp`).
 * @param shape شکل کامپوننت (پیش‌فرض: `RoundedCornerShape(8.dp)`).
 * @param animationDuration مدت زمان انیمیشن‌ها (پیش‌فرض: `300`).
 * @param onExpand Callback برای زمانی که کامپوننت باز می‌شود.
 * @param onCollapse Callback برای زمانی که کامپوننت بسته می‌شود.
 * @param headerContent محتوای سفارشی برای Header (اختیاری).
 * @param content محتوای قابل گسترش.
 */
@Composable
fun ExpandableComponent(
    modifier: Modifier = Modifier,
    title: String? = null,
    titleFontSize: TextUnit = MaterialTheme.typography.labelSmall.fontSize,
    titleFontWeight: FontWeight = FontWeight.Bold,
    titleColor: Color = MaterialTheme.colorScheme.onSurface,
    icon: ImageVector = Icons.Default.ArrowDropDown,
    iconSize: Dp = 24.dp,
    iconColor: Color = MaterialTheme.colorScheme.onSurface,
    image: Painter? = null,
    imageSize: Dp = 40.dp,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    padding: Dp = 12.dp,
    elevation: Dp = 4.dp,
    shape: androidx.compose.ui.graphics.Shape = RoundedCornerShape(8.dp),
    animationDuration: Int = 300,
    onExpand: (() -> Unit)? = null,
    onCollapse: (() -> Unit)? = null,
    headerContent: @Composable (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    // وضعیت باز یا بسته بودن کامپوننت
    var expandedState by remember { mutableStateOf(false) }

    // انیمیشن چرخش آیکون
    val rotationState by animateFloatAsState(
        targetValue = if (expandedState) 180f else 0f
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = animationDuration,
                    easing = LinearOutSlowInEasing
                )
            ),
        shape = shape,
        elevation = CardDefaults.cardElevation(defaultElevation = elevation),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
        ) {
            // Header (عنوان، تصویر و آیکون)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // تصویر کنار عنوان (اختیاری)
                image?.let {
                    Image(
                        painter = it,
                        contentDescription = "Header Image",
                        modifier = Modifier.size(imageSize)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }

                // عنوان یا محتوای سفارشی
                if (headerContent != null) {
                    headerContent()
                } else {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = title ?: "",
                        fontSize = titleFontSize,
                        fontWeight = titleFontWeight,
                        color = titleColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // آیکون باز و بسته شدن
                IconButton(
                    modifier = Modifier
                        .size(iconSize)
                        .rotate(rotationState),
                    onClick = {
                        expandedState = !expandedState
                        if (expandedState) onExpand?.invoke() else onCollapse?.invoke()
                    }
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = "Expand/Collapse",
                        tint = iconColor
                    )
                }
            }

            // محتوای قابل گسترش با انیمیشن
            AnimatedVisibility(
                visible = expandedState,
                enter = expandVertically(animationSpec = tween(durationMillis = animationDuration)),
                exit = shrinkVertically(animationSpec = tween(durationMillis = animationDuration))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    content()
                }
            }
        }
    }
}

/**
 * Preview برای نمایش ExpandableComponent.
 */
@Preview(showBackground = true)
@Composable
private fun ExpandableComponentPreview() {
    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            ExpandableComponent(
                title = "سوالات متداول",
                icon = Icons.Default.ArrowDropDown,
                iconSize = 32.dp,
                elevation = 8.dp,
                backgroundColor = Color.LightGray,
                onExpand = { println("Expanded!") },
                onCollapse = { println("Collapsed!") },
                content = {
                    Text(
                        text = "پاسخ به سوالات متداول در اینجا قرار می‌گیرد.",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            )
        }
    }
}