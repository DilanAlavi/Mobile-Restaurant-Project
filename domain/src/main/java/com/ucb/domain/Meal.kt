package com.ucb.domain

data class Meal(
    val idMeal: String,
    val strMeal: String,
    val strCategory: String,
    val strArea: String,
    val strInstructions: String,
    val strMealThumb: String,
    val price: Double = 50.0 //Esto esta hardcoeado
)