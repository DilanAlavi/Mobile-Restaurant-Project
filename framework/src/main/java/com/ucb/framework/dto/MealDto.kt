package com.ucb.framework.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MealResponseDto(
    @Json(name = "meals")
    val meals: List<MealDto>?
)

@JsonClass(generateAdapter = true)
data class MealDto(
    @Json(name = "idMeal")
    val idMeal: String,
    @Json(name = "strMeal")
    val strMeal: String,
    @Json(name = "strCategory")
    val strCategory: String?,
    @Json(name = "strArea")
    val strArea: String?,
    @Json(name = "strInstructions")
    val strInstructions: String?,
    @Json(name = "strMealThumb")
    val strMealThumb: String?
)