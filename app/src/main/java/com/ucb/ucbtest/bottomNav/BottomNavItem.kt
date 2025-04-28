package com.ucb.ucbtest.bottomNav

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Person
import com.ucb.ucbtest.navigation.Screen

enum class BottomNavItem(val label: String, val icon: ImageVector, val route: String) {
    HOME("Inicio", Icons.Filled.Home, Screen.MealScreen.route),
    SEARCH("Buscar", Icons.Filled.Search, "search"),
    CART("Carrito", Icons.Filled.ShoppingCart, "cart"),
    PROFILE("Perfil", Icons.Filled.Person, "profile")
}
