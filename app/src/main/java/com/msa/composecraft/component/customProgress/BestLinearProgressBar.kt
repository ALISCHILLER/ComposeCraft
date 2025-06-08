package com.msa.composecraft.component.customProgress

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BestLinearProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    height: Dp = 24.dp,
    cornerRadius: Dp = 50.dp,
    backgroundColor: Color = Color.LightGray.copy(alpha = 0.3f),
    textColorInside: Color = Color.White,
    textColorOutside: Color = Color.Black,
    textStyle: TextStyle = TextStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp),
    showPercentInside: Boolean = true,
    showPercentOutside: Boolean = true,
    label: String? = null,
    animationDuration: Int = 700
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(animationDuration),
        label = "animated_progress"
    )

    // استفاده صحیح از linearGradient به جای horizontalGradient
    val progressBrush = Brush.linearGradient(
        colors = listOf(
            Color(0xFFD32F2F), // قرمز
            Color(0xFFFFC107), // زرد
            Color(0xFF388E3C)  // سبز
        ),
        start = Offset.Zero,
        end = Offset.Infinite
    )

    Column(modifier = modifier.fillMaxWidth()) {
        if (label != null) {
            Text(
                text = label,
                style = TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Color.DarkGray
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .clip(RoundedCornerShape(cornerRadius))
                .background(backgroundColor)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(animatedProgress)
                    .clip(RoundedCornerShape(cornerRadius))
                    .background(progressBrush)
            )

            if (showPercentInside && animatedProgress > 0.05f) {
                Text(
                    text = "${(animatedProgress * 100).toInt()}%",
                    color = textColorInside,
                    style = textStyle,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 12.dp)
                )
            }
        }

        if (showPercentOutside) {
            Text(
                text = "${(animatedProgress * 100).toInt()}%",
                style = textStyle,
                color = textColorOutside,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp, end = 6.dp),
                maxLines = 1,
                softWrap = false,
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
fun BestLinearProgressBarDemo() {
    var progress by remember { mutableStateOf(0.3f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BestLinearProgressBar(
            progress = progress,
            label = "دانلود فایل",
            modifier = Modifier.fillMaxWidth(),
            height = 28.dp,
            showPercentInside = true,
            showPercentOutside = true,
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(onClick = { if (progress < 1f) progress += 0.1f }) {
                Text("افزایش")
            }
            Button(onClick = { if (progress > 0f) progress -= 0.1f }) {
                Text("کاهش")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBestLinearProgressBar() {
    MaterialTheme {
        BestLinearProgressBarDemo()
    }
}