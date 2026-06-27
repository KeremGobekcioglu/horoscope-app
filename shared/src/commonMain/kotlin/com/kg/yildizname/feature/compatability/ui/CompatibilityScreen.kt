package com.kg.yildizname.feature.compatability.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.kg.yildizname.core.ui.theme.YzBg
import com.kg.yildizname.core.ui.theme.YzInk

// Prompt 07: two sign pickers, gravity-pull animation, score count-up, particle burst, connection line.
@Composable
fun CompatibilityScreen() {
    Box(
        modifier         = Modifier.fillMaxSize().background(YzBg),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Compatibility — Placeholder", color = YzInk)
    }
}
