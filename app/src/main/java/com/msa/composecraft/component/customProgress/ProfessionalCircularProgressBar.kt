package com.msa.composecraft.component.customProgress

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun ProfessionalCircularProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    size: Dp = 150.dp,
    strokeWidth: Dp = 12.dp,
    backgroundColor: Color = Color.LightGray.copy(alpha = 0.2f),
    initialGradientColors: List<Color> = listOf(Color(0xFF6200EE), Color(0xFF03DAC6)),
    animationDuration: Int = 800,
    strokeCap: StrokeCap = StrokeCap.Round,
    contentColor: Color? = null,
    textStyle: TextStyle = TextStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp),
    showPercentage: Boolean = true,
    elevation: Dp = 10.dp,
    borderColor: Color = Color.Gray.copy(alpha = 0.3f),
    borderWidth: Dp = 2.dp,
    glowEnabled: Boolean = true,
    pulseEnabled: Boolean = true,
    gradientAnimationEnabled: Boolean = true,
    vibrationEnabled: Boolean = true,
    content: @Composable (animatedProgress: Float) -> Unit = { animatedProgress ->
        if (showPercentage) {
            val displayColor = contentColor ?: if (animatedProgress > 0.5f) Color.White else Color.Black
            Text(
                text = "${(animatedProgress * 100).toInt()}%",
                style = textStyle,
                color = displayColor
            )
        }
    }
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = animationDuration),
        label = "progress_animation"
    )

    val density = LocalDensity.current
    val strokePx = with(density) { strokeWidth.toPx() }
    val borderPx = with(density) { borderWidth.toPx() }

    // پالس درخشش
    val pulseAnim = rememberInfiniteTransition(label = "pulse")
    val glowAlpha by pulseAnim.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_alpha"
    )

    // 🎨 انیمیشن رنگ گرادیانت
    val colorAnim by pulseAnim.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "gradient_anim"
    )

    val animatedGradientColors = if (gradientAnimationEnabled) {
        val interpolatedColor1 = lerp(initialGradientColors[0], initialGradientColors[1], colorAnim)
        val interpolatedColor2 = lerp(initialGradientColors[1], initialGradientColors[0], colorAnim)
        listOf(interpolatedColor1, interpolatedColor2)
    } else {
        initialGradientColors
    }

    // 🌀 افکت لرزش (Vibration)
    val vibrationOffset = if (vibrationEnabled && (progress < 0.1f || progress > 0.95f)) {
        val vibration by pulseAnim.animateFloat(
            initialValue = -4f,
            targetValue = 4f,
            animationSpec = infiniteRepeatable(
                animation = tween(50, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "vibration_anim"
        )
        Offset(vibration, 0f)
    } else Offset.Zero

    Box(
        modifier = modifier
            .size(size)
            .offset { IntOffset(vibrationOffset.x.toInt(), vibrationOffset.y.toInt()) }
            .shadow(elevation, shape = CircleShape, clip = false)
            .background(Color.Transparent, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasSize = size.toPx()
            val center = Offset(canvasSize / 2, canvasSize / 2)
            val radius = (canvasSize / 2) - (strokePx / 2)
            val arcSize = Size(canvasSize - strokePx, canvasSize - strokePx)
            val topLeft = Offset(strokePx / 2, strokePx / 2)

            // پس‌زمینه
            drawArc(
                color = backgroundColor,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(strokePx)
            )

            // Glow Effect
            if (glowEnabled && pulseEnabled) {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(animatedGradientColors.last().copy(alpha = glowAlpha), Color.Transparent),
                        center = center,
                        radius = radius * 1.6f
                    ),
                    radius = radius * 1.6f,
                    center = center
                )
            }

            // قوس گرادیانت
            drawArc(
                brush = Brush.sweepGradient(
                    colors = animatedGradientColors,
                    center = center
                ),
                startAngle = -90f,
                sweepAngle = 360f * animatedProgress,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(strokePx, cap = strokeCap)
            )

            // حاشیه
            drawCircle(
                color = borderColor,
                radius = radius,
                center = center,
                style = Stroke(borderPx)
            )
        }

        content(animatedProgress)
    }
}
@Composable
@Preview
fun PreviewFinalCircularProgressBar() {
    var progress by remember { mutableStateOf(0.97f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ProfessionalCircularProgressBar(
            progress = progress,
            strokeWidth = 14.dp,
            size = 200.dp,
            initialGradientColors = listOf(Color.Cyan, Color.Magenta),
            glowEnabled = true,
            pulseEnabled = true,
            gradientAnimationEnabled = true,
            vibrationEnabled = true,
            contentColor = Color.White
        )

        Spacer(modifier = Modifier.height(30.dp))

        Button(onClick = {
            progress = Random.nextFloat()
        }) {
            Text("تغییر درصد تصادفی")
        }
    }
}
