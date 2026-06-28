package com.kg.yildizname.feature.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kg.yildizname.core.ui.components.StarFieldBackground
import com.kg.yildizname.core.ui.theme.YzBg

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onReadingDetail: (sign: String, period: String) -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize().background(YzBg),
    ) {
        StarFieldBackground(Modifier.fillMaxSize())
    }
}