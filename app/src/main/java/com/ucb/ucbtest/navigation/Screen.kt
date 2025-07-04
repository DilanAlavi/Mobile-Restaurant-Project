package com.ucb.ucbtest.navigation

sealed class Screen(val route: String) {
    object SplashScreen : Screen("splash_screen")
    object LoginScreen : Screen("login_screen")
    object MealScreen : Screen("meal_screen")
    object SettingsScreen : Screen("settings_screen")
    object SearchScreen : Screen("search_screen")
    object CartScreen : Screen("cart")
    object CheckoutScreen : Screen("checkout") // AGREGAR
    object OrdersHistoryScreen : Screen("orders_history") // AGREGAR
    object ProfileScreen : Screen("profile")
    object CategoryProductsScreen : Screen("category_products/{categoryName}") {
        fun createRoute(categoryName: String) = "category_products/$categoryName"
    }
    object MealDetailScreen : Screen("meal_detail_screen/{meal}") {
        fun createRoute(mealJson: String) = "meal_detail_screen/$mealJson"
    }
}