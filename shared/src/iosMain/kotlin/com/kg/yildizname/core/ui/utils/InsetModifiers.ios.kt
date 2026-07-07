package com.kg.yildizname.core.ui.utils

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

actual fun Modifier.yzStatusBarsPadding(): Modifier = padding(top = 24.dp)
actual fun Modifier.yzNavigationBarsPadding(): Modifier = navigationBarsPadding()
