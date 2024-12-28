package com.msa.composecraft.component.verticalSlider

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage

/**
 * این کامپوننت برای بارگذاری و کش تصاویر از URL استفاده می‌شود.
 * @param modifier: Modifier برای تنظیمات layout و استایل.
 * @param imageLink: URL تصویر برای بارگذاری.
 * @param token: توکن اختیاری برای احراز هویت (در اینجا استفاده نشده است).
 */
@Composable
fun LoadAndCacheImage(
    modifier: Modifier,
    imageLink: String,
    token: String
) {
    AsyncImage(
        modifier = modifier,
        model = imageLink,
        contentScale = ContentScale.FillBounds,
        contentDescription = "",
        onError = {
            println("-------- onError = $it ----------------")
        },
        onLoading = {
            println("-------- onLoading = $it ----------------")
        },
        onSuccess = {
            println("-------- onSuccess = $it ----------------")
        }
    )
}