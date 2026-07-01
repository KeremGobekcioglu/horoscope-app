package com.kg.yildizname.feature.calendar.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.kg.yildizname.core.ui.theme.YzInk

// Prompt 08: monthly grid, energy dots, today pulse, month swipe, day-tap expand panel.
@Composable
fun CalendarScreen() {
    Box(
        modifier         = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Calendar — Placeholder", color = YzInk)
    }
}
