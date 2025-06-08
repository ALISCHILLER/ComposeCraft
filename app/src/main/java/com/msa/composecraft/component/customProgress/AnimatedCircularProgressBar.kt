package com.msa.composecraft.component.customProgress

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*  // برای size(), fillMaxSize(), Spacer و غیره
import androidx.compose.material3.*
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import kotlin.math.min  // import مهم برای min()



@Composable
fun AnimatedCircularProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    sizeDp: Dp = 150.dp,
    strokeWidthDp: Dp = 16.dp,
    backgroundColor: Color = Color.Gray.copy(alpha = 0.2f),
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 700),
        label = "animated_progress"
    )

    Box(
        modifier = modifier.size(sizeDp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokePx = strokeWidthDp.toPx()
            val canvasSize = size
            // استفاده از min به جای size.minDimension
            val radius = min(canvasSize.width, canvasSize.height) / 2 - strokePx / 2
            val cx = canvasSize.width / 2
            val cy = canvasSize.height / 2

            // دایره پس‌زمینه
            drawCircle(
                color = backgroundColor,
                radius = radius,
                center = Offset(cx, cy),
                style = Stroke(strokePx, cap = StrokeCap.Round)
            )

            // قوس پیشرفت
            drawArc(
                color = getProgressColor(animatedProgress),
                startAngle = -90f,
                sweepAngle = 360 * animatedProgress,
                useCenter = false,
                topLeft = Offset(cx - radius, cy - radius),
                size = Size(radius * 2, radius * 2),
                style = Stroke(strokePx, cap = StrokeCap.Round)
            )
        }

        // درصد پیشرفت
        Text(
            text = "${(animatedProgress * 100).toInt()}%",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = getProgressColor(animatedProgress)
        )
    }
}

fun getProgressColor(progress: Float): Color {
    return when {
        progress < 0.3f -> Color.Red
        progress < 0.7f -> Color.Yellow
        else -> Color(0xFF00C853)
    }
}

@Composable
fun CircularProgressBarScreen() {
    var progress by remember { mutableStateOf(0.5f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Circular Progress",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(32.dp))

        AnimatedCircularProgressBar(progress = progress)

        Spacer(modifier = Modifier.height(32.dp))

        Text(text = "Adjust Progress", color = Color.White)

        Slider(
            value = progress,
            onValueChange = { progress = it },
            valueRange = 0f..1f,
            colors = SliderDefaults.colors(
                thumbColor = getProgressColor(progress),
                activeTrackColor = getProgressColor(progress),
                inactiveTrackColor = Color.DarkGray
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCircularProgressBar() {
    MaterialTheme {
        CircularProgressBarScreen()
    }
}
