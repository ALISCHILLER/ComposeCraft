package com.msa.composecraft.circleProgressBar

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun CircularProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    size: Dp = 150.dp,
    strokeWidth: Float = 12f,
    trackColor: Color = Color.LightGray,
    progressColor: Color = Color.Blue,
    animationDuration: Int = 1000
) {
    // انیمیشن پیشرفت
    val animatedProgress = remember { Animatable(0f) }

    LaunchedEffect(progress) {
        animatedProgress.animateTo(
            targetValue = progress,
            animationSpec = tween(
                durationMillis = animationDuration,
                easing = FastOutSlowInEasing
            )
        )
    }

    Canvas(modifier = modifier.size(size)) {
        // دایره پس‌زمینه
        drawArc(
            color = trackColor,
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = false,
            style = Stroke(width = strokeWidth)
        )

        // دایره پیشرفت
        drawArc(
            color = progressColor,
            startAngle = -90f,
            sweepAngle = 360f * animatedProgress.value,
            useCenter = false,
            style = Stroke(width = strokeWidth)
        )
    }
}

@Composable
fun DailyCalorieProgress(
    breakfastProgress: Float,
    snackProgress: Float,
    lunchProgress: Float,
    dinnerProgress: Float,
    size: Dp = 150.dp,
    strokeWidth: Float = 12f
) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(size)) {
        Canvas(modifier = Modifier.size(size)) {
            // پیشرفت صبحانه
            drawArc(
                color = Color.Green,
                startAngle = -90f,
                sweepAngle = 360f * breakfastProgress,
                useCenter = false,
                style = Stroke(width = strokeWidth)
            )

            // پیشرفت میان وعده
            drawArc(
                color = Color.Yellow,
                startAngle = -90f + 360f * breakfastProgress,
                sweepAngle = 360f * snackProgress,
                useCenter = false,
                style = Stroke(width = strokeWidth)
            )

            // پیشرفت ناهار
            drawArc(
                color = Color.Red,
                startAngle = -90f + 360f * (breakfastProgress + snackProgress),
                sweepAngle = 360f * lunchProgress,
                useCenter = false,
                style = Stroke(width = strokeWidth)
            )

            // پیشرفت شام
            drawArc(
                color = Color.Blue,
                startAngle = -90f + 360f * (breakfastProgress + snackProgress + lunchProgress),
                sweepAngle = 360f * dinnerProgress,
                useCenter = false,
                style = Stroke(width = strokeWidth)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CircularProgressBarPreview() {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
    CircularProgressBar(progress = 0.75f)
    }
}

@Preview(showBackground = true)
@Composable
fun DailyCalorieProgressPreview() {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        DailyCalorieProgress(
            breakfastProgress = 0.3f,
            snackProgress = 0.2f,
            lunchProgress = 0.25f,
            dinnerProgress = 0.25f
        )
    }
}