package com.shalmon.myapplication.widget

import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetHostView
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.viewinterop.AndroidView

class LauncherAppWidgetHost(context: Context, hostId: Int) : AppWidgetHost(context, hostId) {
    
    override fun onCreateView(context: Context, appWidgetId: Int, appWidget: AppWidgetProviderInfo?): AppWidgetHostView {
        return LauncherAppWidgetHostView(context)
    }
    
    override fun onProviderChanged(appWidgetId: Int, appWidget: AppWidgetProviderInfo?) {
        super.onProviderChanged(appWidgetId, appWidget)
    }
    
    override fun onProvidersChanged() {
        super.onProvidersChanged()
    }
}

class LauncherAppWidgetHostView(context: Context) : AppWidgetHostView(context) {
    
    override fun getAppWidgetInfo(): AppWidgetProviderInfo? {
        val info = super.getAppWidgetInfo()
        if (info != null) {
            return info
        }
        return null
    }
    
    override fun updateAppWidget(remoteViews: android.widget.RemoteViews?) {
        super.updateAppWidget(remoteViews)
    }
}

@Composable
fun WidgetHostView(
    context: Context,
    appWidgetHost: LauncherAppWidgetHost,
    appWidgetId: Int,
    modifier: androidx.compose.ui.Modifier = androidx.compose.ui.Modifier
) {
    val appWidgetManager = remember { AppWidgetManager.getInstance(context) }
    
    AndroidView(
        factory = { ctx ->
            val info = appWidgetManager.getAppWidgetInfo(appWidgetId)
            appWidgetHost.createView(ctx, appWidgetId, info) as LauncherAppWidgetHostView
        },
        modifier = modifier
    )
}