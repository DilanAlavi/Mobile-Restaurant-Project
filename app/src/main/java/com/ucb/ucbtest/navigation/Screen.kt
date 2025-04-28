package com.ucb.ucbtest.navigation

sealed class Screen(val route: String) {
    object MealScreen : Screen("meal_screen")
    object SettingsScreen : Screen("settings_screen")
    object MealDetailScreen : Screen("meal_detail_screen/{meal}") {
        fun createRoute(mealJson: String) = "meal_detail_screen/$mealJson"
    }
}
