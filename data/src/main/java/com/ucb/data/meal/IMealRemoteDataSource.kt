
package com.ucb.data.meal

import com.ucb.data.NetworkResult
import com.ucb.domain.Meal

interface IMealRemoteDataSource {
    suspend fun getMealByName(name: String): NetworkResult<Meal>
}