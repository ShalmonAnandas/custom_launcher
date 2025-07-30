package com.shalmon.myapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.shalmon.myapplication.data.LauncherSettings
import com.shalmon.myapplication.viewmodel.LauncherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: LauncherViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val launcherSettings by viewModel.launcherSettings
    
    Column(modifier = modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Launcher Settings") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        )
        
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                SettingsSection(title = "Home Screen") {
                    GridSizeSelector(
                        title = "Grid Columns",
                        value = launcherSettings.homeGridColumns,
                        range = 3..6,
                        onValueChange = { newColumns ->
                            viewModel.updateSettings(
                                launcherSettings.copy(homeGridColumns = newColumns)
                            )
                        }
                    )
                    
                    GridSizeSelector(
                        title = "Grid Rows",
                        value = launcherSettings.homeGridRows,
                        range = 4..8,
                        onValueChange = { newRows ->
                            viewModel.updateSettings(
                                launcherSettings.copy(homeGridRows = newRows)
                            )
                        }
                    )
                }
            }
            
            item {
                SettingsSection(title = "App Menu") {
                    GridSizeSelector(
                        title = "Grid Columns",
                        value = launcherSettings.menuGridColumns,
                        range = 3..6,
                        onValueChange = { newColumns ->
                            viewModel.updateSettings(
                                launcherSettings.copy(menuGridColumns = newColumns)
                            )
                        }
                    )
                    
                    GridSizeSelector(
                        title = "Grid Rows",
                        value = launcherSettings.menuGridRows,
                        range = 4..8,
                        onValueChange = { newRows ->
                            viewModel.updateSettings(
                                launcherSettings.copy(menuGridRows = newRows)
                            )
                        }
                    )
                }
            }
            
            item {
                SettingsSection(title = "Appearance") {
                    SwitchPreference(
                        title = "Show Page Indicator",
                        subtitle = "Display dots indicating current page",
                        checked = launcherSettings.showPageIndicator,
                        onCheckedChange = { newValue ->
                            viewModel.updateSettings(
                                launcherSettings.copy(showPageIndicator = newValue)
                            )
                        }
                    )
                    
                    SwitchPreference(
                        title = "Wallpaper Scrolling",
                        subtitle = "Enable wallpaper parallax scrolling",
                        checked = launcherSettings.enableWallpaperScrolling,
                        onCheckedChange = { newValue ->
                            viewModel.updateSettings(
                                launcherSettings.copy(enableWallpaperScrolling = newValue)
                            )
                        }
                    )
                    
                    SliderPreference(
                        title = "Icon Size",
                        subtitle = "Adjust the size of app icons",
                        value = launcherSettings.iconSize,
                        range = 0.5f..2.0f,
                        onValueChange = { newSize ->
                            viewModel.updateSettings(
                                launcherSettings.copy(iconSize = newSize)
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            content()
        }
    }
}

@Composable
private fun GridSizeSelector(
    title: String,
    value: Int,
    range: IntRange,
    onValueChange: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title)
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            range.forEach { size ->
                FilterChip(
                    onClick = { onValueChange(size) },
                    label = { Text(size.toString()) },
                    selected = value == size
                )
            }
        }
    }
}

@Composable
private fun SwitchPreference(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
private fun SliderPreference(
    title: String,
    subtitle: String,
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    onValueChange: (Float) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Text(
                text = "%.1f".format(value),
                style = MaterialTheme.typography.bodyMedium
            )
        }
        
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = range,
            modifier = Modifier.fillMaxWidth()
        )
    }
}