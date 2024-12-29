package com.msa.composecraft.component.non_lazy_grid

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.min

/**
 * کلاس تنظیمات گرید برای مدیریت پارامترهای پیشرفته.
 * @param columns تعداد ستون‌های گرید.
 * @param horizontalSpacing فاصله افقی بین آیتم‌ها.
 * @param verticalSpacing فاصله عمودی بین ردیف‌ها.
 */
data class GridConfig(
    val columns: Int,
    val horizontalSpacing: Dp = 8.dp,
    val verticalSpacing: Dp = 8.dp
)

/**
 * این کامپوننت یک گرید غیر‌تنبل (Non-Lazy Grid) با قابلیت‌های پیشرفته ایجاد می‌کند.
 * @param config تنظیمات گرید (تعداد ستون‌ها و فاصله‌ها).
 * @param items لیست آیتم‌هایی که باید نمایش داده شوند.
 * @param modifier Modifier برای تنظیمات layout و استایل.
 * @param content تابع Composable که برای نمایش هر آیتم استفاده می‌شود.
 */
@Composable
fun <T> NonlazyGrid(
    config: GridConfig,
    items: List<T>,
    modifier: Modifier = Modifier,
    content: @Composable (T) -> Unit
) {
    // محاسبه تعداد ردیف‌ها
    val rows = (items.size + config.columns - 1) / config.columns

    Column(modifier = modifier) {
        // ایجاد ردیف‌ها
        repeat(rows) { rowIndex ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = config.verticalSpacing),
                horizontalArrangement = Arrangement.spacedBy(config.horizontalSpacing)
            ) {
                // محاسبه محدوده آیتم‌های هر ردیف
                val startIndex = rowIndex * config.columns
                val endIndex = min(startIndex + config.columns, items.size)

                // نمایش آیتم‌های هر ردیف
                for (i in startIndex until endIndex) {
                    Box(
                        modifier = Modifier.weight(1f)
                    ) {
                        content(items[i])
                    }
                }

                // اگر تعداد آیتم‌ها کمتر از تعداد ستون‌ها باشد، Spacer اضافه می‌شود
                if (endIndex - startIndex < config.columns) {
                    Spacer(
                        modifier = Modifier
                            .weight((config.columns - (endIndex - startIndex)).toFloat())
                    )
                }
            }
        }
    }
}

/**
 * این تابع Composable یک آیتم ساده برای نمایش در گرید ایجاد می‌کند.
 * @param index شماره آیتم.
 */
@Composable
fun GridItem(index: Int) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .background(Color.LightGray)
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "آیتم ${index + 1}",
            fontSize = 18.sp,
            color = Color.Black,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * این Preview جامع، چندین حالت مختلف از گرید را نمایش می‌دهد.
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewNonlazyGrid() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // حالت ۱: تعداد آیتم‌ها کمتر از تعداد ستون‌ها
        Text(
            text = "حالت ۱: تعداد آیتم‌ها کمتر از تعداد ستون‌ها",
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        NonlazyGrid(
            config = GridConfig(columns = 3),
            items = List(2) { it },
            modifier = Modifier.padding(bottom = 16.dp)
        ) { index ->
            GridItem(index)
        }

        // حالت ۲: تعداد آیتم‌ها برابر با تعداد ستون‌ها
        Text(
            text = "حالت ۲: تعداد آیتم‌ها برابر با تعداد ستون‌ها",
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        NonlazyGrid(
            config = GridConfig(columns = 3),
            items = List(3) { it },
            modifier = Modifier.padding(bottom = 16.dp)
        ) { index ->
            GridItem(index)
        }

        // حالت ۳: تعداد آیتم‌ها بیشتر از تعداد ستون‌ها
        Text(
            text = "حالت ۳: تعداد آیتم‌ها بیشتر از تعداد ستون‌ها",
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        NonlazyGrid(
            config = GridConfig(columns = 3),
            items = List(7) { it },
            modifier = Modifier.padding(bottom = 16.dp)
        ) { index ->
            GridItem(index)
        }
    }
}


@Composable
fun MyGrid() {
    val items = List(10) { "Item ${it + 1}" }

    NonlazyGrid(
        config = GridConfig(columns = 3, horizontalSpacing = 8.dp, verticalSpacing = 8.dp),
        items = items,
        modifier = Modifier.padding(16.dp)
    ) { item ->
        Text(
            text = item,
            modifier = Modifier
                .padding(8.dp)
                .background(Color.LightGray)
                .padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMyGrid() {
    MyGrid()
}