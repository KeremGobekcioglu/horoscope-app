package com.kg.yildizname.core.data.remote

interface SwiftPushTokenBridge {
    suspend fun getToken(): String?
    suspend fun deleteToken()
}

var swiftPushTokenBridge : SwiftPushTokenBridge? = null