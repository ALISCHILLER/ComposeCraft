package com.msa.composecraft.component.coil

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import com.msa.composecraft.R

/**
 * این تابع Composable یک تصویر را با استفاده از Coil 3 بارگذاری می‌کند.
 * @param imageUrl آدرس تصویر برای بارگذاری.
 * @param modifier Modifier برای تنظیمات layout و استایل.
 * @param contentScale نحوه مقیاس‌گذاری تصویر (پیش‌فرض: ContentScale.Crop).
 * @param placeholder تصویر placeholder که هنگام بارگذاری نمایش داده می‌شود.
 * @param error تصویری که در صورت خطا در بارگذاری نمایش داده می‌شود.
 * @param showLoadingIndicator نمایش یک نشانگر بارگذاری (پیش‌فرض: true).
 * @param onLoading Callback برای زمانی که تصویر در حال بارگذاری است.
 * @param onSuccess Callback برای زمانی که تصویر با موفقیت بارگذاری شد.
 * @param onError Callback برای زمانی که خطایی در بارگذاری تصویر رخ داد.
 */
@Composable
fun CoilImage(
    imageUrl: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    placeholder: Painter? = null,
    error: Painter? = null,
    showLoadingIndicator: Boolean = true,
    onLoading: (() -> Unit)? = null,
    onSuccess: (() -> Unit)? = null,
    onError: (() -> Unit)? = null
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .build(),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = contentScale,
            loading = {
                if (showLoadingIndicator) {
                    CircularProgressIndicator()
                } else if (placeholder != null) {
                    SubcomposeAsyncImageContent(painter = placeholder)
                }
            },
            error = {
                if (error != null) {
                    SubcomposeAsyncImageContent(painter = error)
                }
            },
            onLoading = { onLoading?.invoke() },
            onSuccess = { onSuccess?.invoke() },
            onError = { onError?.invoke() }
        )
    }
}

/**
 * این تابع Composable یک Preview حرفه‌ای برای CoilImage ایجاد می‌کند.
 * در این Preview، چندین حالت مختلف از CoilImage نمایش داده می‌شود.
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewCoilImage() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // حالت ۱: بارگذاری موفق
        Text(
            text = "بارگذاری موفق",
            modifier = Modifier.padding(bottom = 8.dp)
        )
        CoilImage(
            imageUrl = "https://via.placeholder.com/300",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Crop
        )

        // حالت ۲: بارگذاری با Placeholder
        Text(
            text = "بارگذاری با Placeholder",
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )
        CoilImage(
            imageUrl = "https://invalid-url.com/image.jpg", // URL نامعتبر
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            placeholder = painterResource(id = R.drawable.not_load_image), // Placeholder
            contentScale = ContentScale.Crop
        )

        // حالت ۳: بارگذاری با خطا و Error Image
        Text(
            text = "بارگذاری با خطا و Error Image",
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )
        CoilImage(
            imageUrl = "https://invalid-url.com/image.jpg", // URL نامعتبر
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            error = painterResource(id = R.drawable.not_load_image), // Error Image
            contentScale = ContentScale.Crop
        )
    }
}


@Composable
fun CoilImage2(
    imageUrl: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    placeholder: Painter? = null,
    error: Painter? = null,
    showLoadingIndicator: Boolean = true,
    onLoading: (() -> Unit)? = null,
    onSuccess: (() -> Unit)? = null,
    onError: (() -> Unit)? = null
) {
    val painter = rememberAsyncImagePainter(
        model = imageUrl,
        onLoading = { onLoading?.invoke() },
        onSuccess = { onSuccess?.invoke() },
        onError = { onError?.invoke() },
        placeholder = placeholder,
        error = error
    )

    // جمع‌آوری وضعیت به عنوان یک State
    val state = painter.state.collectAsState()

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = contentScale
        )

        // نمایش نشانگر بارگذاری
        if (showLoadingIndicator && placeholder == null && state.value is AsyncImagePainter.State.Loading) {
            CircularProgressIndicator()
        }
    }
}





/**
 * این تابع Composable یک Preview جامع برای CoilImage2 ایجاد می‌کند.
 * در این Preview، چندین حالت مختلف از CoilImage2 نمایش داده می‌شود.
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewCoilImage2() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // حالت ۱: بارگذاری موفق
        Text(
            text = "بارگذاری موفق",
            modifier = Modifier.padding(bottom = 8.dp)
        )
        CoilImage2(
            imageUrl = "https://via.placeholder.com/300",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Crop
        )

        // حالت ۲: بارگذاری با Placeholder
        Text(
            text = "بارگذاری با Placeholder",
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )
        CoilImage2(
            imageUrl = "https://invalid-url.com/image.jpg", // URL نامعتبر
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            placeholder = painterResource(id = R.drawable.not_load_image), // Placeholder
            contentScale = ContentScale.Crop
        )

        // حالت ۳: بارگذاری با خطا و Error Image
        Text(
            text = "بارگذاری با خطا و Error Image",
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )
        CoilImage2(
            imageUrl = "https://invalid-url.com/image.jpg", // URL نامعتبر
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            error = painterResource(id = R.drawable.not_load_image), // Error Image
            contentScale = ContentScale.Crop
        )
    }
}