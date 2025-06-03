package com.ucb.framework.service

import com.ucb.framework.dto.MealResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface IMealApiService {
    @GET("/api/json/v1/1/search.php")
    suspend fun searchMealByName(@Query("s") mealName: String): Response<MealResponseDto>
    @GET("/api/json/v1/1/search.php")
    suspend fun searchMealsByName(@Query("s") mealName: String): Response<MealResponseDto>
}