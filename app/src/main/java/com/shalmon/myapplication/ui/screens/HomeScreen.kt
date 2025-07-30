package com.shalmon.myapplication.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Settings
import com.shalmon.myapplication.ui.components.LauncherGrid
import com.shalmon.myapplication.ui.components.WallpaperBackground
import com.shalmon.myapplication.viewmodel.LauncherViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    viewModel: LauncherViewModel,
    onNavigateToSettings: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val scope = rememberCoroutineScope()
    
    val homeScreenItems by viewModel.homeScreenItems
    val currentPage by viewModel.currentPage
    val launcherSettings by viewModel.launcherSettings
    val isDragMode by viewModel.isDragMode
    
    val pagerState = rememberPagerState(
        initialPage = currentPage,
        pageCount = { maxOf(homeScreenItems.size, 1) }
    )
    
    // Sync pager state with view model
    LaunchedEffect(pagerState.currentPage) {
        viewModel.setCurrentPage(pagerState.currentPage)
    }
    
    // Sync view model state with pager
    LaunchedEffect(currentPage) {
        if (currentPage != pagerState.currentPage) {
            pagerState.animateScrollToPage(currentPage)
        }
    }
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { viewModel.setDragMode(true) },
                    onDragEnd = { viewModel.setDragMode(false) }
                ) { _, _ -> }
            }
    ) {
        // Wallpaper background
        WallpaperBackground(
            wallpaperPath = launcherSettings.wallpaperPath,
            modifier = Modifier.fillMaxSize()
        )
        
        // Main content
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Home screen pages with pager
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                beyondBoundsPageCount = 1
            ) { page ->
                val pageItems = if (page < homeScreenItems.size) {
                    homeScreenItems[page]
                } else {
                    emptyList()
                }
                
                LauncherGrid(
                    items = pageItems,
                    columns = launcherSettings.homeGridColumns,
                    rows = launcherSettings.homeGridRows,
                    isDragMode = isDragMode,
                    onItemClick = { item ->
                        when (item) {
                            is com.shalmon.myapplication.data.LauncherItem.AppShortcut -> {
                                viewModel.launchApp(item.appInfo.packageName)
                            }
                            else -> {
                                // Handle other item types
                            }
                        }
                    },
                    onItemMove = { itemId, newPosition ->
                        // Handle item movement within the same page
                        viewModel.moveItem(itemId, page, page, newPosition)
                    },
                    onItemRemove = { itemId ->
                        viewModel.removeItemFromHomeScreen(itemId, page)
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
            
            // Page indicators
            if (launcherSettings.showPageIndicator && homeScreenItems.size > 1) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(homeScreenItems.size) { page ->
                        val isCurrentPage = page == pagerState.currentPage
                        Box(
                            modifier = Modifier
                                .size(if (isCurrentPage) 12.dp else 8.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isCurrentPage) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                                    }
                                )
                        )
                        
                        if (page < homeScreenItems.size - 1) {
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                    }
                }
            }
        }
        
        // Quick actions (bottom dock area)
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(80.dp)
                .background(
                    MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                ),
            contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Settings button
                FloatingActionButton(
                    onClick = onNavigateToSettings,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings"
                    )
                }
                
                // App drawer button
                FloatingActionButton(
                    onClick = { viewModel.openMenu() },
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Apps,
                        contentDescription = "App Drawer"
                    )
                }
                
                // Spacer for symmetry
                Spacer(modifier = Modifier.size(48.dp))
            }
        }
    }
}