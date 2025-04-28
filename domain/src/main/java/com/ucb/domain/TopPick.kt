package com.ucb.domain

data class TopPick(
    val idMeal: String,
    val strMeal: String,
    val strMealThumb: String,
    val originalPrice: Double,
    val discountPrice: Double,
    val discountPercentage: Int
)