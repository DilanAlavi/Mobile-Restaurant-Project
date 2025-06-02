package com.ucb.ucbtest.auth

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun AuthNavigation(
    navController: NavHostController,
    onAuthSuccess: () -> Unit
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
                onLoginSuccess = onAuthSuccess
            )
        }
    }
}