package com.ucb.ucbtest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import com.ucb.ucbtest.auth.AuthNavigation
import com.ucb.ucbtest.bottomNav.BottomBar
import com.ucb.ucbtest.navigation.AppNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Ucbtest)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainApp()
        }
    }
}

@Composable
fun MainApp() {
    var isAuthenticated by remember { mutableStateOf(false) }
    val authNavController = rememberNavController()
    val mainNavController = rememberNavController()

    if (!isAuthenticated) {
        // Mostrar solo la navegación de autenticación (pantalla completa)
        AuthNavigation(
            navController = authNavController,
            onAuthSuccess = {
                isAuthenticated = true
            }
        )
    } else {
        // Mostrar la app principal con bottom navigation
        Scaffold(
            bottomBar = { BottomBar(mainNavController) }
        ) { innerPadding ->
            AppNavigation(mainNavController, innerPadding)
        }
    }
}