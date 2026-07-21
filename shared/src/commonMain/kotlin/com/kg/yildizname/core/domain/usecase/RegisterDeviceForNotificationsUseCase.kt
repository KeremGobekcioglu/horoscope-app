package com.kg.yildizname.core.domain.usecase

import com.kg.yildizname.core.data.remote.AuthSource
import com.kg.yildizname.core.data.remote.FirestoreSource
import com.kg.yildizname.core.data.remote.PushTokenProvider
import com.kg.yildizname.platform.NotificationPermissionRequester

class RegisterDeviceForNotificationsUseCase(
    private val authSource: AuthSource,
    private val firestoreSource: FirestoreSource,
    private val pushTokenProvider: PushTokenProvider,
    private val permissionRequester: NotificationPermissionRequester
    )
{
    suspend operator fun invoke()
    {
        val uid = authSource.ensureSignedIn() ?: return
        val granted = permissionRequester.requestPermission()
        if(!granted) return
        val token = pushTokenProvider.currentToken() ?: return
        firestoreSource.saveDeviceToken(uid,token)
    }
}