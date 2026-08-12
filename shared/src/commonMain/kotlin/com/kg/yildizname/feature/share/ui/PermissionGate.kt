package com.kg.yildizname.feature.share.ui
import androidx.compose.runtime.Composable

/**
 * Returns a lambda to invoke when the user taps "save to gallery". It either calls [onGranted]
 * straight away (permission not required on this platform/OS version, or already held) or
 * shows the OS permission prompt and calls back once the user answers.
 *
 * Lives here rather than inside ShareManager because requesting a permission needs an Activity
 * and a result callback, neither of which a Koin-singleton holding an application Context has.
 */
@Composable
expect fun rememberGallerySaveGate(
    onGranted: () -> Unit,
    onDenied: () -> Unit,
): () -> Unit