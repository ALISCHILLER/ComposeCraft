package com.msa.composecraft.component.customProgress
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import kotlinx.coroutines.delay
import androidx.compose.animation.core.*
import kotlin.random.Random


enum class ProgressStatus {
    SUCCESS, WARNING, ERROR, INFO, DEFAULT
}
fun gradientForStatus(status: ProgressStatus): List<Color> = when (status) {
    ProgressStatus.SUCCESS -> listOf(Color(0xFF4CAF50), Color(0xFF81C784)) // سبز
    ProgressStatus.WARNING -> listOf(Color(0xFFFFC107), Color(0xFFFFD54F)) // زرد
    ProgressStatus.ERROR   -> listOf(Color(0xFFF44336), Color(0xFFE57373)) // قرمز
    ProgressStatus.INFO    -> listOf(Color(0xFF2196F3), Color(0xFF64B5F6)) // آبی
    ProgressStatus.DEFAULT -> listOf(Color(0xFF6200EE), Color(0xFF03DAC6)) // بنفش/آبی
}



@Composable
fun StatusCircularProgressBar(
    progress: Float,
    status: ProgressStatus = ProgressStatus.DEFAULT,
    size: Dp = 150.dp,
    strokeWidth: Dp = 14.dp,
    showPercentage: Boolean = true,
    modifier: Modifier = Modifier
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 800),
        label = "animated_progress"
    )

    val pulseAlpha = remember { Animatable(0.4f) }
    LaunchedEffect(Unit) {
        while (true) {
            pulseAlpha.animateTo(
                0.9f,
                animationSpec = tween(800, easing = FastOutSlowInEasing)
            )
            pulseAlpha.animateTo(
                0.4f,
                animationSpec = tween(800, easing = FastOutSlowInEasing)
            )
        }
    }

    val strokePx = with(LocalDensity.current) { strokeWidth.toPx() }
    val gradient = gradientForStatus(status)

    Box(
        modifier = modifier
            .size(size)
            .shadow(8.dp, CircleShape)
            .background(Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val canvasSize = size.toPx()
            val center = Offset(canvasSize / 2, canvasSize / 2)
            val radius = (canvasSize / 2) - strokePx / 2

            // درخشش پالس دور دایره
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(gradient.last().copy(alpha = pulseAlpha.value), Color.Transparent),
                    center = center,
                    radius = radius * 1.3f
                ),
                radius = radius * 1.3f,
                center = center
            )

            // دایره پس‌زمینه
            drawCircle(
                color = Color.LightGray.copy(alpha = 0.2f),
                radius = radius,
                center = center,
                style = Stroke(strokePx)
            )

            // دایره پیشرفت
            drawArc(
                brush = Brush.sweepGradient(gradient, center),
                startAngle = -90f,
                sweepAngle = 360f * animatedProgress,
                useCenter = false,
                topLeft = Offset(0f, 0f),
                size = Size(canvasSize, canvasSize),
                style = Stroke(width = strokePx, cap = StrokeCap.Round)
            )
        }

        // محتوای داخلی (درصد یا چیز دیگر)
        if (showPercentage) {
            Text(
                text = "${(animatedProgress * 100).toInt()}%",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = when (status) {
                    ProgressStatus.SUCCESS -> Color(0xFF4CAF50)
                    ProgressStatus.WARNING -> Color(0xFFFFA000)
                    ProgressStatus.ERROR -> Color(0xFFD32F2F)
                    ProgressStatus.INFO -> Color(0xFF1976D2)
                    ProgressStatus.DEFAULT -> Color.White
                }
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun Preview_StatusCircularProgressBar() {
    var progress by remember { mutableStateOf(0.55f) }
    var status by remember { mutableStateOf(ProgressStatus.DEFAULT) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        StatusCircularProgressBar(
            progress = progress,
            status = status,
            showPercentage = true
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = { status = ProgressStatus.SUCCESS }) { Text("Success") }
            Button(onClick = { status = ProgressStatus.WARNING }) { Text("Warning") }
            Button(onClick = { status = ProgressStatus.ERROR }) { Text("Error") }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = { status = ProgressStatus.INFO }) { Text("Info") }
            Button(onClick = { progress = Random.nextFloat() }) { Text("Random") }
        }
    }
}
