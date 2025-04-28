package com.ucb.framework.topPick

import com.ucb.data.NetworkResult
import com.ucb.data.meal.ITopPickRemoteDataSource
import com.ucb.domain.TopPick
import com.ucb.framework.mappers.topPick.toModelList
import com.ucb.framework.service.RetrofitBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

class TopPickRemoteDataSource(
    private val retrofitBuilder: RetrofitBuilder
) : ITopPickRemoteDataSource {

    override suspend fun getTopPicks(): NetworkResult<List<TopPick>> = withContext(Dispatchers.IO) {
        try {
            // Obtenemos platos de la API
            val topPicksByLetterResponse = retrofitBuilder.topPickApiService.getTopPicksByLetter()

            if (topPicksByLetterResponse.isSuccessful) {
                val topPicksDto = topPicksByLetterResponse.body()?.meals
                if (!topPicksDto.isNullOrEmpty()) {
                    // Tomamos solo los primeros 4 platos para la vista de TopPicks
                    val modelList = topPicksDto.take(4).toModelList()
                    return@withContext NetworkResult.Success(modelList)
                }
            }

            return@withContext NetworkResult.Error("No se pudieron obtener los platos destacados")
        } catch (e: Exception) {
            return@withContext NetworkResult.Error("Error: ${e.message}")
        }
    }
}