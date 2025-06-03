package com.ucb.usecases

import com.ucb.data.MealRepository
import com.ucb.data.NetworkResult
import com.ucb.domain.Meal
import javax.inject.Inject

class GetMealById @Inject constructor(
    private val mealRepository: MealRepository
) {
    suspend fun invoke(id: String): NetworkResult<Meal> {
        return mealRepository.getMealById(id)
    }
}