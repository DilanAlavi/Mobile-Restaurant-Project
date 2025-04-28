package com.ucb.domain
import java.io.Serializable

data class Meal(
    val idMeal: String,
    val strMeal: String,
    val strCategory: String,
    val strArea: String,
    val strInstructions: String,
    val strMealThumb: String,
    val price: Double = 50.0 //Esto esta hardcoeado
) : Serializable