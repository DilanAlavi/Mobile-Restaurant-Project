package com.ucb.ucbtest.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ucb.ucbtest.meal.MealUI
import com.ucb.ucbtest.settings.SettingsScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    innerPadding: PaddingValues // Agregado aquí
) {
    NavHost(
        navController = navController,
        startDestination = Screen.MealScreen.route,
        modifier = Modifier.padding(innerPadding), // Ahora usa innerPadding correctamente
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        composable(Screen.MealScreen.route) {
            MealUI(
                navController = navController,
                onDetailsClick = { /* Acción para ver detalles */ }
            )
        }
        composable(Screen.SettingsScreen.route) {
            SettingsScreen(
                navController = navController
            )
        }
    }
}
