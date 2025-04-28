package com.ucb.framework.service

import com.ucb.framework.dto.topPick.TopPickResponseDto
import retrofit2.Response
import retrofit2.http.GET

interface ITopPickApiService {
    // Podemos usar la API de comidas aleatorias para simular los "top picks"
    @GET("/api/json/v1/1/random.php")
    suspend fun getRandomMeal(): Response<TopPickResponseDto>

    // O podemos usar la función de búsqueda por primera letra para obtener varios platos
    @GET("/api/json/v1/1/search.php?f=c")
    suspend fun getTopPicksByLetter(): Response<TopPickResponseDto>

    // Para una versión más realista (pero que requiere upgrade para usar filtros)
    @GET("/api/json/v1/1/filter.php?c=Chicken")
    suspend fun getChickenMeals(): Response<TopPickResponseDto>
}