package com.ucb.framework.categoryMeal

import com.ucb.data.NetworkResult
import com.ucb.data.meal.ICategoryMealRemoteDataSource
import com.ucb.domain.CategoryMeal
import com.ucb.framework.mappers.categoryMeal.toModelList
import com.ucb.framework.service.RetrofitBuilder

class CategoryMealRemoteDataSource(
    private val retrofitBuilder: RetrofitBuilder
) : ICategoryMealRemoteDataSource {

    override suspend fun getAllCategories(): NetworkResult<List<CategoryMeal>> {
        val response = retrofitBuilder.categoryMealApiService.getAllCategories()
        return if (response.isSuccessful) {
            val categoriesDto = response.body()?.categories
            if (!categoriesDto.isNullOrEmpty()) {
                NetworkResult.Success(categoriesDto.toModelList())
            } else {
                NetworkResult.Error("No se encontraron categorías")
            }
        } else {
            NetworkResult.Error(response.message())
        }
    }
}
