package com.ucb.framework.mappers.topPick

import com.ucb.domain.TopPick
import com.ucb.framework.dto.topPick.TopPickDto

fun TopPickDto.toModel(): TopPick {
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