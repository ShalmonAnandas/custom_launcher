package com.shalmon.myapplication.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.shalmon.myapplication.data.LauncherItem
import com.shalmon.myapplication.widget.WidgetHostView
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LauncherGrid(
    items: List<LauncherItem>,
    columns: Int,
    rows: Int,
    isDragMode: Boolean,
    onItemClick: (LauncherItem) -> Unit,
    onItemMove: (String, IntOffset) -> Unit,
    onItemRemove: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    
    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        modifier = modifier.padding(16.dp),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items, key = { it.id }) { item ->
            LauncherGridItem(
                item = item,
                isDragMode = isDragMode,
                onClick = { onItemClick(item) },
                onMove = { offset -> onItemMove(item.id, offset) },
                onRemove = { onItemRemove(item.id) },
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun LauncherGridItem(
    item: LauncherItem,
    isDragMode: Boolean,
    onClick: () -> Unit,
    onMove: (IntOffset) -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isDragging by remember { mutableStateOf(false) }
    var dragOffset by remember { mutableStateOf(IntOffset.Zero) }
    
    Box(
        modifier = modifier
            .graphicsLayer {
                translationX = dragOffset.x.toFloat()
                translationY = dragOffset.y.toFloat()
                scaleX = if (isDragging) 1.1f else 1.0f
                scaleY = if (isDragging) 1.1f else 1.0f
            }
            .zIndex(if (isDragging) 1f else 0f)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { 
                        isDragging = true 
                    },
                    onDragEnd = { 
                        isDragging = false
                        if (dragOffset != IntOffset.Zero) {
                            onMove(dragOffset)
                            dragOffset = IntOffset.Zero
                        }
                    }
                ) { _, dragAmount ->
                    dragOffset += IntOffset(
                        dragAmount.x.roundToInt(),
                        dragAmount.y.roundToInt()
                    )
                }
            }
            .combinedClickable(
                onClick = onClick,
                onLongClick = onRemove
            )
    ) {
        when (item) {
            is LauncherItem.AppShortcut -> {
                AppShortcutItem(
                    item = item,
                    isDragMode = isDragMode,
                    modifier = Modifier.fillMaxSize()
                )
            }
            is LauncherItem.Widget -> {
                WidgetItem(
                    item = item,
                    isDragMode = isDragMode,
                    modifier = Modifier.fillMaxSize()
                )
            }
            is LauncherItem.Folder -> {
                FolderItem(
                    item = item,
                    isDragMode = isDragMode,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun AppShortcutItem(
    item: LauncherItem.AppShortcut,
    isDragMode: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isDragMode) 8.dp else 2.dp
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AppIcon(
                app = item.appInfo,
                size = 48.dp,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            
            Text(
                text = item.appInfo.label,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun WidgetItem(
    item: LauncherItem.Widget,
    isDragMode: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isDragMode) 8.dp else 2.dp
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // Placeholder for widget content
            Text(
                text = "Widget\n${item.packageName}",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun FolderItem(
    item: LauncherItem.Folder,
    isDragMode: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isDragMode) 8.dp else 2.dp
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Show preview of folder apps
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.size(32.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                items(item.items.take(4)) { app ->
                    AppIcon(
                        app = app.appInfo,
                        size = 14.dp
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = item.name,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}