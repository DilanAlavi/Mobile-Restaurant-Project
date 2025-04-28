package com.ucb.framework.dto.topPick

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TopPickResponseDto(
    @Json(name = "meals")
    val meals: List<TopPickDto>?
)

@JsonClass(generateAdapter = true)
data class TopPickDto(
    @Json(name = "idMeal")
    val idMeal: String,

    @Json(name = "strMeal")
    val strMeal: String,

    @Json(name = "strMealThumb")
    val strMealThumb: String,

    // Estos campos no vienen de la API, pero los necesitamos para nuestro modelo
    // Los calcularemos a partir de otros datos
    @Transient
    val originalPrice: Double = 1000.0, // Valor predeterminado

    @Transient
    val discountPrice: Double = 500.0, // Valor predeterminado

    @Transient
    val discountPercentage: Int = 50 // Valor predeterminado
)