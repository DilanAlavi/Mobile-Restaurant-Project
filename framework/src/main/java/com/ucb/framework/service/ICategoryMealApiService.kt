package com.ucb.framework.service

import com.ucb.framework.dto.categoryMeal.CategoryMealResponseDto
import retrofit2.Response
import retrofit2.http.GET

interface ICategoryMealApiService {
    @GET("/api/json/v1/1/categories.php")
    suspend fun getAllCategories(): Response<CategoryMealResponseDto>
}