package com.shalmon.myapplication.viewmodel

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shalmon.myapplication.data.AppInfo
import com.shalmon.myapplication.data.LauncherItem
import com.shalmon.myapplication.data.LauncherSettings
import com.shalmon.myapplication.utils.AppManager
import com.shalmon.myapplication.utils.PreferencesManager
import com.shalmon.myapplication.utils.PermissionHelper

class LauncherViewModel(private val context: Context) : ViewModel() {
    
    private val appManager = AppManager(context)
    private val preferencesManager = PreferencesManager(context)
    private val permissionHelper = PermissionHelper(context)
    
    private val _allApps = mutableStateOf<List<AppInfo>>(emptyList())
    val allApps: State<List<AppInfo>> = _allApps
    
    private val _homeScreenItems = mutableStateOf<List<List<LauncherItem>>>(emptyList())
    val homeScreenItems: State<List<List<LauncherItem>>> = _homeScreenItems
    
    private val _currentPage = mutableStateOf(0)
    val currentPage: State<Int> = _currentPage
    
    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery
    
    private val _filteredApps = mutableStateOf<List<AppInfo>>(emptyList())
    val filteredApps: State<List<AppInfo>> = _filteredApps
    
    private val _launcherSettings = mutableStateOf(LauncherSettings())
    val launcherSettings: State<LauncherSettings> = _launcherSettings
    
    private val _isMenuOpen = mutableStateOf(false)
    val isMenuOpen: State<Boolean> = _isMenuOpen
    
    private val _isDragMode = mutableStateOf(false)
    val isDragMode: State<Boolean> = _isDragMode
    
    init {
        loadApps()
        loadSettings()
        initializeHomeScreenPages()
    }
    
    private fun loadSettings() {
        _launcherSettings.value = preferencesManager.loadLauncherSettings()
        _currentPage.value = preferencesManager.loadCurrentPage()
    }
    
    private fun loadApps() {
        _allApps.value = appManager.getAllInstalledApps()
        _filteredApps.value = _allApps.value
    }
    
    private fun initializeHomeScreenPages() {
        val savedItems = preferencesManager.loadHomeScreenItems()
        _homeScreenItems.value = if (savedItems.isEmpty()) {
            // Initialize with 3 empty pages if no saved data
            listOf(emptyList(), emptyList(), emptyList())
        } else {
            savedItems
        }
    }
    
    fun searchApps(query: String) {
        _searchQuery.value = query
        _filteredApps.value = if (query.isBlank()) {
            _allApps.value
        } else {
            appManager.searchApps(query)
        }
    }
    
    fun setCurrentPage(page: Int) {
        _currentPage.value = page
        preferencesManager.saveCurrentPage(page)
    }
    
    fun toggleMenu() {
        _isMenuOpen.value = !_isMenuOpen.value
    }
    
    fun closeMenu() {
        _isMenuOpen.value = false
    }
    
    fun openMenu() {
        _isMenuOpen.value = true
    }
    
    fun launchApp(packageName: String) {
        appManager.launchApp(packageName)
        closeMenu()
    }
    
    fun addAppToHomeScreen(appInfo: AppInfo, page: Int, position: IntOffset) {
        val newItem = LauncherItem.AppShortcut(
            id = "${appInfo.packageName}_${System.currentTimeMillis()}",
            position = position,
            appInfo = appInfo
        )
        
        val currentPages = _homeScreenItems.value.toMutableList()
        if (page < currentPages.size) {
            val currentPageItems = currentPages[page].toMutableList()
            currentPageItems.add(newItem)
            currentPages[page] = currentPageItems
            _homeScreenItems.value = currentPages
            
            // Save to preferences
            preferencesManager.saveHomeScreenItems(currentPages)
        }
    }
    
    fun removeItemFromHomeScreen(itemId: String, page: Int) {
        val currentPages = _homeScreenItems.value.toMutableList()
        if (page < currentPages.size) {
            val currentPageItems = currentPages[page].toMutableList()
            currentPageItems.removeAll { it.id == itemId }
            currentPages[page] = currentPageItems
            _homeScreenItems.value = currentPages
            
            // Save to preferences
            preferencesManager.saveHomeScreenItems(currentPages)
        }
    }
    
    fun moveItem(itemId: String, fromPage: Int, toPage: Int, newPosition: IntOffset) {
        val currentPages = _homeScreenItems.value.toMutableList()
        
        // Find and remove item from source page
        if (fromPage < currentPages.size) {
            val fromPageItems = currentPages[fromPage].toMutableList()
            val item = fromPageItems.find { it.id == itemId }
            
            if (item != null) {
                fromPageItems.removeAll { it.id == itemId }
                currentPages[fromPage] = fromPageItems
                
                // Add item to target page with new position
                if (toPage < currentPages.size) {
                    val toPageItems = currentPages[toPage].toMutableList()
                    val updatedItem = when (item) {
                        is LauncherItem.AppShortcut -> item.copy(position = newPosition)
                        is LauncherItem.Widget -> item.copy(position = newPosition)
                        is LauncherItem.Folder -> item.copy(position = newPosition)
                    }
                    toPageItems.add(updatedItem)
                    currentPages[toPage] = toPageItems
                }
                
                _homeScreenItems.value = currentPages
                
                // Save to preferences
                preferencesManager.saveHomeScreenItems(currentPages)
            }
        }
    }
    
    fun updateSettings(newSettings: LauncherSettings) {
        _launcherSettings.value = newSettings
        preferencesManager.saveLauncherSettings(newSettings)
    }
    
    fun toggleDragMode() {
        _isDragMode.value = !_isDragMode.value
    }
    
    fun setDragMode(enabled: Boolean) {
        _isDragMode.value = enabled
    }
    
    fun addNewPage() {
        val currentPages = _homeScreenItems.value.toMutableList()
        currentPages.add(emptyList())
        _homeScreenItems.value = currentPages
        
        // Save to preferences
        preferencesManager.saveHomeScreenItems(currentPages)
    }
    
    fun hasPermissions(): Boolean {
        return permissionHelper.hasAllRequiredPermissions()
    }
    
    fun getMissingPermissions(): List<String> {
        return permissionHelper.getMissingPermissions()
    }
}

class LauncherViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LauncherViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LauncherViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}