package com.ucb.data.meal

import com.ucb.data.NetworkResult
import com.ucb.domain.CategoryMeal


interface ICategoryMealRemoteDataSource {
    suspend fun getAllCategories(): NetworkResult<CategoryMeal>
}