package com.msa.composecraft.component.verticalSlider

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import kotlin.math.absoluteValue
import com.msa.composecraft.ui.theme.*
import androidx.compose.ui.unit.dp
import com.msa.composecraft.ui.theme.PaddingLarge
import com.msa.composecraft.ui.theme.PaddingNormal
import com.msa.composecraft.ui.theme.CornerSizeLarge
/**
 * این کامپوننت یک اسلایدر عمودی است که تصاویر را به صورت عمودی نمایش می‌دهد.
 * @param modifier: Modifier برای تنظیمات layout و استایل.
 * @param imageUrl: لیستی از مدل‌های بنر که شامل URL تصاویر و اطلاعات مرتبط است.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VerticalSlider(
    modifier: Modifier = Modifier,
    imageUrl: List<BannerModel> = emptyList()
) {
    // حالت Pager برای مدیریت صفحات و موقعیت فعلی
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { imageUrl.size })

    VerticalPager(
        modifier = modifier,
        state = pagerState,
        contentPadding = PaddingValues(start = PaddingLarge, end = PaddingLarge, bottom = 25.dp, top = PaddingNormal),
    ) { page ->
        // محاسبه افست صفحه فعلی نسبت به صفحه مرکزی
        val pageOffset = (page - pagerState.currentPage) + pagerState.currentPageOffsetFraction
        // محاسبه فاکتور مقیاس برای انیمیشن‌های zoom-in و zoom-out
        val scaleFactor = 0.85f + (1f - 0.85f) * (1f - pageOffset.absoluteValue)

        // نمایش تصویر با استفاده از کامپوننت LoadAndCacheImage
        LoadAndCacheImage(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = scaleFactor
                    alpha = lerp(
                        start = 0.5f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    )
                }
                .offset(
                    y = if (pagerState.currentPage == page) 0.dp * pageOffset else (-130 * pageOffset).dp
                )
                .fillMaxSize()
                .clip(RoundedCornerShape(CornerSizeLarge)),
            imageLink = imageUrl[page].bannerImage,
            token = ""
        )
    }
}

/**
 * مدل داده‌ای برای بنر که شامل URL تصویر، نام بنر و شناسه منحصر به فرد است.
 */
data class BannerModel(
    val bannerImage: String,
    val bannerName: String,
    val id: String
)



@Preview(showBackground = true, device = "id:pixel_5", showSystemUi = true)
@Composable
fun PreviewVerticalSlider() {
    // لیستی از مدل‌های بنر برای نمایش در Preview
    val sampleBanners = listOf(
        BannerModel(
            bannerImage = "https://axprint.com/blog/wp-content/uploads/2020/10/girl4.jpg",
            bannerName = "Banner 1",
            id = "1"
        ),
        BannerModel(
            bannerImage = "https://www.gfxdownload.ir/uploads/posts/2023-09/nature3.jpg",
            bannerName = "Banner 2",
            id = "2"
        ),
        BannerModel(
            bannerImage = "https://dl1.mrtarh.com/QLUU-GXDA/preview.jpg",
            bannerName = "Banner 3",
            id = "3"
        ),
        BannerModel(
            bannerImage = "https://www.jowhareh.com/images/Jowhareh/galleries_5/poster_24691af5-5db6-49c2-a775-6ce55ab66bc3.jpeg",
            bannerName = "Banner 4",
            id = "4"
        ),
        BannerModel(
            bannerImage = "https://mag.parsnews.com/wp-content/uploads/2022/06/Most-Beautiful-Nature-Wallpapers-Top-Free-Most-Beautiful-25.jpg",
            bannerName = "Banner 5",
            id = "5"
        ),
        BannerModel(
            bannerImage = "https://www.eligasht.com/Blog/wp-content/uploads/2019/03/maxresdefault-2.jpg",
            bannerName = "Banner 6",
            id = "6"
        )
    )

    // فراخوانی کامپوننت VerticalSlider با داده‌های نمونه
    VerticalSlider(
        modifier = Modifier.fillMaxSize(),
        imageUrl = sampleBanners
    )
}