package com.kg.yildizname.core.domain.usecase

import com.kg.yildizname.core.data.remote.AuthSource
import com.kg.yildizname.core.data.remote.FirestoreSource
import com.kg.yildizname.core.data.remote.PushTokenProvider

class RegisterDeviceForNotificationsUseCase(
    private val authSource: AuthSource,
    private val firestoreSource: FirestoreSource,
    private val pushTokenProvider: PushTokenProvider
)
{
    suspend operator fun invoke()
    {
        val uid = authSource.ensureSignedIn() ?: return
        val token = pushTokenProvider.currentToken() ?: return
        firestoreSource.saveDeviceToken(uid,token)
    }
}