package com.shalmon.myapplication.utils

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.IOException

class WallpaperUtils(private val context: Context) {
    
    private val wallpaperManager = WallpaperManager.getInstance(context)
    
    fun getCurrentWallpaper(): ImageBitmap? {
        return try {
            val drawable = wallpaperManager.drawable
            if (drawable is BitmapDrawable) {
                drawable.bitmap.asImageBitmap()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
    
    fun setWallpaper(uri: Uri): Boolean {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            wallpaperManager.setStream(inputStream)
            true
        } catch (e: IOException) {
            false
        }
    }
    
    fun setWallpaper(bitmap: Bitmap): Boolean {
        return try {
            wallpaperManager.setBitmap(bitmap)
            true
        } catch (e: IOException) {
            false
        }
    }
    
    fun hasWallpaperPermission(): Boolean {
        return try {
            context.checkSelfPermission(android.Manifest.permission.SET_WALLPAPER) == 
                android.content.pm.PackageManager.PERMISSION_GRANTED
        } catch (e: Exception) {
            false
        }
    }
}