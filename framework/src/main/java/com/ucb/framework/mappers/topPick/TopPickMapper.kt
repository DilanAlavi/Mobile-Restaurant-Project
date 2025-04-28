package com.ucb.framework.mappers.topPick

import com.ucb.domain.TopPick
import com.ucb.framework.dto.topPick.TopPickDto
import kotlin.math.roundToInt

fun TopPickDto.toModel(): TopPick {
    // Cada comida tendrá un precio original entre 800 y 1200 Bs.
    val originalPrice = when {
        idMeal.hashCode() % 5 == 0 -> 800.0
        idMeal.hashCode() % 5 == 1 -> 900.0
        idMeal.hashCode() % 5 == 2 -> 1000.0
        idMeal.hashCode() % 5 == 3 -> 1100.0
        else -> 1200.0
    }

    // El descuento será del 30%, 40% o 50%
    val discountPercentage = when {
        idMeal.hashCode() % 3 == 0 -> 30
        idMeal.hashCode() % 3 == 1 -> 40
        else -> 50
    }

    // Calcular el precio con descuento
    val discountPrice = (originalPrice * (100 - discountPercentage) / 100.0).roundToInt().toDouble()

    return TopPick(
        idMeal = idMeal,
        strMeal = strMeal,
        strMealThumb = strMealThumb,
        originalPrice = originalPrice,
        discountPrice = discountPrice,
        discountPercentage = discountPercentage
    )
}

fun List<TopPickDto>.toModelList(): List<TopPick> {
    return this.map { it.toModel() }
}