package com.shalmon.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.shalmon.myapplication.ui.screens.AppMenuScreen
import com.shalmon.myapplication.ui.screens.HomeScreen
import com.shalmon.myapplication.ui.screens.SettingsScreen
import com.shalmon.myapplication.ui.theme.MyApplicationTheme
import com.shalmon.myapplication.viewmodel.LauncherViewModel
import com.shalmon.myapplication.viewmodel.LauncherViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                LauncherApp()
            }
        }
    }
}

@Composable
fun LauncherApp() {
    val context = LocalContext.current
    val navController = rememberNavController()
    val viewModel: LauncherViewModel = viewModel(
        factory = LauncherViewModelFactory(context)
    )
    
    val isMenuOpen by viewModel.isMenuOpen
    
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable("home") {
                // Home Screen
                HomeScreen(
                    viewModel = viewModel,
                    onNavigateToSettings = { navController.navigate("settings") },
                    modifier = Modifier.fillMaxSize()
                )
                
                // App Menu Overlay
                AppMenuScreen(
                    viewModel = viewModel,
                    isVisible = isMenuOpen,
                    onDismiss = { viewModel.closeMenu() },
                    modifier = Modifier.fillMaxSize()
                )
            }
            
            composable("settings") {
                SettingsScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}