package com.shalmon.myapplication.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import com.shalmon.myapplication.data.AppInfo

@Composable
fun AppIcon(
    app: AppInfo,
    size: Dp = 56.dp,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    
    try {
        val bitmap = app.icon.toBitmap(
            width = with(context) { size.toPx().toInt() },
            height = with(context) { size.toPx().toInt() }
        )
        
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = app.label,
            modifier = modifier
                .size(size)
                .clip(RoundedCornerShape(12.dp))
        )
    } catch (e: Exception) {
        // Fallback for icon loading errors
        androidx.compose.material3.Surface(
            modifier = modifier
                .size(size)
                .clip(RoundedCornerShape(12.dp)),
            color = androidx.compose.material3.MaterialTheme.colorScheme.primary
        ) {
            androidx.compose.foundation.layout.Box(
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                androidx.compose.material3.Text(
                    text = app.label.take(1).uppercase(),
                    color = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary,
                    style = androidx.compose.material3.MaterialTheme.typography.headlineSmall
                )
            }
        }
    }
}