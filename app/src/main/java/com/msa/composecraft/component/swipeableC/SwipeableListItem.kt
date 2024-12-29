package com.msa.composecraft.component.swipeable

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

// کلاس داده‌ای برای نگهداری اطلاعات هر آیتم
data class SwipeableItem(
    val id: Int, // شناسه منحصر به فرد آیتم
    val title: String, // عنوان آیتم
    val description: String // توضیحات آیتم
)

// کامپوننت اصلی برای نمایش آیتم‌های قابل کشیدن
@Composable
fun SwipeableListItem(
    item: SwipeableItem, // اطلاعات آیتم
    onDelete: () -> Unit, // عملیات حذف آیتم
    onArchive: () -> Unit, // عملیات آرشیو آیتم
    modifier: Modifier = Modifier
) {
    // عرض آیتم برای محاسبه میزان کشیدن
    val itemWidth = with(LocalDensity.current) { 300.dp.toPx() }
    val swipeThreshold = itemWidth * 0.2f // آستانه کشیدن برای فعال‌سازی عملیات

    // حالت برای نگهداری مقدار کشیدن
    var swipeOffset by remember { mutableStateOf(0f) }
    val coroutineScope = rememberCoroutineScope()

    // جهت کشیدن (چپ یا راست)
    val swipeDirection = when {
        swipeOffset > 0 -> SwipeDirection.RightToLeft
        swipeOffset < 0 -> SwipeDirection.LeftToRight
        else -> SwipeDirection.None
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .offset { IntOffset(swipeOffset.toInt(), 0) }
    ) {
        // پس‌زمینه برای عملیات‌های کشیدن
        SwipeBackground(
            swipeDirection = swipeDirection,
            onDelete = onDelete,
            onArchive = onArchive,
            modifier = Modifier.matchParentSize()
        )

        // محتوای آیتم
        SwipeContent(
            item = item,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                .padding(16.dp)
        )
    }
}

// جهت کشیدن
enum class SwipeDirection {
    LeftToRight, RightToLeft, None
}

// پس‌زمینه برای عملیات‌های کشیدن
@Composable
fun SwipeBackground(
    swipeDirection: SwipeDirection,
    onDelete: () -> Unit,
    onArchive: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                when (swipeDirection) {
                    SwipeDirection.LeftToRight -> Color.Red
                    SwipeDirection.RightToLeft -> Color.Green
                    else -> Color.Transparent
                }
            ),
        contentAlignment = when (swipeDirection) {
            SwipeDirection.LeftToRight -> Alignment.CenterStart
            SwipeDirection.RightToLeft -> Alignment.CenterEnd
            else -> Alignment.Center
        }
    ) {
        when (swipeDirection) {
            SwipeDirection.LeftToRight -> {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.White,
                    modifier = Modifier
                        .size(40.dp)
                        .clickable { onDelete() }
                )
            }
            SwipeDirection.RightToLeft -> {
                Icon(
                    imageVector = Icons.Default.Archive,
                    contentDescription = "Archive",
                    tint = Color.White,
                    modifier = Modifier
                        .size(40.dp)
                        .clickable { onArchive() }
                )
            }
            else -> Unit
        }
    }
}

// محتوای آیتم
@Composable
fun SwipeContent(
    item: SwipeableItem,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = item.title,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = item.description,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
            fontSize = 14.sp
        )
    }
}

// پیش‌نمایش: نمایش آیتم‌های قابل کشیدن
@Preview(showBackground = true)
@Composable
fun PreviewSwipeableListItem() {
    val item = SwipeableItem(
        id = 1,
        title = "آیتم ۱",
        description = "توضیحات آیتم ۱"
    )

    MaterialTheme {
        SwipeableListItem(
            item = item,
            onDelete = { println("آیتم حذف شد") },
            onArchive = { println("آیتم آرشیو شد") },
            modifier = Modifier.padding(16.dp)
        )
    }
}