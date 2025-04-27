
package com.ucb.framework.mappers

import com.ucb.domain.Meal
import com.ucb.framework.dto.MealDto

fun MealDto.toModel(): Meal {
    return Meal(
        idMeal = idMeal,
        strMeal = strMeal,
        strCategory = strCategory ?: "",
        strArea = strArea ?: "",
        strInstructions = strInstructions ?: "",
        strMealThumb = strMealThumb ?: "",
        price = 50.0 // Precio hardcodeado
    )
}