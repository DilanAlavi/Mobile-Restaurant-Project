package com.ucb.framework.service

import com.chuckerteam.chucker.api.ChuckerInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import android.content.Context

class RetrofitBuilder(
    val context: Context
) {

    private fun getRetrofit(url: String): Retrofit {

        val client = OkHttpClient.Builder()
            .addInterceptor(ChuckerInterceptor.Builder(context).build())
            .build()

        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl(url)
            .client(client)
            .build()
    }
    val mealApiService: IMealApiService = getRetrofit(BASE_URL_MEALS).create(IMealApiService::class.java)
    val categoryMealApiService: ICategoryMealApiService = getRetrofit(BASE_URL_MEALS).create(ICategoryMealApiService::class.java)


    companion object {
        private const val BASE_URL_MEALS = "https://www.themealdb.com"
    }
}
