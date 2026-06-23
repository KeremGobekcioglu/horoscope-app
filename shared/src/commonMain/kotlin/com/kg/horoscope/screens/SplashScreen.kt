package com.kg.horoscope.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.kg.horoscope.ui.theme.YzBg
import com.kg.horoscope.ui.theme.YzGold
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToOnboarding: () -> Unit
) {
    // Prompt 02: star-field fade-in, logo scale, constellation draw-on, exit transition.
    // For now: auto-route to Onboarding (first run placeholder).
    LaunchedEffect(Unit) {
        delay(1500)
        onNavigateToOnboarding()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(YzBg),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Yıldızname",
            color = YzGold
        )
    }
}
