package com.shalmon.myapplication.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import com.shalmon.myapplication.data.AppInfo

class AppManager(private val context: Context) {
    
    fun getAllInstalledApps(): List<AppInfo> {
        val packageManager = context.packageManager
        val intent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        
        val apps = packageManager.queryIntentActivities(intent, 0)
        return apps.mapNotNull { resolveInfo ->
            try {
                val applicationInfo = packageManager.getApplicationInfo(
                    resolveInfo.activityInfo.packageName, 
                    0
                )
                
                AppInfo(
                    packageName = resolveInfo.activityInfo.packageName,
                    label = resolveInfo.loadLabel(packageManager).toString(),
                    icon = resolveInfo.loadIcon(packageManager),
                    isSystemApp = (applicationInfo.flags and android.content.pm.ApplicationInfo.FLAG_SYSTEM) != 0
                )
            } catch (e: Exception) {
                null
            }
        }.sortedBy { it.label.lowercase() }
    }
    
    fun searchApps(query: String): List<AppInfo> {
        if (query.isBlank()) return getAllInstalledApps()
        
        return getAllInstalledApps().filter { app ->
            app.label.contains(query, ignoreCase = true) ||
            app.packageName.contains(query, ignoreCase = true)
        }
    }
    
    fun launchApp(packageName: String) {
        try {
            val intent = context.packageManager.getLaunchIntentForPackage(packageName)
            intent?.let {
                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(it)
            }
        } catch (e: Exception) {
            // Handle launch error
        }
    }
}