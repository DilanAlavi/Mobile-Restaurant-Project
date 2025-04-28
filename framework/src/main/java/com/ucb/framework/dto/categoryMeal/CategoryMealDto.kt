package com.ucb.framework.dto.categoryMeal

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CategoryMealResponseDto(
    @Json(name = "categories")
    val categories: List<CategoryMealDto>?
)

@JsonClass(generateAdapter = true)
data class CategoryMealDto(
    @Json(name = "idCategory")
    val idCategory: String,
    @Json(name = "strCategory")
    val strCategory: String,
    @Json(name = "strCategoryThumb")
    val strCategoryThumb: String,
    @Json(name = "strCategoryDescription")
    val strCategoryDescription: String
)
