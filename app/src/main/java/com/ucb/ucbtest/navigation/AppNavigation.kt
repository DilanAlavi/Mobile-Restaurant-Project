package com.ucb.ucbtest.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ucb.ucbtest.meal.MealUI
import com.ucb.ucbtest.settings.SettingsScreen
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URLDecoder
import java.net.URLEncoder

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.MealScreen.route,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        composable(Screen.MealScreen.route) {
            MealUI(
                navController = navController, // Pasamos el navController a MealUI
                onDetailsClick = { /* Acción para ver detalles */ }
            )
        }
        composable(Screen.SettingsScreen.route) {
            // Aquí agregas el SettingsScreen en la navegación
            SettingsScreen(
                navController = navController
            )
        }
    }
}