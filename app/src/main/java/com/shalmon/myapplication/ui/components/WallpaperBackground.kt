package com.shalmon.myapplication.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import java.io.File

@Composable
fun WallpaperBackground(
    wallpaperPath: String?,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    
    Box(modifier = modifier) {
        if (wallpaperPath != null && File(wallpaperPath).exists()) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(wallpaperPath)
                    .crossfade(true)
                    .build(),
                contentDescription = "Wallpaper",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            // Default gradient background
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                                MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f),
                                MaterialTheme.colorScheme.tertiary.copy(alpha = 0.4f)
                            )
                        )
                    )
            )
        }
    }
}