@file:OptIn(ExperimentalMaterial3Api::class)


package com.msa.composecraft.component.bottomSheet


import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * یک کامپوننت پیشرفته برای نمایش BottomSheet با انیمیشن سفارشی.
 *
 * @param modifier Modifier برای تنظیمات layout.
 * @param onDismissRequest callback هنگام درخواست بسته شدن BottomSheet.
 * @param content محتوای داخلی BottomSheet.
 * @param animationColor رنگ انیمیشن دایره‌ای.
 * @param animationSize اندازه انیمیشن دایره‌ای.
 */
@Composable
fun AdvancedBottomSheet(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
    animationColor: Color = Color.Red,
    animationSize: Float = 200f
) {
    val modalBottomSheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showAnimation by remember { mutableStateOf(false) }

    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = {
            showAnimation = true
            onDismissRequest()
        },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        content()
    }

    if (showAnimation) {
        CircularDismissAnimation(
            onAnimationEnd = {
                showAnimation = false
                onDismissRequest()
            },
            color = animationColor,
            size = animationSize
        )
    }
}

/**
 * یک انیمیشن دایره‌ای سفارشی.
 *
 * @param onAnimationEnd callback پس از پایان انیمیشن.
 * @param color رنگ انیمیشن.
 * @param size اندازه انیمیشن.
 */
@Composable
fun CircularDismissAnimation(
    onAnimationEnd: () -> Unit,
    color: Color = Color.Red,
    size: Float = 200f
) {
    val animatableSize = remember { Animatable(1f) }
    val animatableYOffset = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        // اجرای انیمیشن‌ها به طور همزمان
        launch {
            animatableSize.animateTo(
                targetValue = 0f,
                animationSpec = tween(500, easing = FastOutSlowInEasing)
            )
        }
        launch {
            animatableYOffset.animateTo(
                targetValue = 2000f,
                animationSpec = tween(500, easing = FastOutSlowInEasing)
            )
        }

        // منتظر ماندن تا هر دو انیمیشن به پایان برسند
        delay(500)
        onAnimationEnd()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Canvas(
            modifier = Modifier
                .size(size.dp * animatableSize.value)
                .offset { IntOffset(0, animatableYOffset.value.toInt()) }
                .clip(CircleShape),
            onDraw = {
                val circleSize = animatableSize.value * size
                drawRoundRect(
                    color = color,
                    topLeft = Offset.Zero,
                    size = Size(circleSize, circleSize),
                    cornerRadius = CornerRadius(circleSize / 2, circleSize / 2)
                )
            }
        )
    }
}

/**
 * یک لیست از کشورها با پرچم‌های مربوطه.
 */
@Composable
fun CountryList() {
    val countries = listOf(
        Pair("United States", "\uD83C\uDDFA\uD83C\uDDF8"),
        Pair("Canada", "\uD83C\uDDE8\uD83C\uDDE6"),
        Pair("India", "\uD83C\uDDEE\uD83C\uDDF3"),
        Pair("Germany", "\uD83C\uDDE9\uD83C\uDDEA"),
        Pair("France", "\uD83C\uDDEB\uD83C\uDDF7"),
        Pair("Japan", "\uD83C\uDDEF\uD83C\uDDF5"),
        Pair("China", "\uD83C\uDDE8\uD83C\uDDF3"),
        Pair("Brazil", "\uD83C\uDDE7\uD83C\uDDF7"),
        Pair("Australia", "\uD83C\uDDE6\uD83C\uDDFA"),
        Pair("Russia", "\uD83C\uDDF7\uD83C\uDDFA"),
        Pair("Russia", "\uD83C\uDDF7\uD83C\uDDFA"),
        Pair("Russia", "\uD83C\uDDF7\uD83C\uDDFA"),
        Pair("Russia", "\uD83C\uDDF7\uD83C\uDDFA"),
        Pair("Russia", "\uD83C\uDDF7\uD83C\uDDFA"),
        Pair("Russia", "\uD83C\uDDF7\uD83C\uDDFA"),
        Pair("Russia", "\uD83C\uDDF7\uD83C\uDDFA"),
        Pair("Russia", "\uD83C\uDDF7\uD83C\uDDFA"),
        Pair("Russia", "\uD83C\uDDF7\uD83C\uDDFA"),
        Pair("Russia", "\uD83C\uDDF7\uD83C\uDDFA"),
        Pair("United Kingdom", "\uD83C\uDDEC\uD83C\uDDE7"),
    )

    LazyColumn {
        items(countries) { (country, flag) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp, horizontal = 20.dp)
            ) {
                Text(
                    text = flag,
                    modifier = Modifier.padding(end = 20.dp)
                )
                Text(text = country)
            }
        }
    }
}

@Preview
@Composable
private fun AdvancedBottomSheetPreview() {
    AdvancedBottomSheet(
        onDismissRequest = { /* Handle dismiss */ },
        content = {
            // محتوای BottomSheet
            CountryList()
        }
    )
}