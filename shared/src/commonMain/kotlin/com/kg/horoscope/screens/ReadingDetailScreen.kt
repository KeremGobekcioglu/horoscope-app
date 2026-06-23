package com.kg.horoscope.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.kg.horoscope.ui.theme.YzBg
import com.kg.horoscope.ui.theme.YzInk

// Prompt 05: parallax hero, sectioned text (Genel/Aşk/Kariyer/Sağlık), lucky details, share.
@Composable
fun ReadingDetailScreen(
    sign: String,
    period: String,
    onBack: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(YzBg),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "ReadingDetail — $sign / $period",
            color = YzInk
        )
    }
}
