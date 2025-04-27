
package com.ucb.framework.meal

import com.ucb.data.NetworkResult
import com.ucb.data.meal.IMealRemoteDataSource
import com.ucb.domain.Meal
import com.ucb.framework.mappers.toModel
import com.ucb.framework.service.RetrofitBuilder

class MealRemoteDataSource(
    private val retrofitBuilder: RetrofitBuilder
) : IMealRemoteDataSource {

    override suspend fun getMealByName(name: String): NetworkResult<Meal> {
        val response = retrofitBuilder.mealApiService.searchMealByName(name)
        return if (response.isSuccessful) {
            val mealDto = response.body()?.meals?.firstOrNull()
            if (mealDto != null) {
                NetworkResult.Success(mealDto.toModel())
            } else {
                NetworkResult.Error("No se encontró el plato solicitado")
            }
        } else {
            NetworkResult.Error(response.message())
        }
    }
}