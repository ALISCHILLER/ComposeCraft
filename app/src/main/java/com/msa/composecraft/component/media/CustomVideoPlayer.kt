package com.msa.composecraft.component.media

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@Composable
fun CustomVideoPlayer(
    videoUri: String, // آدرس ویدیو (URL یا URI محلی)
    modifier: Modifier = Modifier,
    showControls: Boolean = true, // نمایش یا عدم نمایش کنترل‌ها
    autoPlay: Boolean = true // شروع خودکار پخش
) {
    val context = LocalContext.current

    // ایجاد ExoPlayer و مدیریت چرخه حیات آن
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri(Uri.parse(videoUri))
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = autoPlay
        }
    }

    // آزاد کردن منابع ExoPlayer هنگام از بین رفتن کامپوننت
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        // نمایش ویدیو
        AndroidView(
            factory = { context ->
                PlayerView(context).apply {
                    player = exoPlayer
                    useController = false // غیرفعال کردن کنترل‌های پیش‌فرض
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f) // نسبت تصویر 16:9
        )

        // نمایش کنترل‌ها (اگر showControls true باشد)
        if (showControls) {
            VideoControls(exoPlayer = exoPlayer)
        }
    }
}

@Composable
fun VideoControls(exoPlayer: ExoPlayer) {
    var isPlaying by remember { mutableStateOf(exoPlayer.isPlaying) }
    var volume by remember { mutableStateOf(exoPlayer.volume) }
    var progress by remember { mutableStateOf(0f) }

    // به‌روزرسانی پیشرفت ویدیو
    LaunchedEffect(key1 = exoPlayer) {
        while (true) {
            withFrameNanos {
                progress = if (exoPlayer.duration > 0) {
                    exoPlayer.currentPosition.toFloat() / exoPlayer.duration
                } else {
                    0f
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // کنترل‌های پخش/توقف، صدای ویدیو، و حالت تمام‌صفحه
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
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
                volume = if (volume == 0f) 1f else 0f
                exoPlayer.volume = volume
            }) {
                Icon(
                    imageVector = if (volume == 0f) Icons.AutoMirrored.Filled.VolumeOff else Icons.AutoMirrored.Filled.VolumeUp,
                    contentDescription = if (volume == 0f) "Unmute" else "Mute"
                )
            }

            // دکمه تمام‌صفحه (پیشنهاد برای توسعه بیشتر)
            IconButton(onClick = {
                // اینجا می‌توانید منطق حالت تمام‌صفحه را اضافه کنید
            }) {
                Icon(
                    imageVector = Icons.Default.Fullscreen,
                    contentDescription = "Fullscreen"
                )
            }
        }

        // اسلایدر پیشرفت ویدیو
        Slider(
            value = progress,
            onValueChange = { newProgress ->
                exoPlayer.seekTo((newProgress * exoPlayer.duration).toLong())
            },
            modifier = Modifier.fillMaxWidth()
        )

        // نمایش زمان فعلی و کل ویدیو
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

@Preview
@Composable
private fun CustomVideoPlayerPreview() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        CustomVideoPlayer(
            videoUri = "https://www.sample-videos.com/video123/mp4/720/big_buck_bunny_720p_1mb.mp4",
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            showControls = true
        )
    }

}
