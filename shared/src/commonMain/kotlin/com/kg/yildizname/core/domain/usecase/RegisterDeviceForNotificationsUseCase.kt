package com.kg.yildizname.core.domain.usecase

import com.kg.yildizname.core.data.remote.AuthSource
import com.kg.yildizname.core.data.remote.FirestoreSource
import com.kg.yildizname.core.data.remote.PushTokenProvider
import com.kg.yildizname.platform.NotificationPermissionRequester
import kotlinx.coroutines.CancellationException

class RegisterDeviceForNotificationsUseCase(
    private val authSource: AuthSource,
    private val firestoreSource: FirestoreSource,
    private val pushTokenProvider: PushTokenProvider,
    private val permissionRequester: NotificationPermissionRequester
    )
{

    // Best-effort: a network/auth failure here must never take the whole app
    // down. In particular, the permission prompt + APNs token wait below can
    // take 20+ seconds, which is plenty of time for the user to trigger
    // Reset Data in Settings and delete the anonymous auth user this
    // invocation started with — so any failure past that point (chiefly a
    // Firestore permission-denied on a uid that no longer exists) is caught
    // and logged here, not left to crash the caller.
    suspend operator fun invoke() {
        println("REGISTER DEVICE USE CASE IS CALLED.")
        try {
            if (authSource.ensureSignedIn() == null) {
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

            // Re-resolve the uid here rather than reusing the one from before
            // the wait: if Reset Data ran during the wait above, the old
            // user is gone and this re-creates/re-validates the current one,
            // so the write lands on whoever is actually signed in now instead
            // of failing against a deleted account.
            val uid = authSource.ensureSignedIn()
            if (uid == null) {
                println("RegisterDeviceForNotifications: no uid after wait, aborting")
                return
            }

            firestoreSource.saveDeviceToken(uid, token)
            println("RegisterDeviceForNotifications: saved uid=$uid token=${token.take(12)}...")
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            println("RegisterDeviceForNotifications: failed: ${e.message}")
        }
    }
}