package com.ucb.framework.service

import com.ucb.framework.dto.topPick.TopPickResponseDto
import retrofit2.Response
import retrofit2.http.GET

interface ITopPickApiService {
    @GET("/api/json/v1/1/top_picks.php")
    suspend fun getTopPicks(): Response<TopPickResponseDto>
}