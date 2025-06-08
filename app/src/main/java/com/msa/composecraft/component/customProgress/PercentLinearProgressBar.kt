package com.msa.composecraft.component.customProgress



import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*

@Composable
fun PercentLinearProgressBar(
    progress: Float, // مقدار بین 0.0 تا 1.0
    modifier: Modifier = Modifier,
    height: Dp = 22.dp,
    backgroundColor: Color = Color.LightGray.copy(alpha = 0.3f),
    progressBrush: Brush = Brush.horizontalGradient(
        colors = listOf(Color(0xFF00BCD4), Color(0xFF4CAF50))
    ),
    showPercentText: Boolean = true,
    textColor: Color = Color.White,
    textStyle: TextStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold),
    cornerRadius: Dp = 100.dp,
    animationDuration: Int = 600
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = animationDuration),
        label = "animatedProgress"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(cornerRadius))
            .background(backgroundColor),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(animatedProgress)
                .clip(RoundedCornerShape(cornerRadius))
                .background(progressBrush),
            contentAlignment = Alignment.Center
        ) {
            if (showPercentText && animatedProgress > 0.05f) {
                Text(
                    text = "${(animatedProgress * 100).toInt()}%",
                    color = textColor,
                    style = textStyle,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    }
}

@Composable
fun ProgressBarDemo() {
    var progress by remember { mutableStateOf(0.0f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("پیشرفت فایل", fontSize = 18.sp, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(12.dp))

        PercentLinearProgressBar(
            progress = progress,
            height = 24.dp,
            cornerRadius = 50.dp,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button (onClick = {
                if (progress < 1f) progress += 0.1f
            }) {
                Text("افزایش")
            }

            Button(onClick = {
                if (progress > 0f) progress -= 0.1f
            }) {
                Text("کاهش")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewProgressBar() {
    ProgressBarDemo()
}

