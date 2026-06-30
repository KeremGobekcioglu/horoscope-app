package com.kg.yildizname.feature.readingDetail.ui

import org.jetbrains.compose.resources.DrawableResource

sealed interface ReadingDetailUiState {
    data object Loading : ReadingDetailUiState

    data class Success(
        val signDisplayName: String,
        val periodLabel: String,
        val constellationArt: DrawableResource,
        val luckyNumber: Int,
        val luckyColorName: String,
        val generalText: String,
        val loveText: String,
        val careerText: String,
        val healthText: String,
        val luckText: String,
    ) : ReadingDetailUiState

    data class Error(val message: String) : ReadingDetailUiState
}
