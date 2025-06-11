package com.ucb.ucbtest.auth

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

// auth/AuthNavigation.kt
@Composable
fun AuthNavigation(
    navController: NavHostController,
    onAuthSuccess: () -> Unit,
    authViewModel: AuthViewModel // ✅ Recibir ViewModel desde MainActivity
) {
    NavHost(
        navController = navController,
        startDestination = "splash_screen"
    ) {
        composable("splash_screen") {
            SplashScreen(navController = navController)
        }

        composable("login_screen") {
            LoginScreen(
                navController = navController,
                onLoginSuccess = onAuthSuccess,
                viewModel = authViewModel // ✅ Pasar el mismo ViewModel
            )
        }

        composable("register_screen") {
            RegisterScreen(
                navController = navController,
                onRegisterSuccess = onAuthSuccess,
                viewModel = authViewModel // ✅ Pasar el mismo ViewModel
            )
        }
    }
}