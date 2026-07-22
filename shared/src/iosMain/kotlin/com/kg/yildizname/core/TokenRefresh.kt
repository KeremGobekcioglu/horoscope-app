package com.kg.yildizname.core

import com.kg.yildizname.core.data.remote.AuthSource
import com.kg.yildizname.core.data.remote.FirestoreSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.mp.KoinPlatformTools

private val tokenRefreshScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

fun onIosTokenRefreshed(token: String)
{
    val authSource : AuthSource = KoinPlatformTools.defaultContext().get().get()
    val firestoreSource : FirestoreSource = KoinPlatformTools.defaultContext().get().get()

    tokenRefreshScope.launch {
        val uid = authSource.ensureSignedIn() ?: return@launch
        firestoreSource.saveDeviceToken(uid,token)
        println("onIosTokenRefreshed: re-saved token for uid=$uid")
    }

}