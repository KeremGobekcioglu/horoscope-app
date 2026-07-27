package com.kg.yildizname.core.domain.usecase

import com.kg.yildizname.core.data.local.YildiznameDatabase
import com.kg.yildizname.core.data.remote.AuthSource
import com.kg.yildizname.core.data.remote.FirestoreSource
import com.kg.yildizname.core.data.remote.PushTokenProvider
import com.kg.yildizname.core.data.repository.UserRepository

class ResetAppDataUseCase(
    private val authSource: AuthSource,
    private val firestoreSource: FirestoreSource,
    private val pushTokenProvider: PushTokenProvider,
    private val userRepository: UserRepository,
    private val database: YildiznameDatabase,
) {
    // Server-side cleanup (FCM token, users/{uid} Firestore doc, anonymous auth
    // user). Safe to run from SettingsViewModel's viewModelScope before
    // navigating away — it never touches DataStore/Room, so it can't disturb
    // any screen still observing UserRepository.getSignFlow().
    //
    // Each step is best-effort: a network failure here must not block the
    // local wipe the user actually asked for, so every step is caught and
    // logged individually rather than propagating.
    suspend fun clearRemote() {
        // Delete the FCM token locally, then the users/{uid} Firestore doc —
        // both must happen while the anonymous auth session is still valid,
        // since Firestore rules require an authenticated owning user. Deleting
        // the auth user first would orphan the doc with no way to reach it.
        val uid = try {
            authSource.ensureSignedIn()
        } catch (e: Exception) {
            println("ResetAppDataUseCase: ensureSignedIn failed: ${e.message}")
            null
        }

        try {
            pushTokenProvider.deleteToken()
        } catch (e: Exception) {
            println("ResetAppDataUseCase: deleteToken failed: ${e.message}")
        }

        if (uid != null) {
            try {
                firestoreSource.deleteUserDoc(uid)
            } catch (e: Exception) {
                println("ResetAppDataUseCase: deleteUserDoc failed: ${e.message}")
            }
        }

        // Delete the anonymous auth user. A fresh anonymous user is created
        // the next time ensureSignedIn() runs (next app launch).
        try {
            authSource.deleteCurrentUser()
        } catch (e: Exception) {
            println("ResetAppDataUseCase: deleteCurrentUser failed: ${e.message}")
        }
    }

    // Local wipe (DataStore except language, then Room). MUST be called only
    // after navigation has torn down any screen still observing
    // UserRepository.getSignFlow() — HomeViewModel and CalendarViewModel both
    // collect it from init{}, and the bottom-nav's popUpTo(saveState = true)
    // keeps them alive in the background across a Settings visit. Clearing the
    // zodiac sign key while one of those collectors is still subscribed hits
    // an unsafe cast in UserPreferencesDataSource.getSignFlow() and crashes —
    // that cast is intentionally left as-is (out of scope here), so the
    // caller is responsible for sequencing this after a full-back-stack
    // navigate(popUpTo(0){inclusive=true}), which synchronously clears those
    // ViewModelStores. See the call site in YildiznameNavGraph.
    //
    // This also can't run on SettingsViewModel's viewModelScope: navigating
    // away pops Settings off the back stack too, clearing that ViewModel and
    // cancelling its scope. It's launched on the app-scoped coroutine
    // (core.util.appScope) from the nav graph instead, right after the
    // navigate() call, so it survives that.
    suspend fun clearLocal() {
        userRepository.clearAllExceptLanguage()

        // Room is a cache keyed by (sign, period, date). If clearing the
        // DataStore sign key above causes some other observer to write one
        // stray row before this line runs, that row is harmless and gets
        // pruned on the next fetch.
        database.clearAllTables()
    }
}
