package com.kg.yildizname.feature.share.ui.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.launch
import platform.Photos.PHAuthorizationStatusAuthorized
import platform.Photos.PHAuthorizationStatusDenied
import platform.Photos.PHAuthorizationStatusLimited
import platform.Photos.PHAuthorizationStatusNotDetermined
import platform.Photos.PHAuthorizationStatusRestricted
import platform.Photos.PHPhotoLibrary
import platform.Photos.PHAccessLevelAddOnly
import androidx.compose.runtime.rememberCoroutineScope
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun rememberGallerySaveGate(
    onGranted: () -> Unit,
    onDenied: () -> Unit,
): () -> Unit {
    // Same reasoning as the Android side: the launch below is fired from a plain lambda
    // returned out of this composable, so it can run after a recomposition that swapped
    // these callbacks for new ones — rememberUpdatedState keeps it pointing at the latest pair.
    val currentGranted by rememberUpdatedState(onGranted)
    val currentDenied by rememberUpdatedState(onDenied)
    val scope = rememberCoroutineScope()

    return {
        val status = PHPhotoLibrary.authorizationStatusForAccessLevel(PHAccessLevelAddOnly)
        when (status) {
            PHAuthorizationStatusAuthorized,
            PHAuthorizationStatusLimited -> currentGranted()

            // Already denied once — iOS will never show the system dialog again for this
            // app, so calling requestAuthorization here would silently no-op. Go straight
            // to the caller's onDenied so it can route the user to Settings instead.
            PHAuthorizationStatusDenied,
            PHAuthorizationStatusRestricted -> currentDenied()

            // First ever ask — this is the one case where the system prompt still works.
            PHAuthorizationStatusNotDetermined -> {
                scope.launch {
                    requestPhotoAccess { granted ->
                        if (granted) currentGranted() else currentDenied()
                    }
                }
            }
            else -> currentDenied()
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun requestPhotoAccess(onResult: (Boolean) -> Unit) {
    PHPhotoLibrary.requestAuthorizationForAccessLevel(PHAccessLevelAddOnly) { newStatus ->
        val granted = newStatus == PHAuthorizationStatusAuthorized ||
                newStatus == PHAuthorizationStatusLimited
        // requestAuthorization's completion handler fires on an arbitrary background queue,
        // not necessarily main — the caller's onResult eventually flips Compose state, and
        // Compose state must only be touched from main.
        dispatch_async(dispatch_get_main_queue()) {
            onResult(granted)
        }
    }
}