package com.kg.yildizname.core.ui.utils

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

actual fun Modifier.yzStatusBarsPadding(): Modifier = statusBarsPadding()
actual fun Modifier.yzNavigationBarsPadding(): Modifier = navigationBarsPadding()
