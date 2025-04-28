package com.ucb.usecases

import com.ucb.data.NetworkResult
import com.ucb.data.TopPickRepository
import com.ucb.domain.TopPick

class GetTopPicks(private val topPickRepository: TopPickRepository) {
    suspend operator fun invoke(): NetworkResult<List<TopPick>> {
        return topPickRepository.getTopPicks()
    }
}