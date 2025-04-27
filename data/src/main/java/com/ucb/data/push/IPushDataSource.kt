package com.ucb.data.push

interface IPushDataSource {
    suspend fun getToken(): String
}