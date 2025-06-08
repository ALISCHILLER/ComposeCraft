package com.msa.composecraft.component.customProgress

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.min
import kotlin.random.Random

enum class ProgressStatusLinear {
    SUCCESS, WARNING, ERROR, INFO, DEFAULT
}

fun linearGradientForStatus(status: ProgressStatus): List<Color> = when (status) {
    ProgressStatus.SUCCESS -> listOf(Color(0xFF4CAF50), Color(0xFF81C784))
    ProgressStatus.WARNING -> listOf(Color(0xFFFFC107), Color(0xFFFFD54F))
    ProgressStatus.ERROR   -> listOf(Color(0xFFF44336), Color(0xFFE57373))
    ProgressStatus.INFO    -> listOf(Color(0xFF2196F3), Color(0xFF64B5F6))
    ProgressStatus.DEFAULT -> listOf(Color(0xFF6200EE), Color(0xFF03DAC6))
}

@Composable
fun AnimatedLinearProgressBar(
    progress: Float,
    status: ProgressStatus = ProgressStatus.DEFAULT,
    modifier: Modifier = Modifier,
    height: Dp = 24.dp,
    showPercentage: Boolean = true,
    percentageOnTop: Boolean = true,
    indeterminate: Boolean = false,
    animationDuration: Int = 900,
    elevation: Dp = 4.dp,
    cornerRadius: Dp = 50.dp,
    enableShimmer: Boolean = true,
    enableShadow: Boolean = true,
    contentColor: Color = Color.White,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium.copy(
        fontWeight = FontWeight.Medium,
        color = contentColor
    )
) {
    val animatedProgress by animateFloatAsState(
        targetValue = if (indeterminate) 1f else progress.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = animationDuration, easing = FastOutSlowInEasing),
        label = "animatedProgress"
    )

    val gradientColors = linearGradientForStatus(status)

    val density = LocalDensity.current
    val shimmerWidth = with(density) { 100.dp.toPx() }

    // شاین متحرک
    val infiniteTransition = rememberInfiniteTransition(label = "shimmerTransition")
    val shimmerTranslate by infiniteTransition.animateFloat(
        initialValue = -shimmerWidth,
        targetValue = shimmerWidth * 2,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerTranslate"
    )

    Column(modifier = modifier.fillMaxWidth()) {
        if (showPercentage && percentageOnTop) {
            Text(
                text = "${if (indeterminate) "..." else "${(animatedProgress * 100).toInt()}%"}",
                style = textStyle,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .clip(RoundedCornerShape(cornerRadius))
                .background(Color.Gray.copy(alpha = 0.1f))
                .let {
                    if (enableShadow) it.shadow(elevation, RoundedCornerShape(cornerRadius)) else it
                }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(if (indeterminate) 1f else animatedProgress)
                    .clip(RoundedCornerShape(cornerRadius))
                    .background(Brush.linearGradient(gradientColors))
                    .drawWithContent {
                        drawContent()
                        if (enableShimmer) {
                            val shimmerStart = shimmerTranslate
                            drawRect(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.White.copy(alpha = 0.2f),
                                        Color.Transparent
                                    ),
                                    start = Offset(shimmerStart, 0f),
                                    end = Offset(shimmerStart + shimmerWidth, size.height)
                                )
                            )
                        }
                    }
            )

            if (showPercentage && !percentageOnTop) {
                Text(
                    text = "${(animatedProgress * 100).toInt()}%",
                    style = textStyle,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .align(Alignment.CenterStart)
                )
            }
        }

        if (showPercentage && percentageOnTop && indeterminate) {
            Text(
                text = "...",
                style = textStyle,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
fun PreviewAnimatedLinearProgressBar() {
    var progress by remember { mutableStateOf(0.65f) }
    var status by remember { mutableStateOf(ProgressStatus.INFO) }
    var indeterminate by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        AnimatedLinearProgressBar(
            progress = progress,
            status = status,
            height = 24.dp,
            showPercentage = true,
            percentageOnTop = true,
            indeterminate = indeterminate,
            enableShimmer = true,
            enableShadow = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = { status = ProgressStatus.SUCCESS }) { Text("Success") }
            Button(onClick = { status = ProgressStatus.WARNING }) { Text("Warning") }
            Button(onClick = { status = ProgressStatus.ERROR }) { Text("Error") }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = { status = ProgressStatus.INFO }) { Text("Info") }
            Button(onClick = { progress = Random.nextFloat() }) { Text("Random") }
            Button(onClick = { indeterminate = !indeterminate }) {
                Text(if (indeterminate) "Determin" else "Indetermin")
            }
        }
    }
}