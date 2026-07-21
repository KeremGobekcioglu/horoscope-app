package com.kg.yildizname.platform

import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class AndroidNotificationPermissionRequester(
    private val activity: ComponentActivity
) : NotificationPermissionRequester {

    private var pendingContinuation : CancellableContinuation<Boolean>? =  null

    private val launcher = activity.registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    )
    {
        granted ->
        pendingContinuation?.resume(granted)
        pendingContinuation = null
    }

    override suspend fun requestPermission(): Boolean {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return true
        return suspendCancellableCoroutine{
            cont ->
            pendingContinuation = cont
            launcher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}