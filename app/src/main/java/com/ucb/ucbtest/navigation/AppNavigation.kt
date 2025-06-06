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
import com.ucb.ucbtest.search.SearchScreen
import com.ucb.ucbtest.settings.SettingsScreen
import com.ucb.ucbtest.cart.CartScreen

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
        // Pantalla principal (Home/Meal)
        composable(Screen.MealScreen.route) {
            MealUI(
                navController = navController,
                onDetailsClick = { meal ->
                    val mealJson = Uri.encode(Gson().toJson(meal))
                    navController.navigate(Screen.MealDetailScreen.createRoute(mealJson))
                }
            )
        }

        // Pantalla de configuraciones
        composable(Screen.SettingsScreen.route) {
            SettingsScreen(navController = navController)
        }

        // NUEVA: Pantalla de búsqueda
        composable("search") {
            SearchScreen(
                onMealClick = { meal ->
                    val mealJson = Uri.encode(Gson().toJson(meal))
                    navController.navigate(Screen.MealDetailScreen.createRoute(mealJson))
                }
            )
        }

        // Pantalla de detalles del plato
        composable(
            route = Screen.MealDetailScreen.route,
            arguments = listOf(navArgument("meal") { type = NavType.StringType })
        ) { backStackEntry ->
            val mealJson = backStackEntry.arguments?.getString("meal")
            val meal = Gson().fromJson(mealJson, Meal::class.java)
            MealDetailScreen(meal = meal, navController = navController) // AGREGAR navController
        }
        composable("cart") {
            CartScreen(navController = navController)
        }
    }
}