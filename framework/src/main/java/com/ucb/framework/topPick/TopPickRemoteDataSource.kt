package com.ucb.framework.topPick

import com.ucb.data.NetworkResult
import com.ucb.data.meal.ITopPickRemoteDataSource
import com.ucb.domain.TopPick
import com.ucb.framework.mappers.topPick.toModelList
import com.ucb.framework.service.RetrofitBuilder

class TopPickRemoteDataSource(
    private val retrofitBuilder: RetrofitBuilder
) : ITopPickRemoteDataSource {

    override suspend fun getTopPicks(): NetworkResult<List<TopPick>> {
        val response = retrofitBuilder.topPickApiService.getTopPicks()
        return if (response.isSuccessful) {
            val topPicksDto = response.body()?.topPicks
            if (!topPicksDto.isNullOrEmpty()) {
                NetworkResult.Success(topPicksDto.toModelList())
            } else {
                NetworkResult.Error("No se encontraron platos destacados")
            }
        } else {
            NetworkResult.Error(response.message())
        }
    }
}