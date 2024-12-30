package com.msa.eshop.ui.component.button

import androidx.compose.animation.core.EaseOutSine
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * یک کامپوننت انعطاف‌پذیر برای ایجاد انیمیشن حاشیه‌ای.
 *
 * @param cardShape شکل گوشه‌های کامپوننت.
 * @param borderWidth عرض حاشیه.
 * @param loading آیا انیمیشن فعال باشد.
 * @param backgroundColor رنگ پس‌زمینه.
 * @param borderColors لیست رنگ‌های گرادیان حاشیه.
 * @param content محتوای داخلی کامپوننت.
 */
@Composable
fun BorderAnimation(
    cardShape: RoundedCornerShape,
    borderWidth: Dp,
    loading: Boolean,
    backgroundColor: Color,
    borderColors: List<Color>,
    content: @Composable () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "infiniteTransition")
    val rotate by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = EaseOutSine
            )
        ),
        label = "rotateAnimation"
    )

    Box(
        modifier = Modifier
            .wrapContentSize()
            .padding(borderWidth)
            .clip(cardShape)
            .drawWithContent {
                rotate(
                    degrees = if (loading) rotate else 0f
                ) {
                    drawCircle(
                        brush = if (loading) {
                            Brush.sweepGradient(borderColors)
                        } else {
                            Brush.linearGradient(listOf(backgroundColor, backgroundColor))
                        },
                        radius = size.width,
                        blendMode = BlendMode.SrcIn,
                    )
                }
                drawContent()
            }
            .background(backgroundColor, cardShape)
    ) {
        content()
    }
}

/**
 * یک دکمه با انیمیشن حاشیه‌ای.
 *
 * @param modifier Modifier برای تنظیمات layout.
 * @param text متن دکمه.
 * @param enabled آیا دکمه فعال باشد.
 * @param loading آیا انیمیشن بارگذاری فعال باشد.
 * @param onClick callback هنگام کلیک روی دکمه.
 * @param buttonColors رنگ‌های دکمه.
 * @param borderColors لیست رنگ‌های گرادیان حاشیه.
 * @param shape شکل دکمه.
 */
@Composable
fun ButtonBorderAnimation(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    loading: Boolean = false,
    onClick: () -> Unit,
    buttonColors: ButtonColors = ButtonDefaults.buttonColors(),
    borderColors: List<Color> = listOf(Color(0xFFFF5252), Color(0xFF42A5F5), Color(0xFFFF5252)),
    shape: RoundedCornerShape = RoundedCornerShape(6.dp)
) {
    BorderAnimation(
        cardShape = shape,
        borderWidth = 6.dp,
        loading = loading,
        backgroundColor = buttonColors.containerColor,
        borderColors = borderColors
    ) {
        Button(
            modifier = modifier,
            enabled = enabled && !loading,
            shape = shape,
            colors = buttonColors,
            onClick = onClick
        ) {
            Text(
                modifier = Modifier.padding(6.dp),
                text = if (loading) "Loading..." else text,
                color = buttonColors.contentColor
            )
        }
    }
}

@Preview
@Composable
private fun PreviewButtonBorderAnimation() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ButtonBorderAnimation(
            text = "Submit",
            loading = false,
            onClick = { /* Handle click */ },
            buttonColors = ButtonDefaults.buttonColors(
                containerColor = Color.Blue,
                contentColor = Color.White
            ),
            borderColors = listOf(Color.Red, Color.Yellow, Color.Green),
            shape = RoundedCornerShape(12.dp)
        )
    }
}