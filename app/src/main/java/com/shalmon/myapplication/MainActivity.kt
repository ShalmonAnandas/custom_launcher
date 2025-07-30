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
import com.shalmon.myapplication.ui.screens.AppMenuScreen
import com.shalmon.myapplication.ui.screens.HomeScreen
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
    val viewModel: LauncherViewModel = viewModel(
        factory = LauncherViewModelFactory(context)
    )
    
    val isMenuOpen by viewModel.isMenuOpen
    
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        // Home Screen
        HomeScreen(
            viewModel = viewModel,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
        
        // App Menu Overlay
        AppMenuScreen(
            viewModel = viewModel,
            isVisible = isMenuOpen,
            onDismiss = { viewModel.closeMenu() },
            modifier = Modifier.fillMaxSize()
        )
    }
}