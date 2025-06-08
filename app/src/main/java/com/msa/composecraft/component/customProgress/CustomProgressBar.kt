package com.msa.composecraft.component.customProgress

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.min

@Composable
fun CustomLinearProgressBar(
    progress: Float?,               // مقدار بین 0f تا 1f، یا null برای indeterminate
    modifier: Modifier = Modifier,
    height: Dp = 12.dp,
    backgroundColor: Color = Color.Gray.copy(alpha = 0.2f),
    gradientColors: List<Color> = listOf(Color.Red, Color.Yellow, Color.Green),
    animationDsl: AnimationSpec<Float> = tween(600, easing = LinearOutSlowInEasing),
    gradientSpeed: Int = 2000,
) {
    val infiniteTransition = rememberInfiniteTransition()
    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(gradientSpeed, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    val brush = Brush.linearGradient(
        colors = gradientColors,
        start = Offset(x = -offset * 200f, y = 0f),
        end = Offset(x = 200f - offset * 200f, y = 0f)
    )

    if (progress == null) {
        // حالت Indeterminate
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(height)
                .clip(RoundedCornerShape(height / 2))
                .background(backgroundColor)
        ) {
            val indeterminateWidth = 0.3f
            val infiniteAnim = rememberInfiniteTransition()
            val slideOffset by infiniteAnim.animateFloat(
                initialValue = -indeterminateWidth,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 1500, easing = LinearEasing)
                )
            )

            Box(
                modifier = Modifier
                    .width(0.dp)
                    .fillMaxHeight()
                    .offset(x = (slideOffset * 100).dp)
                    .background(brush)
            )
        }
    } else {
        // حالت Determinate
        val animatedProgress by animateFloatAsState(
            targetValue = progress.coerceIn(0f, 1f),
            animationSpec = animationDsl
        )

        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(height)
                .clip(RoundedCornerShape(height / 2))
                .background(backgroundColor)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedProgress)
                    .matchParentSize()
                    .background(brush)
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun LinearProgressDemo() {
    var progress by remember { mutableStateOf(0f) }
    var indeterminate by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    ) {
        CustomLinearProgressBar(
            progress = if (indeterminate) null else progress,
            gradientColors = listOf(Color.Magenta, Color.Cyan, Color.Blue),
            height = 16.dp
        )

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(onClick = {
                if (indeterminate) indeterminate = false
                else if (progress < 1f) progress += 0.1f
            }) {
                Text(if (indeterminate) "Switch to Determinate" else "Increase")
            }
            Button(onClick = {
                if (!indeterminate) indeterminate = true
                else { indeterminate = false; progress = 0f }
            }) {
                Text(if (indeterminate) "Stop Indeterminate" else "Start Indeterminate")
            }
        }
    }
}