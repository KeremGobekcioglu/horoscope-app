package com.kg.yildizname.feature.share.ui.platform

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
actual fun rememberGallerySaveGate(
    onGranted: () -> Unit,
    onDenied: () -> Unit
): () -> Unit {
    val context = LocalContext.current

    // rememberLauncherForActivityResult captures its callback at first composition; wrapping
    // in rememberUpdatedState means a recomposed lambda is the one that actually fires.
    val currentGranted by rememberUpdatedState(onGranted)
    val currentDenied by rememberUpdatedState(onDenied)

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
            granted ->
        if (granted)
        {
            currentGranted()
        }
        else
        {
            currentDenied()
        }
    }
    return {
        // API 29+ writes through MediaStore, which needs no permission — asking would be
        // pointless, and the manifest caps this permission at maxSdkVersion 28 anyway.
        val needsPermission = Build.VERSION.SDK_INT < Build.VERSION_CODES.Q &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED

        if (needsPermission) launcher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        else currentGranted()
    }
}