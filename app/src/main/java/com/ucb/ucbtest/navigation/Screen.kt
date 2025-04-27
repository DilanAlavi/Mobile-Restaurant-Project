package com.ucb.ucbtest.navigation

sealed class Screen(val route: String) {
    object MealScreen : Screen("meal")
}