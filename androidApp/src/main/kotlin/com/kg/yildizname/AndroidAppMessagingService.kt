package com.kg.yildizname

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.kg.yildizname.core.data.remote.AuthSource
import com.kg.yildizname.core.data.remote.FirestoreSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.core.context.GlobalContext

class AndroidAppMessagingService : FirebaseMessagingService() {

    // The OS creates this service, so we can't use constructor injection.
    // We reach into Koin directly (service-locator) — the accepted pattern
    // for framework-instantiated Android components. See conversation notes.
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "onNewToken fired: ${token.take(12)}...")
        // FCM issued a new token; the copy in Firestore is now stale.
        // Re-save it so notifications keep reaching this device.
        val firestoreSource: FirestoreSource = GlobalContext.get().get()
        val authSource : AuthSource = GlobalContext.get().get()

        scope.launch {
            val uid = authSource.ensureSignedIn() ?: return@launch
            firestoreSource.saveDeviceToken(uid,token)
            Log.d("FCM", "onNewToken: re-saved token for uid=$uid")
        }
    }
}