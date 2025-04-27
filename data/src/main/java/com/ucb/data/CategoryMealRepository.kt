package com.ucb.data

import com.ucb.data.meal.ICategoryMealRemoteDataSource
import com.ucb.domain.CategoryMeal

class CategoryMealRepository(private val remoteDataSource: ICategoryMealRemoteDataSource) {
    suspend fun getAllCategories(): NetworkResult<CategoryMeal> {
        return remoteDataSource.getAllCategories()
    }
}