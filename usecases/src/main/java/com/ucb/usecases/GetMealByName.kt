package com.ucb.usecases

import com.ucb.data.MealRepository
import com.ucb.data.NetworkResult
import com.ucb.domain.Meal
import javax.inject.Inject

class GetMealByName @Inject constructor(
    private val mealRepository: MealRepository
) {
    suspend fun invoke(name: String): NetworkResult<Meal> {
        return mealRepository.getMealByName(name)
    }
}