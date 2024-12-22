package com.msa.composecraft.animatedButtonShowcase

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

// کامپوزبل اصلی برای نمایش انیمیشن‌های دکمه‌های رنگی
@Composable
fun AnimatedButtonShowcase(modifier: Modifier = Modifier) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        // نمایش ستون دکمه‌های انیمیشنی در داخل Scaffold
        AnimatedButtonList(
            modifier = Modifier.padding(innerPadding)
        )
    }
}

// کامپوزبل برای نمایش دکمه‌ها در یک ستون با انیمیشن‌های مختلف
@Composable
fun AnimatedButtonList(modifier: Modifier = Modifier) {
    // تعریف رنگ‌های گرادیان برای دکمه‌ها
    val buttonColors = listOf(
        listOf(Color(0xFFff7e5f), Color(0xFFfeb47b)),
        listOf(Color(0xFF6a11cb), Color(0xFF2575fc)),
        listOf(Color(0xFF00c6ff), Color(0xFF0072ff)),
        listOf(Color(0xFFf7971e), Color(0xFFffd200)),
        listOf(Color(0xFFff5f6d), Color(0xFFffc371)),
        listOf(Color(0xFF2193b0), Color(0xFF6dd5ed)),
        listOf(Color(0xFFcc2b5e), Color(0xFF753a88)),
        listOf(Color(0xFFee9ca7), Color(0xFFffdde1))
    )

    // ستون دکمه‌ها با رنگ‌های گرادیان و انیمیشن‌های مختلف
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // برای هر رنگ گرادیان یک دکمه انیمیشنی با نوع انیمیشن مختلف نمایش داده می‌شود
        buttonColors.forEachIndexed { index, colors ->
            AnimatedButton(
                text = "Button ${index + 1}",
                gradientColors = colors,
                // تعیین نوع انیمیشن بر اساس ایندکس
                animationType = when (index % 4) {
                    0 -> AnimationType.Scale
                    1 -> AnimationType.Rotate
                    2 -> AnimationType.Fade
                    else -> AnimationType.Shake
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// تعریف نوع‌های مختلف انیمیشن برای دکمه‌ها
enum class AnimationType {
    Scale, Rotate, Fade, Shake
}

// کامپوزبل برای نمایش دکمه با انیمیشن خاص
@Composable
fun AnimatedButton(
    text: String,
    gradientColors: List<Color>,
    animationType: AnimationType
) {
    // راه‌اندازی coroutineScope برای انجام انیمیشن‌ها
    val coroutineScope = rememberCoroutineScope()

    // تعریف متغیرهای انیماتور برای هر نوع انیمیشن
    val scaleAnim = remember { Animatable(1f) }
    val rotateAnim = remember { Animatable(0f) }
    val alphaAnim = remember { Animatable(1f) }
    val offsetXAnim = remember { Animatable(0f) }

    // دکمه با انیمیشن‌های مختلف
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(12.dp),
                clip = false
            )
            .background(
                brush = Brush.linearGradient(colors = gradientColors),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable {
                // انجام انیمیشن‌ها هنگام کلیک بر روی دکمه
                coroutineScope.launch {
                    when (animationType) {
                        AnimationType.Scale -> {
                            // انیمیشن مقیاس برای بزرگ و کوچک شدن دکمه
                            scaleAnim.animateTo(
                                targetValue = 1.2f,
                                animationSpec = tween(durationMillis = 100)
                            )
                            scaleAnim.animateTo(
                                targetValue = 1f,
                                animationSpec = tween(durationMillis = 100)
                            )
                        }
                        AnimationType.Rotate -> {
                            // انیمیشن چرخش دکمه
                            rotateAnim.animateTo(
                                targetValue = 15f,
                                animationSpec = tween(durationMillis = 100)
                            )
                            rotateAnim.animateTo(
                                targetValue = -15f,
                                animationSpec = tween(durationMillis = 100)
                            )
                            rotateAnim.animateTo(
                                targetValue = 0f,
                                animationSpec = tween(durationMillis = 100)
                            )
                        }
                        AnimationType.Fade -> {
                            // انیمیشن محو شدن و نمایان شدن دکمه
                            alphaAnim.animateTo(
                                targetValue = 0f,
                                animationSpec = tween(durationMillis = 100)
                            )
                            alphaAnim.animateTo(
                                targetValue = 1f,
                                animationSpec = tween(durationMillis = 100)
                            )
                        }
                        AnimationType.Shake -> {
                            // انیمیشن لرزش دکمه
                            val shakeValues = listOf(
                                0f, 25f, -25f, 20f, -20f, 15f, -15f, 10f, -10f, 5f, -5f, 0f
                            )
                            shakeValues.forEach { value ->
                                offsetXAnim.animateTo(
                                    targetValue = value,
                                    animationSpec = tween(durationMillis = 50)
                                )
                            }
                        }
                    }
                }
            }
            .scale(scaleAnim.value)
            .rotate(rotateAnim.value)
            .alpha(alphaAnim.value)
            .offset(x = offsetXAnim.value.dp)
    ) {
        // نمایش متن دکمه
        Text(
            text = text,
            color = Color.White,
            modifier = Modifier.padding(8.dp)
        )
    }
}
