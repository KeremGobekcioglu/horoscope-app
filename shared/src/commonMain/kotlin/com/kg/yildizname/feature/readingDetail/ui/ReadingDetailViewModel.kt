package com.kg.yildizname.feature.readingDetail.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kg.yildizname.core.data.model.PeriodType
import com.kg.yildizname.core.data.model.ZodiacSign
import com.kg.yildizname.core.data.repository.HoroscopeRepository
import com.kg.yildizname.core.domain.model.ZodiacSigns
import com.kg.yildizname.core.util.DateUtils
import com.kg.yildizname.core.util.LuckyInfo
import com.kg.yildizname.core.util.currentLanguageCode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString

class ReadingDetailViewModel(
    private val signKey: String,
    private val periodKey: String,
    private val repository: HoroscopeRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<ReadingDetailUiState>(ReadingDetailUiState.Loading)
    val uiState: StateFlow<ReadingDetailUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    private fun load() {
        val sign = ZodiacSign.fromKey(signKey)
        val period = PeriodType.entries.firstOrNull { it.apiKey == periodKey.lowercase() }
            ?: PeriodType.DAILY
        val today = DateUtils.today()

        viewModelScope.launch {
            repository.getReading(sign, period, today)
                .catch { e -> _uiState.value = ReadingDetailUiState.Error(e.message ?: "error") }
                .collect { reading ->
                    _uiState.value = ReadingDetailUiState.Success(
                        sign             = sign,
                        signDisplayName  = getString(ZodiacSigns[sign.ordinal].nameRes),
                        periodLabel      = period.displayLabel(),
                        luckyNumber      = LuckyInfo.luckyNumber(sign.apiKey, today),
                        luckyColorName   = LuckyInfo.luckyColorName(sign.apiKey, today),
                        generalText      = reading.text,
                        loveText         = reading.categoryDetail?.love.orEmpty(),
                        careerText       = reading.categoryDetail?.work.orEmpty(),
                        healthText       = reading.categoryDetail?.health.orEmpty(),
                        luckText         = reading.categoryDetail?.luck.orEmpty(),
                    )
                }
        }
    }

    private fun PeriodType.displayLabel(): String =
        if (currentLanguageCode() == "tr") when (this) {
            PeriodType.DAILY   -> "Günlük"
            PeriodType.WEEKLY  -> "Haftalık"
            PeriodType.MONTHLY -> "Aylık"
        } else when (this) {
            PeriodType.DAILY   -> "Daily"
            PeriodType.WEEKLY  -> "Weekly"
            PeriodType.MONTHLY -> "Monthly"
        }
}
