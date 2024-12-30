package com.msa.composecraft.component.media

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.msa.composecraft.ui.theme.ComposeCraftTheme
import kotlinx.coroutines.delay

@Composable
fun AudioPlayer(
    audioUri: String, // آدرس فایل صوتی (URL یا URI محلی)
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // ایجاد ExoPlayer و مدیریت چرخه حیات آن
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri(Uri.parse(audioUri))
            setMediaItem(mediaItem)
            prepare()
        }
    }

    // آزاد کردن منابع ExoPlayer هنگام از بین رفتن کامپوننت
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // کنترل‌های پخش
        PlayerControls(exoPlayer = exoPlayer)

        // اسلایدر پیشرفت پخش
        ProgressSlider(exoPlayer = exoPlayer)
    }
}

@Composable
fun PlayerControls(exoPlayer: ExoPlayer) {
    var isPlaying by remember { mutableStateOf(exoPlayer.isPlaying) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // دکمه پخش/توقف
        IconButton(onClick = {
            if (isPlaying) {
                exoPlayer.pause()
            } else {
                exoPlayer.play()
            }
            isPlaying = !isPlaying
        }) {
            Icon(
                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = if (isPlaying) "Pause" else "Play"
            )
        }

        // دکمه کنترل صدا
        IconButton(onClick = {
            exoPlayer.volume = if (exoPlayer.volume == 0f) 1f else 0f
        }) {
            Icon(
                imageVector = if (exoPlayer.volume == 0f) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                contentDescription = if (exoPlayer.volume == 0f) "Unmute" else "Mute"
            )
        }
    }
}

@Composable
fun ProgressSlider(exoPlayer: ExoPlayer) {
    var progress by remember { mutableStateOf(0f) }

    // به‌روزرسانی پیشرفت پخش
    LaunchedEffect(key1 = exoPlayer) {
        while (true) {
            delay(500) // به‌روزرسانی هر 500 میلی‌ثانیه
            progress = if (exoPlayer.duration > 0) {
                exoPlayer.currentPosition.toFloat() / exoPlayer.duration
            } else {
                0f
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Slider(
            value = progress,
            onValueChange = { newProgress ->
                exoPlayer.seekTo((newProgress * exoPlayer.duration).toLong())
            },
            modifier = Modifier.fillMaxWidth()
        )

        // نمایش زمان فعلی و کل فایل صوتی
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = formatTime(exoPlayer.currentPosition))
            Text(text = formatTime(exoPlayer.duration))
        }
    }
}

// تابع برای فرمت‌دهی زمان (میلی‌ثانیه به MM:SS)
private fun formatTime(millis: Long): String {
    val totalSeconds = millis / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}


@Composable
fun AudioPlayerScreen(modifier: Modifier = Modifier) {
    ComposeCraftTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            AudioPlayer(
                audioUri = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}


@Preview
@Composable
private fun AudioPlayerPreview() {
    AudioPlayerScreen()
}