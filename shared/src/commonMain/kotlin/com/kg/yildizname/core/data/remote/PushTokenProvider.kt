package com.kg.yildizname.core.data.remote

interface PushTokenProvider {
    suspend fun currentToken() : String?
}