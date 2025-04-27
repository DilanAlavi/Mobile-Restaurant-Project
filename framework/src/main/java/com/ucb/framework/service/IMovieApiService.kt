package com.ucb.framework.service

import com.ucb.framework.dto.MovieResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface IMovieApiService {
    @GET("/3/discover/movie?sort_by=popularity.desc")
    suspend fun fetchPopularMovies(@Query("api_key") token: String): Response<MovieResponseDto>

}