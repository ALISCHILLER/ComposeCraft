@file:OptIn(ExperimentalFoundationApi::class)

package com.msa.composecraft.component.pager

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.google.accompanist.pager.*
import com.msa.composecraft.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.yield
import kotlin.math.absoluteValue

/**
 * مدل داده‌ای برای بنرها.
 * @param bannerImage آدرس تصویر بنر.
 * @param bannerName نام بنر.
 * @param id شناسه منحصر به فرد بنر.
 */
data class BannerModel(
    val bannerImage: String,
    val bannerName: String,
    val id: String
)

/**
 * این کامپوننت یک اسلایدر افقی برای نمایش بنرها ایجاد می‌کند.
 * @param modifier Modifier برای تنظیمات layout و استایل.
 * @param banner لیستی از بنرها برای نمایش در اسلایدر.
 */
@Composable
fun SliderBanner(
    modifier: Modifier = Modifier,
    banner: List<BannerModel> = emptyList(),
) {
    // حالت Pager برای مدیریت صفحات و موقعیت فعلی
    val pagerState = rememberPagerState(initialPage = 0)
    val context = LocalContext.current

    // اتوماتیک اسکرول کردن اسلایدر
    LaunchedEffect(Unit) {
        while (true) {
            yield()
            delay(2600)
            if (pagerState.pageCount > 0) {
                try {
                    pagerState.animateScrollToPage(
                        page = (pagerState.currentPage + 1) % (pagerState.pageCount)
                    )
                } catch (e: Exception) {
                    Log.e("SliderBanner", "خطا در اسکرول صفحه: ${e.message}")
                }
            } else {
                Log.e("SliderBanner", "تعداد صفحات صفر است، امکان اسکرول وجود ندارد")
            }
        }
    }

    Column {
        // HorizontalPager برای نمایش بنرها به صورت افقی
        HorizontalPager(
            count = banner.size,
            state = pagerState,
            contentPadding = PaddingValues(horizontal = DIMENS_16dp),
            modifier = modifier
                .fillMaxWidth()
                .height(150.dp)
        ) { page ->
            Card(
                shape = RoundedCornerShape(DIMENS_12dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer {
                        // محاسبه افست صفحه فعلی برای انیمیشن‌ها
                        val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue

                        // اعمال انیمیشن مقیاس (zoom-in و zoom-out)
                        lerp(
                            start = 0.85f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        ).also { scale ->
                            scaleX = scale
                            scaleY = scale
                        }

                        // اعمال انیمیشن شفافیت (fade-in و fade-out)
                        alpha = lerp(
                            start = 0.5f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )
                    }
            ) {
                // نمایش تصویر بنر با استفاده از AsyncImage
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(banner[page].bannerImage)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                )
            }
        }

        // نشانگر صفحات (PagerIndicator)
        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(DIMENS_8dp)
        )
    }
}

/**
 * این تابع یک URL تصادفی برای تصاویر نمونه ایجاد می‌کند.
 * @param seed مقدار seed برای ایجاد تصاویر متفاوت.
 * @param width عرض تصویر.
 * @param height ارتفاع تصویر.
 * @return آدرس URL تصویر.
 */
fun randomSampleImageUrlV2(
    seed: Int = (0..100000).random(),
    width: Int = 300,
    height: Int = width,
): String {
    return "https://picsum.photos/seed/$seed/$width/$height"
}

/**
 * این تابع یک URL تصادفی را به صورت remember شده برمی‌گرداند.
 * @param seed مقدار seed برای ایجاد تصاویر متفاوت.
 * @param width عرض تصویر.
 * @param height ارتفاع تصویر.
 * @return آدرس URL تصویر.
 */
@SuppressLint("RememberReturnType")
@Composable
fun rememberRandomSampleImageUrl2(
    seed: Int = (0..100000).random(),
    width: Int = 300,
    height: Int = width,
): String = remember { randomSampleImageUrlV2(seed, width, height) }

/**
 * Preview برای کامپوننت SliderBanner.
 */
@ExperimentalPagerApi
@Preview
@Composable
fun SliderBannerPreview() {
    val imageSlider = listOf(
        BannerModel(
            bannerImage = "https://imgv3.fotor.com/images/slider-image/Female-portrait-picture-enhanced-with-better-clarity-and-higher-quality-using-Fotors-free-online-AI-photo-enhancer.jpg",
            bannerName = "بنر ۱",
            id = "1"
        ),
        BannerModel(
            bannerImage = "https://imgv3.fotor.com/images/slider-image/A-blurry-close-up-photo-of-a-woman.jpg",
            bannerName = "بنر ۲",
            id = "2"
        )
    )

    SliderBanner(
        banner = imageSlider
    )

//    val banners = listOf(
//        BannerModel(
//            bannerImage = "https://example.com/image1.jpg",
//            bannerName = "بنر ۱",
//            id = "1"
//        ),
//        BannerModel(
//            bannerImage = "https://example.com/image2.jpg",
//            bannerName = "بنر ۲",
//            id = "2"
//        )
//    )
//
//    SliderBanner(
//        banner = banners,
//        modifier = Modifier.fillMaxWidth()
//    )
}