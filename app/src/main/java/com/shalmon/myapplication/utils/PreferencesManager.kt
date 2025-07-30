package com.shalmon.myapplication.utils

import android.content.Context
import android.content.SharedPreferences
import com.shalmon.myapplication.data.LauncherSettings
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PreferencesManager(context: Context) {
    
    private val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()
    
    companion object {
        private const val PREFS_NAME = "launcher_prefs"
        private const val KEY_LAUNCHER_SETTINGS = "launcher_settings"
        private const val KEY_HOME_SCREEN_ITEMS = "home_screen_items"
        private const val KEY_CURRENT_PAGE = "current_page"
    }
    
    fun saveLauncherSettings(settings: LauncherSettings) {
        val json = gson.toJson(settings)
        sharedPreferences.edit()
            .putString(KEY_LAUNCHER_SETTINGS, json)
            .apply()
    }
    
    fun loadLauncherSettings(): LauncherSettings {
        val json = sharedPreferences.getString(KEY_LAUNCHER_SETTINGS, null)
        return if (json != null) {
            try {
                gson.fromJson(json, LauncherSettings::class.java)
            } catch (e: Exception) {
                LauncherSettings()
            }
        } else {
            LauncherSettings()
        }
    }
    
    fun saveCurrentPage(page: Int) {
        sharedPreferences.edit()
            .putInt(KEY_CURRENT_PAGE, page)
            .apply()
    }
    
    fun loadCurrentPage(): Int {
        return sharedPreferences.getInt(KEY_CURRENT_PAGE, 0)
    }
    
    fun saveHomeScreenItems(items: List<List<com.shalmon.myapplication.data.LauncherItem>>) {
        val json = gson.toJson(items)
        sharedPreferences.edit()
            .putString(KEY_HOME_SCREEN_ITEMS, json)
            .apply()
    }
    
    fun loadHomeScreenItems(): List<List<com.shalmon.myapplication.data.LauncherItem>> {
        val json = sharedPreferences.getString(KEY_HOME_SCREEN_ITEMS, null)
        return if (json != null) {
            try {
                val type = object : TypeToken<List<List<com.shalmon.myapplication.data.LauncherItem>>>() {}.type
                gson.fromJson(json, type)
            } catch (e: Exception) {
                // Return 3 empty pages as default
                listOf(emptyList(), emptyList(), emptyList())
            }
        } else {
            // Return 3 empty pages as default
            listOf(emptyList(), emptyList(), emptyList())
        }
    }
    
    fun clearAllData() {
        sharedPreferences.edit().clear().apply()
    }
}