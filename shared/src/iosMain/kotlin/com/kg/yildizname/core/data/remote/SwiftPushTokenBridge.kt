package com.kg.yildizname.core.data.remote

interface SwiftPushTokenBridge {
    suspend fun getToken(): String?
}

var swiftPushTokenBridge : SwiftPushTokenBridge? = null