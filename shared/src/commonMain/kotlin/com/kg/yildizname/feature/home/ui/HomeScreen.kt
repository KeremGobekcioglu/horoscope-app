package com.kg.yildizname.feature.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.kg.yildizname.core.ui.theme.YzBg
import com.kg.yildizname.core.ui.theme.YzInk

// Prompt 04: hero constellation, daily reading card, 4 score pills, shimmer skeleton, count-up.
@Composable
fun HomeScreen(
    onReadingDetail: (sign: String, period: String) -> Unit
) {
    Box(
        modifier         = Modifier.fillMaxSize().background(YzBg),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Home — Placeholder", color = YzInk)
    }
}
