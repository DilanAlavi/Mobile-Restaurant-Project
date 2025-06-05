package com.ucb.domain

data class CartItem(
    val meal: Meal,
    val quantity: Int = 1,
    val totalPrice: Double = meal.price * quantity
)