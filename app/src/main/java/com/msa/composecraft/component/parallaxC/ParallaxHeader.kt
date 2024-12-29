package com.msa.composecraft.component.parallaxC



import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.abs

// کامپوننت اصلی برای نمایش هدر پارالاکس
@Composable
fun ParallaxHeader(
    headerImageRes: Int, // منبع تصویر هدر
    headerTitle: String, // عنوان هدر
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit // محتوای زیر هدر
) {
    // حالت اسکرول
    val scrollState = rememberLazyListState()

    // مقدار اسکرول برای ایجاد اثر پارالاکس
    var scrollOffset by remember { mutableStateOf(0f) }

    // اتصال اسکرول برای محاسبه مقدار پارالاکس
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                scrollOffset = (scrollOffset + available.y).coerceAtLeast(0f)
                return Offset.Zero
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
    ) {
        // هدر پارالاکس
        Image(
            painter = painterResource(id = headerImageRes),
            contentDescription = "Header Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .graphicsLayer {
                    translationY = scrollOffset * 0.5f // اثر پارالاکس
                }
        )

        // عنوان هدر
        Text(
            text = headerTitle,
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
                .graphicsLayer {
                    translationY = scrollOffset * 0.3f // اثر پارالاکس برای عنوان
                }
        )

        // محتوای زیر هدر
        LazyColumn(
            state = scrollState,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 250.dp)
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .background(MaterialTheme.colorScheme.background)
        ) {
            items(20) { index ->
                Text(
                    text = "Item $index",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }
    }
}

// پیش‌نمایش: نمایش هدر پارالاکس
@Preview(showBackground = true)
@Composable
fun PreviewParallaxHeader() {
    MaterialTheme {
        ParallaxHeader(
            headerImageRes = android.R.drawable.ic_dialog_info, // تصویر نمونه
            headerTitle = "Parallax Header",
            modifier = Modifier.fillMaxSize()
        ) {
            // محتوای اضافی (اختیاری)
        }
    }
}