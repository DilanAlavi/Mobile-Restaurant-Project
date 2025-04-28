package com.ucb.data

import com.ucb.data.meal.ITopPickRemoteDataSource
import com.ucb.domain.TopPick

class TopPickRepository(private val remoteDataSource: ITopPickRemoteDataSource) {
    suspend fun getTopPicks(): NetworkResult<List<TopPick>> {
        return remoteDataSource.getTopPicks()
    }
}