package com.ucb.framework.mappers.categoryMeal

import com.ucb.domain.CategoryMeal
import com.ucb.framework.dto.categoryMeal.CategoryMealDto

fun CategoryMealDto.toModel(): CategoryMeal {
    return CategoryMeal(
        idCategory = idCategory,
        strCategory = strCategory,
        strCategoryThumb = strCategoryThumb,
        strCategoryDescription = strCategoryDescription
    )
}

fun List<CategoryMealDto>.toModelList(): List<CategoryMeal> {
    return this.map { it.toModel() }
}
