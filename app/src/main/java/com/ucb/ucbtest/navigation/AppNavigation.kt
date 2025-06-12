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
import com.ucb.ucbtest.checkout.CheckoutScreen
import com.ucb.ucbtest.orders.OrdersHistoryScreen
import com.ucb.ucbtest.categoryproducts.CategoryProductsScreen
import com.ucb.ucbtest.profile.ProfileScreen

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

        composable(
            route = "category_products/{categoryName}",
            arguments = listOf(navArgument("categoryName") { type = NavType.StringType })
        ) { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("categoryName") ?: ""
            CategoryProductsScreen(
                categoryName = categoryName,
                navController = navController,
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
        composable("profile") {
            ProfileScreen(navController = navController)
        }
        composable("cart") {
            CartScreen(navController = navController)
        }
        // Pantalla de checkout
        composable("checkout") {
            CheckoutScreen(navController = navController)
        }

        // Pantalla de historial de órdenes
        composable("orders_history") {
            OrdersHistoryScreen(navController = navController)
        }
    }
}