package com.kg.yildizname.core.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp

enum class YzWindowWidth { Compact, Medium, Expanded }

@Composable
fun rememberWindowWidth(): YzWindowWidth {
    val density    = LocalDensity.current
    val windowInfo = LocalWindowInfo.current
    val widthDp    = with(density) { windowInfo.containerSize.width.toDp() }
    return when {
        widthDp < 600.dp -> YzWindowWidth.Compact
        widthDp < 840.dp -> YzWindowWidth.Medium
        else             -> YzWindowWidth.Expanded
    }
}
