package com.ucb.data.cart

import com.ucb.domain.CartItem
import com.ucb.domain.Meal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepository @Inject constructor() : ICartRepository {

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())

    override fun getCartItems(): Flow<List<CartItem>> = _cartItems

    override suspend fun addToCart(meal: Meal) {
        val currentItems = _cartItems.value.toMutableList()
        val existingItemIndex = currentItems.indexOfFirst { it.meal.idMeal == meal.idMeal }

        if (existingItemIndex >= 0) {
            // Si ya existe, aumentar cantidad
            val existingItem = currentItems[existingItemIndex]
            val updatedItem = existingItem.copy(
                quantity = existingItem.quantity + 1,
                totalPrice = meal.price * (existingItem.quantity + 1)
            )
            currentItems[existingItemIndex] = updatedItem
        } else {
            // Si no existe, agregar nuevo
            currentItems.add(CartItem(meal = meal, quantity = 1, totalPrice = meal.price))
        }

        _cartItems.value = currentItems
    }

    override suspend fun removeFromCart(mealId: String) {
        val currentItems = _cartItems.value.toMutableList()
        currentItems.removeAll { it.meal.idMeal == mealId }
        _cartItems.value = currentItems
    }

    override suspend fun updateQuantity(mealId: String, quantity: Int) {
        if (quantity <= 0) {
            removeFromCart(mealId)
            return
        }

        val currentItems = _cartItems.value.toMutableList()
        val itemIndex = currentItems.indexOfFirst { it.meal.idMeal == mealId }

        if (itemIndex >= 0) {
            val item = currentItems[itemIndex]
            val updatedItem = item.copy(
                quantity = quantity,
                totalPrice = item.meal.price * quantity
            )
            currentItems[itemIndex] = updatedItem
            _cartItems.value = currentItems
        }
    }

    override suspend fun clearCart() {
        _cartItems.value = emptyList()
    }

    override fun getCartTotal(): Flow<Double> {
        return _cartItems.map { items ->
            items.sumOf { it.totalPrice }
        }
    }

    override fun getCartItemCount(): Flow<Int> {
        return _cartItems.map { items ->
            items.sumOf { it.quantity }
        }
    }
}