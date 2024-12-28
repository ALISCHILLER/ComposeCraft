package com.msa.composecraft.component.verticalSlider



import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import coil3.compose.AsyncImage
import kotlin.math.absoluteValue

/**
 * این کامپوننت یک اسلایدر عمودی است که تصاویر را به صورت عمودی نمایش می‌دهد.
 * @param modifier: Modifier برای تنظیمات layout و استایل.
 * @param imageUrls: لیستی از URL تصاویر برای نمایش در اسلایدر.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VerticalSlider2(
    modifier: Modifier = Modifier,
    imageUrls: List<String> = emptyList()
) {
    // حالت Pager برای مدیریت صفحات و موقعیت فعلی
    val pagerState = rememberPagerState(pageCount = { imageUrls.size })

    VerticalPager(
        modifier = modifier,
        state = pagerState,
        contentPadding = PaddingValues(vertical = 32.dp),
    ) { page ->
        // محاسبه افست صفحه فعلی نسبت به صفحه مرکزی
        val pageOffset = (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
        // محاسبه فاکتور مقیاس برای انیمیشن‌های zoom-in و zoom-out
        val scaleFactor = 0.85f + (1f - 0.85f) * (1f - pageOffset.absoluteValue)

        // نمایش تصویر با استفاده از AsyncImage
        AsyncImage(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = scaleFactor
                    scaleY = scaleFactor
                    alpha = lerp(
                        start = 0.5f,
                        stop = 1f,
                        fraction = 1f - pageOffset.absoluteValue.coerceIn(0f, 1f)
                    )
                }
                .clip(RoundedCornerShape(16.dp)),
            model = imageUrls[page],
            contentDescription = "Slider Image $page",
            contentScale = ContentScale.Crop
        )
    }
}

/**
 * Preview برای کامپوننت VerticalSlider.
 */
@Preview(showBackground = true, device = "id:pixel_5")
@Composable
fun PreviewVerticalSlider2() {
    // لیستی از URL تصاویر برای نمایش در Preview
    val sampleImageUrls = listOf(
        "https://via.placeholder.com/600x400/FF5733/FFFFFF",
        "https://via.placeholder.com/600x400/C70039/FFFFFF",
        "https://via.placeholder.com/600x400/900C3F/FFFFFF",
        "https://via.placeholder.com/600x400/581845/FFFFFF"
    )

    // فراخوانی کامپوننت VerticalSlider با داده‌های نمونه
    VerticalSlider2(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        imageUrls = sampleImageUrls
    )


}