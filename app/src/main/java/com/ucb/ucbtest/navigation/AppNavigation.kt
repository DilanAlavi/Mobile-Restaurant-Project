package com.ucb.ucbtest.navigation

import android.net.Uri
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.google.gson.Gson
import com.ucb.domain.Meal
import com.ucb.ucbtest.meal.MealDetailScreen
import com.ucb.ucbtest.meal.MealUI
import com.ucb.ucbtest.settings.SettingsScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    innerPadding: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = Screen.MealScreen.route,
        modifier = Modifier.padding(innerPadding),
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        composable(Screen.MealScreen.route) {
            MealUI(
                navController = navController,
                onDetailsClick = { meal ->
                    val mealJson = Uri.encode(Gson().toJson(meal))
                    navController.navigate(Screen.MealDetailScreen.createRoute(mealJson))
                }
            )
        }
        composable(Screen.SettingsScreen.route) {
            SettingsScreen(navController = navController)
        }
        composable(
            route = Screen.MealDetailScreen.route,
            arguments = listOf(navArgument("meal") { type = NavType.StringType })
        ) { backStackEntry ->
            val mealJson = backStackEntry.arguments?.getString("meal")
            val meal = Gson().fromJson(mealJson, Meal::class.java)
            MealDetailScreen(meal = meal)
        }
    }
}