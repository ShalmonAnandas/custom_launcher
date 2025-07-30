package com.shalmon.myapplication.data

data class LauncherSettings(
    val homeGridColumns: Int = 4,
    val homeGridRows: Int = 6,
    val menuGridColumns: Int = 4,
    val menuGridRows: Int = 6,
    val wallpaperPath: String? = null,
    val enableWallpaperScrolling: Boolean = true,
    val showPageIndicator: Boolean = true,
    val iconSize: Float = 1.0f
)