package com.ucb.framework.service

import com.ucb.framework.dto.AvatarResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface IApiService {
    @GET("/users/{githubLogin}")
    suspend fun getInfoAvatar(@Path("githubLogin") githubLogin: String): Response<AvatarResponseDto>
}
