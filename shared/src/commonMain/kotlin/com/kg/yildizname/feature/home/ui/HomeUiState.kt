package com.kg.yildizname.feature.home.ui

import com.kg.yildizname.core.domain.model.ZodiacSign

data class HomeUiState(
    val isLoading: Boolean = true,
    val zodiacSign: ZodiacSign? = null,
    val birthDay: Int? = null,
    val birthMonth: Int? = null,
    val birthYear: Int? = null,
    val birthTime: String? = null,
    val birthCity: String? = null,
    val gender: String? = null,
)
