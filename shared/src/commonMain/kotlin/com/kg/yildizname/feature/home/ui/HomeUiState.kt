package com.kg.yildizname.feature.home.ui

import com.kg.yildizname.core.data.model.Reading

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(
        val reading: Reading,
        val todayLabel: String,
    ) : HomeUiState
    data class Error(val message: String) : HomeUiState
}
