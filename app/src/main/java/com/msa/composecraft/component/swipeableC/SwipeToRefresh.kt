package com.msa.composecraft.component.swipeableC


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.delay

// کامپوننت اصلی برای نمایش SwipeToRefresh
@Composable
fun SwipeToRefresh(
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit, // عملیات رفرش
    content: @Composable () -> Unit // محتوای قابل رفرش
) {
    // حالت برای نمایش انیمیشن رفرش
    var isRefreshing by remember { mutableStateOf(false) }

    // حالت SwipeRefresh
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isRefreshing)

    // اعمال عملیات رفرش
    LaunchedEffect(swipeRefreshState.isRefreshing) {
        if (swipeRefreshState.isRefreshing) {
            onRefresh()
            delay(2000) // شبیه‌سازی عملیات رفرش (2 ثانیه)
            isRefreshing = false
        }
    }

    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = { isRefreshing = true },
        modifier = modifier,
        indicator = { state, trigger ->
            // سفارشی‌سازی انیمیشن رفرش
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Refresh",
                    tint = if (state.isRefreshing) MaterialTheme.colorScheme.primary else Color.Gray,
                    modifier = Modifier
                        .size(32.dp)
                        .rotate(if (state.isRefreshing) 360f else 0f)
                )
            }
        }
    ) {
        // محتوای قابل رفرش
        content()
    }
}

// پیش‌نمایش: نمایش SwipeToRefresh
@Preview(showBackground = true)
@Composable
fun PreviewSwipeToRefresh() {
    var items by remember { mutableStateOf((1..10).map { "Item $it" }) }

    MaterialTheme {
        SwipeToRefresh(
            onRefresh = { items = (1..10).map { "Refreshed Item $it" } },
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(items) { item ->
                    Text(
                        text = item,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                }
            }
        }
    }
}