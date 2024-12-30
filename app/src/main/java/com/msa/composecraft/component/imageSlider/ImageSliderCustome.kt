package com.msa.composecraft.component.imageSlider

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.*
import com.msa.composecraft.R
import com.msa.composecraft.ui.theme.ComposeCraftTheme
import kotlinx.coroutines.delay

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ImageSlider(
    images: List<Int>, // لیست تصاویر (مثلاً R.drawable.image1, R.drawable.image2)
    modifier: Modifier = Modifier,
    autoScroll: Boolean = true, // آیا اسلایدر به‌طور خودکار اسکرول شود؟
    autoScrollInterval: Long = 3000 // فاصله‌ی اسکرول خودکار (میلی‌ثانیه)
) {
    val pagerState = rememberPagerState()

    // اسکرول خودکار
    LaunchedEffect(pagerState) {
        if (autoScroll) {
            while (true) {
                delay(autoScrollInterval)
                val nextPage = (pagerState.currentPage + 1) % pagerState.pageCount
                pagerState.animateScrollToPage(nextPage)
            }
        }
    }

    Column(modifier = modifier) {
        // نمایش تصاویر با HorizontalPager
        HorizontalPager(
            count = images.size,
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) { page ->
            Image(
                painter = painterResource(id = images[page]),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
        }

        // نشان‌گرهای صفحه (صفحه‌ی فعلی)
        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp),
            activeColor = MaterialTheme.colorScheme.primary,
            inactiveColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
        )
    }
}

@Preview
@Composable
private fun ImageSliderPreview() {
    ComposeCraftTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            val images = listOf(
                R.drawable.image_1,
                R.drawable.image_2,
                R.drawable.image_3
            )
            ImageSlider(
                images = images,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                autoScroll = true,
                autoScrollInterval = 3000
            )
        }
    }
}