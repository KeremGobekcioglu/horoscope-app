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

    suspend operator fun invoke() {
        println("REGISTER DEVICE USE CASE IS CALLED.")
        val uid = authSource.ensureSignedIn()
        if (uid == null) {
            println("RegisterDeviceForNotifications: no uid, aborting")
            return
        }
        // IMPORTANT: tokenProvider.currentToken() must only be called after
        // permission is confirmed granted. On iOS, currentToken() waits on
        // APNsTokenWaiter, which only resolves if registerForRemoteNotifications()
        // was actually called (which only happens when permission is granted).
        // If this order is ever changed, or currentToken() is called from
        // anywhere else without this same guard, it can hang forever on iOS
        // when permission was denied. See APNsTokenWaiter.swift for details.
        val granted = permissionRequester.requestPermission()
        if (!granted) {
            println("RegisterDeviceForNotifications: permission denied, aborting")
            return
        }

        val token = pushTokenProvider.currentToken()
        if (token == null) {
            println("RegisterDeviceForNotifications: no token, aborting")
            return
        }

        firestoreSource.saveDeviceToken(uid, token)
        println("RegisterDeviceForNotifications: saved uid=$uid token=${token.take(12)}...")
    }
}