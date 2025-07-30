package com.shalmon.myapplication.data

import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.IntOffset

@Stable
sealed class LauncherItem {
    abstract val id: String
    abstract val position: IntOffset
    
    data class AppShortcut(
        override val id: String,
        override val position: IntOffset,
        val appInfo: AppInfo
    ) : LauncherItem()
    
    data class Widget(
        override val id: String,
        override val position: IntOffset,
        val widgetId: Int,
        val width: Int,
        val height: Int,
        val packageName: String,
        val className: String
    ) : LauncherItem()
    
    data class Folder(
        override val id: String,
        override val position: IntOffset,
        val name: String,
        val items: List<AppShortcut>
    ) : LauncherItem()
}