package com.ucb.ucbtest.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ucb.ucbtest.bottomNav.BottomNavItem
import com.ucb.ucbtest.meal.MealUI

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController, startDestination = BottomNavItem.HOME.route) {
        composable(BottomNavItem.HOME.route) {
            MealUI(
                navController = navController,
                onDetailsClick = { /* Acción al hacer clic en un elemento de comida */ }
            )
        }
        composable(BottomNavItem.SEARCH.route) {
            //SearchScreen()
        }
        composable(BottomNavItem.CART.route) {
            //CartScreen()
        }
        composable(BottomNavItem.PROFILE.route) {
            //ProfileScreen()
        }
    }
}
