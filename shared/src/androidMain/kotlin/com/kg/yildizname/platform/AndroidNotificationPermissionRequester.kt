package com.kg.yildizname.platform

import android.Manifest.permission.POST_NOTIFICATIONS
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
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
        // this condition is for api 13.
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return true
        val alreadyGranted = ContextCompat.checkSelfPermission(
            activity, POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
        if(alreadyGranted) return true
        return suspendCancellableCoroutine{
            cont ->
            pendingContinuation = cont
            launcher.launch(POST_NOTIFICATIONS)
        }
    }

    override suspend fun currentStatus(): PermissionStatus {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return PermissionStatus.GRANTED

        val granted = ContextCompat.checkSelfPermission(
            activity, POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED

        if(granted) return PermissionStatus.GRANTED
        // shouldShowRequestPermissionRationale returns true only if the user has
        // denied once but the system will still show the dialog again. It returns
        // false both when never asked AND when permanently denied — Android gives
        // no direct way to tell those apart, so this is the best signal available.

        val canAskAgain = activity.shouldShowRequestPermissionRationale(
            android.Manifest.permission.POST_NOTIFICATIONS
        )
        return if(canAskAgain) PermissionStatus.DENIED else PermissionStatus.NOT_DETERMINED
    }
}