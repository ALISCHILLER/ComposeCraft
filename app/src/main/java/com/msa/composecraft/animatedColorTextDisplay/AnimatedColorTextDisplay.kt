package com.msa.composecraft.animatedColorTextDisplay

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Shader
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// کامپوننت اصلی که نمایش متن انیمیشنی با تغییر رنگ را مدیریت می‌کند
@Composable
fun AnimatedColorTextDisplay(modifier: Modifier = Modifier) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        // این کامپوننت برای ایجاد و نمایش متن انیمیشنی استفاده می‌شود
        AnimatedTextWithTileModes()
    }
}

// این کامپوننت متن انیمیشنی را با افکت‌های tileMode مختلف نمایش می‌دهد
@Composable
fun AnimatedTextWithTileModes() {
    // وضعیت tileMode که برای تعیین شیوه نمایش افکت‌های رنگی استفاده می‌شود
    var tileMode by remember { mutableStateOf(TileMode.Clamp) }

    // ایجاد یک انتقال بی‌نهایت (infiniteTransition) برای انیمیشن
    val infiniteTransition = rememberInfiniteTransition(label = "infiniteTransition")

    // انیمیشن تغییر افکت رنگی به طور مداوم با تغییر موقعیت افکت رنگی
    val animatedOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "animatedOffset"
    )

    // ایجاد یک Shader برای متن که افکت رنگی را با توجه به انیمیشن به کار می‌برد
    val brush = remember(animatedOffset, tileMode) {
        object : ShaderBrush() {
            override fun createShader(size: Size): Shader {
                val widthOffset = size.width * animatedOffset
                val heightOffset = size.height * animatedOffset
                return LinearGradientShader(
                    colors = listOf(Color.Red, Color.Blue, Color.Green),
                    from = Offset(widthOffset, heightOffset),
                    to = Offset(widthOffset + size.width, heightOffset + size.height),
                    tileMode = tileMode // استفاده از tileMode برای تعیین نوع افکت
                )
            }
        }
    }

    // نمایش متن با افکت رنگی انیمیشنی
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        // متن انیمیشنی که با shader ساخته شده رنگ آمیزی می‌شود
        Text(
            text = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. " +
                    "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, " +
                    "when an unknown printer took a galley of type and scrambled it to make a type " +
                    "specimen book. It has survived not only five centuries, but also the leap " +
                    "into electronic typesetting, remaining essentially unchanged.",
            style = TextStyle(
                brush = brush,  // رنگ متن با استفاده از brush انیمیشنی تغییر می‌کند
                fontSize = 32.sp
            ),
            modifier = Modifier.padding(16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ایجاد دکمه‌ها برای تغییر tileMode
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            // دکمه‌ها برای انتخاب tileMode مختلف (Clamp, Mirror, Repeated, Decal)
            Button(onClick = { tileMode = TileMode.Clamp }) {
                Text(text = "Clamp")
            }
            Button(onClick = { tileMode = TileMode.Mirror }) {
                Text(text = "Mirror")
            }
            Button(onClick = { tileMode = TileMode.Repeated }) {
                Text(text = "Repeated")
            }
            Button(onClick = { tileMode = TileMode.Decal }) {
                Text(text = "Decal")
            }
        }
    }
}
