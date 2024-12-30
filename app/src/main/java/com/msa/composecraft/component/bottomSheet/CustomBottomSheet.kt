@file:OptIn(ExperimentalMaterial3Api::class)

package com.msa.composecraft.component.bottomSheet

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/**
 * یک BottomSheet پیشرفته و قابل کنترل.
 *
 * @param modifier Modifier برای تنظیمات layout.
 * @param sheetState وضعیت BottomSheet.
 * @param sheetBackgroundColor رنگ پس‌زمینه BottomSheet.
 * @param sheetShape شکل گوشه‌های BottomSheet.
 * @param sheetElevation ارتفاع سایه BottomSheet.
 * @param onDismissRequest callback هنگام درخواست بسته شدن BottomSheet.
 * @param content محتوای داخلی BottomSheet.
 */
@Composable
fun CustomBottomSheet(
    modifier: Modifier = Modifier,
    sheetState: CustomBottomSheetState,
    sheetBackgroundColor: Color = MaterialTheme.colorScheme.surface,
    sheetShape: RoundedCornerShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
    sheetElevation: Dp = 8.dp,
    onDismissRequest: () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
) {
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()

    // محاسبه ارتفاع BottomSheet بر اساس وضعیت آن
    val sheetHeight = with(density) {
        when (sheetState.currentState) {
            CustomBottomSheetState.State.Collapsed -> sheetState.collapsedHeight.toPx()
            CustomBottomSheetState.State.Expanded -> sheetState.expandedHeight.toPx()
            CustomBottomSheetState.State.Hidden -> 0f
        }
    }

    // انیمیشن برای تغییر ارتفاع BottomSheet
    val animatedHeight by animateFloatAsState(
        targetValue = sheetHeight,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "sheetHeightAnimation"
    )

    // نمایش BottomSheet فقط اگر مخفی نباشد
    if (sheetState.currentState != CustomBottomSheetState.State.Hidden) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(animatedHeight.dp)
                .background(sheetBackgroundColor, sheetShape)
                .shadow(sheetElevation, sheetShape)
                .pointerInput(Unit) {
                    detectTapGestures {
                        scope.launch {
                            when (sheetState.currentState) {
                                CustomBottomSheetState.State.Collapsed -> sheetState.expand()
                                CustomBottomSheetState.State.Expanded -> sheetState.collapse()
                                CustomBottomSheetState.State.Hidden -> {}
                            }
                        }
                    }
                }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                content()
            }

            // دکمه بستن BottomSheet
            IconButton(
                onClick = {
                    scope.launch {
                        sheetState.hide()
                        onDismissRequest()
                    }
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "بستن"
                )
            }
        }
    }
}

/**
 * وضعیت BottomSheet پیشرفته.
 *
 * @param collapsedHeight ارتفاع BottomSheet در حالت بسته.
 * @param expandedHeight ارتفاع BottomSheet در حالت باز.
 */
class CustomBottomSheetState(
    val collapsedHeight: Dp,
    val expandedHeight: Dp
) {
    enum class State {
        Collapsed, Expanded, Hidden
    }

    var currentState by mutableStateOf(State.Collapsed)
        private set

    fun expand() {
        currentState = State.Expanded
    }

    fun collapse() {
        currentState = State.Collapsed
    }

    fun hide() {
        currentState = State.Hidden
    }
}

@Composable
fun rememberAdvancedBottomSheetState(
    collapsedHeight: Dp = 100.dp,
    expandedHeight: Dp = 400.dp
): CustomBottomSheetState {
    return remember {
        CustomBottomSheetState(collapsedHeight, expandedHeight)
    }
}

@Preview
@Composable
private fun CustomBottomSheetPreview() {
    val sheetState = rememberAdvancedBottomSheetState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
    ) {
        Button(
            onClick = { sheetState.expand() },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("نمایش BottomSheet")
        }

        CustomBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { sheetState.hide() },
            content = {
                Text("این یک BottomSheet پیشرفته است!")
            }
        )
    }
}