package com.shalmon.myapplication.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

class PermissionHelper(private val context: Context) {
    
    fun hasWallpaperPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context, 
            Manifest.permission.SET_WALLPAPER
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    fun hasWallpaperHintsPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context, 
            Manifest.permission.SET_WALLPAPER_HINTS
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    fun hasQueryAllPackagesPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context, 
            Manifest.permission.QUERY_ALL_PACKAGES
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    fun hasBindAppWidgetPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context, 
            Manifest.permission.BIND_APPWIDGET
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    fun hasAllRequiredPermissions(): Boolean {
        return hasWallpaperPermission() && 
               hasWallpaperHintsPermission() && 
               hasQueryAllPackagesPermission() && 
               hasBindAppWidgetPermission()
    }
    
    fun getRequiredPermissions(): Array<String> {
        return arrayOf(
            Manifest.permission.SET_WALLPAPER,
            Manifest.permission.SET_WALLPAPER_HINTS,
            Manifest.permission.QUERY_ALL_PACKAGES,
            Manifest.permission.BIND_APPWIDGET
        )
    }
    
    fun getMissingPermissions(): List<String> {
        val missingPermissions = mutableListOf<String>()
        
        if (!hasWallpaperPermission()) {
            missingPermissions.add(Manifest.permission.SET_WALLPAPER)
        }
        if (!hasWallpaperHintsPermission()) {
            missingPermissions.add(Manifest.permission.SET_WALLPAPER_HINTS)
        }
        if (!hasQueryAllPackagesPermission()) {
            missingPermissions.add(Manifest.permission.QUERY_ALL_PACKAGES)
        }
        if (!hasBindAppWidgetPermission()) {
            missingPermissions.add(Manifest.permission.BIND_APPWIDGET)
        }
        
        return missingPermissions
    }
}