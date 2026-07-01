package com.kg.yildizname.feature.readingDetail.ui

import com.kg.yildizname.core.data.model.ZodiacSign

sealed interface ReadingDetailUiState {
    data object Loading : ReadingDetailUiState

    data class Success(
        val sign: ZodiacSign,
        val signDisplayName: String,
        val periodLabel: String,
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
