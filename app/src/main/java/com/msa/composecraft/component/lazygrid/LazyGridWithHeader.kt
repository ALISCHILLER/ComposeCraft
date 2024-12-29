

package com.msa.composecraft.component.lazygrid

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// کلاس داده‌ای برای نگهداری اطلاعات هر آیتم در گرید
data class GridItem(
    val id: Int, // شناسه منحصر به فرد آیتم
    val title: String, // عنوان آیتم
    val description: String, // توضیحات آیتم
    val color: Color // رنگ پس‌زمینه آیتم
)

// کامپوننت اصلی برای نمایش گرید با هدر
@Composable
fun LazyGridWithHeader(
    headerTitle: String, // عنوان هدر
    items: List<GridItem>, // لیست آیتم‌های گرید
    onBackClick: () -> Unit = {}, // عملیات کلیک روی دکمه بازگشت
    onItemClick: (GridItem) -> Unit = {} // عملیات کلیک روی آیتم‌ها
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // هدر گرید
        Header(
            title = headerTitle,
            onBackClick = onBackClick
        )

        // گرید آیتم‌ها
        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // تعداد ستون‌ها (در اینجا 2 ستون)
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items) { item ->
                GridItemView(
                    item = item,
                    onClick = { onItemClick(item) }
                )
            }
        }
    }
}

// کامپوننت برای نمایش هدر
@Composable
fun Header(
    title: String, // عنوان هدر
    onBackClick: () -> Unit // عملیات کلیک روی دکمه بازگشت
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // دکمه بازگشت
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back",
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .size(24.dp)
                .clickable { onBackClick() }
        )

        // عنوان هدر
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        // یک Spacer برای ایجاد فاصله
        Spacer(modifier = Modifier.size(24.dp))
    }
}

// کامپوننت برای نمایش هر آیتم در گرید
@Composable
fun GridItemView(
    item: GridItem, // اطلاعات آیتم
    onClick: () -> Unit // عملیات کلیک روی آیتم
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f) // نسبت ابعاد مربعی
            .background(item.color)
            .clickable { onClick() }
            .animateContentSize(animationSpec = tween(durationMillis = 300)), // انیمیشن تغییر اندازه
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // عنوان آیتم
            Text(
                text = item.title,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            // توضیحات آیتم
            Text(
                text = item.description,
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp
            )
        }
    }
}

// پیش‌نمایش: نمایش گرید با هدر
@Preview(showBackground = true)
@Composable
fun PreviewLazyGridWithHeader() {
    val items = listOf(
        GridItem(1, "آیتم ۱", "توضیحات آیتم ۱", Color(0xFF6200EE)),
        GridItem(2, "آیتم ۲", "توضیحات آیتم ۲", Color(0xFF03DAC6)),
        GridItem(3, "آیتم ۳", "توضیحات آیتم ۳", Color(0xFFE91E63)),
        GridItem(4, "آیتم ۴", "توضیحات آیتم ۴", Color(0xFFFFC107)),
        GridItem(5, "آیتم ۵", "توضیحات آیتم ۵", Color(0xFF4CAF50)),
        GridItem(6, "آیتم ۶", "توضیحات آیتم ۶", Color(0xFF2196F3))
    )

    MaterialTheme {
        LazyGridWithHeader(
            headerTitle = "گرید پیشرفته",
            items = items,
            onBackClick = { /* عملیات بازگشت */ },
            onItemClick = { item -> println("Clicked on: ${item.title}") }
        )
    }
}