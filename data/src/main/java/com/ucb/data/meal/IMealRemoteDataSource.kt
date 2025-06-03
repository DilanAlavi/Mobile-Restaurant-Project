
package com.ucb.data.meal

import com.ucb.data.NetworkResult
import com.ucb.domain.Meal

interface IMealRemoteDataSource {
    suspend fun getMealByName(name: String): NetworkResult<Meal>
    suspend fun searchMealsByName(name: String): NetworkResult<List<Meal>>
    suspend fun getMealById(id: String): NetworkResult<Meal>
}