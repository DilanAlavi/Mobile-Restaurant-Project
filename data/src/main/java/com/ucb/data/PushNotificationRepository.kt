package com.ucb.data

import com.ucb.data.push.IPushDataSource

class PushNotificationRepository(
    val push: IPushDataSource
) {

    suspend fun getToken(): String {
        return push.getToken()
    }
}