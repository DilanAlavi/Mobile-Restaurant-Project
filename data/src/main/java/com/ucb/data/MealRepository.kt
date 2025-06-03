package com.ucb.data

import com.ucb.data.meal.IMealRemoteDataSource
import com.ucb.domain.Meal

class MealRepository(
    private val remoteDataSource: IMealRemoteDataSource
) {
    suspend fun getMealByName(name: String): NetworkResult<Meal> {
        return remoteDataSource.getMealByName(name)
    }
    suspend fun searchMealsByName(name: String): NetworkResult<List<Meal>> {
        return remoteDataSource.searchMealsByName(name)
    }
}