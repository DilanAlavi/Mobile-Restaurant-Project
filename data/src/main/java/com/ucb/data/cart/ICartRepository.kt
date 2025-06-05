package com.ucb.data.cart

import com.ucb.domain.CartItem
import com.ucb.domain.Meal
import kotlinx.coroutines.flow.Flow

interface ICartRepository {
    fun getCartItems(): Flow<List<CartItem>>
    suspend fun addToCart(meal: Meal)
    suspend fun removeFromCart(mealId: String)
    suspend fun updateQuantity(mealId: String, quantity: Int)
    suspend fun clearCart()
    fun getCartTotal(): Flow<Double>
    fun getCartItemCount(): Flow<Int>
}