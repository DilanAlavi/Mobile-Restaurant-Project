package com.ucb.framework.dto.topPick

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TopPickResponseDto(
    @Json(name = "topPicks")
    val topPicks: List<TopPickDto>?
)

@JsonClass(generateAdapter = true)
data class TopPickDto(
    @Json(name = "idMeal")
    val idMeal: String,
    @Json(name = "strMeal")
    val strMeal: String,
    @Json(name = "strMealThumb")
    val strMealThumb: String,
    @Json(name = "originalPrice")
    val originalPrice: Double,
    @Json(name = "discountPrice")
    val discountPrice: Double,
    @Json(name = "discountPercentage")
    val discountPercentage: Int
)