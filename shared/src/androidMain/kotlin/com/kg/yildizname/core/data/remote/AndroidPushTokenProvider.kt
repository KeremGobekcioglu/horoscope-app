package com.kg.yildizname.core.data.remote

import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await

class AndroidPushTokenProvider : PushTokenProvider {
    override suspend fun currentToken(): String? {
        return try {
            FirebaseMessaging.getInstance().token.await()
        }
        catch (e: Exception)
        {
            null
        }
    }

    override suspend fun deleteToken() {
        FirebaseMessaging.getInstance().deleteToken().await()
    }
}