@file:OptIn(ExperimentalMaterialApi::class)

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
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.msa.composecraft.R



/**
 * یک Expandable Card حرفه‌ای با قابلیت‌های انیمیشن و سفارشی‌سازی.
 *
 * @param modifier Modifier برای تنظیمات layout و استایل.
 * @param title عنوان کارت.
 * @param titleFontSize اندازه فونت عنوان (پیش‌فرض: `MaterialTheme.typography.h6.fontSize`).
 * @param titleFontWeight وزن فونت عنوان (پیش‌فرض: `FontWeight.Bold`).
 * @param padding padding داخلی کارت (پیش‌فرض: `12.dp`).
 * @param painter تصویر نمایش داده شده در کنار عنوان (پیش‌فرض: `R.drawable.not_load_image`).
 * @param imageSize اندازه تصویر کنار عنوان (پیش‌فرض: `40.dp`).
 * @param content محتوای قابل گسترش.
 */
@Composable
fun ExpandableCard(
    modifier: Modifier = Modifier,
    title: String,
    titleFontSize: TextUnit = MaterialTheme.typography.h6.fontSize,
    titleFontWeight: FontWeight = FontWeight.Bold,
    padding: Dp = 12.dp,
    painter: Painter = painterResource(id = R.drawable.not_load_image),
    imageSize: Dp = 40.dp, // اندازه تصویر
    content: @Composable ColumnScope.() -> Unit,
) {
    // وضعیت باز یا بسته بودن کارت
    var expandedState by remember { mutableStateOf(false) }

    // انیمیشن چرخش آیکون
    val rotationState by animateFloatAsState(
        targetValue = if (expandedState) 180f else 0f
    )

    Card(
        modifier = modifier
            .padding(5.dp)
            .fillMaxWidth()
            .shadow(elevation = 10.dp, shape = RoundedCornerShape(18.dp))
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            ),
        onClick = { expandedState = !expandedState },
        shape = RoundedCornerShape(18.dp),
        elevation = 4.dp
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
                // تصویر کنار عنوان
                Image(
                    painter = painter,
                    contentDescription = "Logo",
                    modifier = Modifier.size(imageSize) // استفاده از پارامتر imageSize
                )
                Spacer(modifier = Modifier.width(8.dp))

                // عنوان
                Text(
                    modifier = Modifier.weight(1f),
                    text = title,
                    fontSize = titleFontSize,
                    fontWeight = titleFontWeight,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // آیکون باز و بسته شدن
                IconButton(
                    modifier = Modifier
                        .rotate(rotationState),
                    onClick = { expandedState = !expandedState }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Drop-Down Arrow"
                    )
                }
            }

            // محتوای قابل گسترش با انیمیشن
            AnimatedVisibility(
                visible = expandedState,
                enter = expandVertically(animationSpec = tween(durationMillis = 300)),
                exit = shrinkVertically(animationSpec = tween(durationMillis = 300))
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
 * Preview برای نمایش ExpandableCard.
 */
@Preview(showBackground = true)
@Composable
private fun ExpandableCardPreview() {
    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            ExpandableCard(
                title = "عنوان کارت",
                imageSize = 90.dp // تنظیم اندازه تصویر
            ) {
                Text(
                    text = "این محتوای قابل گسترش است. می‌توانید هر چیزی را در اینجا قرار دهید.",
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}