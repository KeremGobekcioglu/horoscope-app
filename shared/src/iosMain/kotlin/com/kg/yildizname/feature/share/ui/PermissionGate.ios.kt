package com.kg.yildizname.feature.share.ui

import androidx.compose.runtime.Composable

@Composable
actual fun rememberGallerySaveGate(
    onGranted: () -> Unit,
    onDenied: () -> Unit,
): () -> Unit = onGranted