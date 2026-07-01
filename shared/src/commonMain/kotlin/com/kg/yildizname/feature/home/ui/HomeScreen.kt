package com.kg.yildizname.feature.home.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kg.yildizname.core.data.model.Reading
import com.kg.yildizname.core.ui.utils.YzWindowWidth
import com.kg.yildizname.core.ui.utils.rememberWindowWidth
import com.kg.yildizname.feature.home.ui.components.ConstellationHero
import com.kg.yildizname.feature.home.ui.components.DailyReadingCard
import com.kg.yildizname.feature.home.ui.components.EnergySection
import com.kg.yildizname.feature.home.ui.components.HomeErrorContent
import com.kg.yildizname.feature.home.ui.components.HomeLoadingContent
import com.kg.yildizname.feature.home.ui.components.HomeShareFab
import com.kg.yildizname.feature.home.ui.components.HomeTopBar
import com.kg.yildizname.feature.home.ui.components.SignHeader

// ─────────────────────────────────────────────
// Entry point — stateless, all callbacks injected
// ─────────────────────────────────────────────

/**
 * HomeScreen — stateless.
 *
 * The nav graph owns the Scaffold + YzBottomNav.
 * This composable is the content body only.
 *
 * @param uiState            Current state from HomeViewModel.
 * @param onReadMoreClick    Navigate to ReadingDetail(sign, period).
 * @param onShareClick       Open native share sheet for the reading text.
 * @param onShareCardClick   Open share card flow (rendered bitmap).
 * @param onNotificationClick Open notification settings.
 * @param onRetryClick       Re-trigger the reading fetch on error.
 */
@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onReadMoreClick: (sign: String, period: String) -> Unit,
    onShareClick: (text: String) -> Unit,
    onShareCardClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val windowWidth = rememberWindowWidth()

    // Constrain content width on tablets / large screens
    val contentMaxWidth: Dp = when (windowWidth) {
        YzWindowWidth.Compact  -> Dp.Infinity
        YzWindowWidth.Medium   -> 520.dp
        YzWindowWidth.Expanded -> 480.dp
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        when (uiState) {
            is HomeUiState.Loading -> HomeLoadingContent(
                contentMaxWidth = contentMaxWidth,
                onShareCardClick = onShareCardClick,
                onNotificationClick = onNotificationClick,
            )

            is HomeUiState.Error -> HomeErrorContent(
                message = uiState.message,
                onRetry = onRetryClick,
            )

            is HomeUiState.Success -> HomeSuccessContent(
                reading = uiState.reading,
                todayLabel = uiState.todayLabel,
                contentMaxWidth = contentMaxWidth,
                onReadMoreClick = onReadMoreClick,
                onShareClick = onShareClick,
                onShareCardClick = onShareCardClick,
                onNotificationClick = onNotificationClick,
            )
        }
    }
}

// ─────────────────────────────────────────────
// Success state
// ─────────────────────────────────────────────

@Composable
private fun HomeSuccessContent(
    reading: Reading,
    todayLabel: String,
    contentMaxWidth: Dp,
    onReadMoreClick: (sign: String, period: String) -> Unit,
    onShareClick: (text: String) -> Unit,
    onShareCardClick: () -> Unit,
    onNotificationClick: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .let { if (contentMaxWidth != Dp.Infinity) it.widthIn(max = contentMaxWidth) else it }
                .align(Alignment.TopCenter)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // ── Top bar ──
            HomeTopBar(
                dateLabel = todayLabel,
                onNotificationClick = onNotificationClick,
                onShareCardClick = onShareCardClick,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(24.dp))

            // ── Constellation image ──
            ConstellationHero(
                sign = reading.sign,
                modifier = Modifier
                    .fillMaxWidth(0.65f)
                    .aspectRatio(1f)
            )

            Spacer(Modifier.height(20.dp))

            // ── Sign name + date range ──
            SignHeader(
                sign = reading.sign,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(28.dp))

            // ── Daily reading card ──
            DailyReadingCard(
                text = reading.text,
                onReadMoreClick = {
                    onReadMoreClick(reading.sign.apiKey, reading.period.apiKey)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            )

            Spacer(Modifier.height(28.dp))

            // ── Energy section ──
            EnergySection(
                scores = reading.scores,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            )

            // Bottom padding — leaves room for FAB + bottom nav
            Spacer(Modifier.height(96.dp))
        }

        // ── Share FAB — floats bottom-right ──
        HomeShareFab(
            onClick = { onShareClick(reading.text) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 20.dp)
        )
    }
}
