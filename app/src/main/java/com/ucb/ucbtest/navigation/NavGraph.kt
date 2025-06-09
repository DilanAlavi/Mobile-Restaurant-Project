package com.ucb.ucbtest.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.google.gson.Gson
import com.ucb.domain.Meal
import com.ucb.ucbtest.bottomNav.BottomNavItem
import com.ucb.ucbtest.meal.MealDetailScreen
import com.ucb.ucbtest.meal.MealUI
import com.ucb.ucbtest.search.SearchScreen
import com.ucb.ucbtest.cart.CartScreen
import com.ucb.ucbtest.settings.SettingsScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController, startDestination = BottomNavItem.HOME.route) {
        // HOME - Pantalla principal
        composable(BottomNavItem.HOME.route) {
            MealUI(
                navController = navController,
                onDetailsClick = { meal ->
                    val mealJson = Uri.encode(Gson().toJson(meal))
                    navController.navigate(Screen.MealDetailScreen.createRoute(mealJson))
                }
            )
        }

        // SEARCH - Pantalla de búsqueda
        composable(BottomNavItem.SEARCH.route) {
            SearchScreen(
                onMealClick = { meal ->
                    val mealJson = Uri.encode(Gson().toJson(meal))
                    navController.navigate(Screen.MealDetailScreen.createRoute(mealJson))
                }
            )
        }

        // CART - Pantalla del carrito (placeholder)
        composable(BottomNavItem.CART.route) {
            CartScreen(navController = navController)
        }

        // PROFILE - Pantalla del perfil (placeholder)
        composable(BottomNavItem.PROFILE.route) {
            // TODO: Implementar ProfileScreen
            // ProfileScreen()
        }

        // Detalles del plato (desde cualquier pantalla)
        composable(
            route = Screen.MealDetailScreen.route,
            arguments = listOf(navArgument("meal") { type = NavType.StringType })
        ) { backStackEntry ->
            val mealJson = backStackEntry.arguments?.getString("meal")
            val meal = Gson().fromJson(mealJson, Meal::class.java)
            MealDetailScreen(meal = meal, navController = navController) // AGREGAR navController
        }
        composable("settings") {
            SettingsScreen(
                navController = navController,
                onItemSelected = { item ->
                    when (item) {
                        "Mis pedidos" -> navController.navigate("orders_history")
                        // otros casos si los tienes
                    }
                }
            )
        }
    }
}