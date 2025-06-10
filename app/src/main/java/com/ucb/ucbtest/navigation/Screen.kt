package com.ucb.ucbtest.navigation

sealed class Screen(val route: String) {
    object MealScreen : Screen("meal_screen")
    object SettingsScreen : Screen("settings_screen")
    object SearchScreen : Screen("search_screen")
    object CartScreen : Screen("cart")
    object CheckoutScreen : Screen("checkout") // AGREGAR
    object OrdersHistoryScreen : Screen("orders_history") // AGREGAR
    object MealDetailScreen : Screen("meal_detail_screen/{meal}") {
        fun createRoute(mealJson: String) = "meal_detail_screen/$mealJson"
    }
}