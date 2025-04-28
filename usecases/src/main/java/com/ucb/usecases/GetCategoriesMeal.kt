package com.ucb.usecases

import com.ucb.data.CategoryMealRepository
import com.ucb.data.NetworkResult
import com.ucb.domain.CategoryMeal
import javax.inject.Inject

class GetCategoriesMeal @Inject constructor(
    private val categoryMealRepository: CategoryMealRepository)
{
    suspend fun invoke(): NetworkResult<List<CategoryMeal>> {
        return categoryMealRepository.getAllCategories()
    }

}