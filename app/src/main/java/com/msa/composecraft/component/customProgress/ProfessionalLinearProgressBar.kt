package com.msa.composecraft.component.customProgress

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfessionalLinearProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    height: Dp = 24.dp,
    cornerRadius: Dp = 10.dp,
    backgroundColor: Color = Color.LightGray.copy(alpha = 0.2f),
    gradientColors: List<Color> = listOf(Color(0xFF6200EE), Color(0xFF03DAC6)),
    showPercentageInside: Boolean = true,
    showPercentageOutside: Boolean = true,
    contentColor: Color = Color.White,
    textStyle: TextStyle = TextStyle(fontWeight = FontWeight.Bold, fontSize = 14.sp),
    animationDuration: Int = 700
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(animationDuration)
    )

    val infiniteTransition = rememberInfiniteTransition()
    val shimmerOffset by infiniteTransition.animateFloat(
        initialValue = -100f,
        targetValue = 100f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(modifier = modifier.fillMaxWidth()) {
        // 🔲 نوار پس‌زمینه
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .clip(RoundedCornerShape(cornerRadius))
                .background(backgroundColor)
        )

        // 🎨 نوار پیشرفت با گرادیانت
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(animatedProgress)
                .clip(RoundedCornerShape(cornerRadius))
                .background(
                    Brush.linearGradient(
                        colors = gradientColors,
                        start = Offset.Zero,
                        end = Offset.Infinite
                    )
                )
        )

        // 💬 نمایش درصد داخل ProgressBar
        if (showPercentageInside && animatedProgress > 0.05f) {
            Text(
                text = "${(animatedProgress * 100).toInt()}%",
                style = textStyle.copy(color = contentColor),
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 12.dp)
            )
        }
    }

    if (showPercentageOutside) {
        Text(
            text = "${(animatedProgress * 100).toInt()}%",
            style = textStyle.copy(color = contentColor),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
                .wrapContentSize(Alignment.TopEnd)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun Preview_ProfessionalLinearProgressBar() {
    var progress by remember { mutableStateOf(0.3f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        ProfessionalLinearProgressBar(
            progress = progress,
            height = 28.dp,
            cornerRadius = 14.dp,
            gradientColors = listOf(Color.Magenta, Color.Cyan),
            showPercentageInside = true,
            showPercentageOutside = false,
            textStyle = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        )

        ProfessionalLinearProgressBar(
            progress = progress,
            height = 12.dp,
            cornerRadius = 6.dp,
            gradientColors = listOf(Color.Green, Color.Yellow),
            showPercentageInside = false,
            showPercentageOutside = true,
            textStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.align(Alignment.End)
        ) {
            Button(onClick = { if (progress < 1f) progress += 0.1f }) {
                Text("افزایش")
            }
            Button(onClick = { if (progress > 0f) progress -= 0.1f }) {
                Text("کاهش")
            }
        }
    }
}