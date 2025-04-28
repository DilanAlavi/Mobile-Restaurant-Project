package com.ucb.data.meal

import com.ucb.data.NetworkResult
import com.ucb.domain.TopPick

interface ITopPickRemoteDataSource {
    suspend fun getTopPicks(): NetworkResult<List<TopPick>>
}