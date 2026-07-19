package com.kg.yildizname.core.data.remote

class iOSPushTokenProvider(): PushTokenProvider {
    override suspend fun currentToken(): String? {
        return swiftPushTokenBridge?.getToken()
    }
}