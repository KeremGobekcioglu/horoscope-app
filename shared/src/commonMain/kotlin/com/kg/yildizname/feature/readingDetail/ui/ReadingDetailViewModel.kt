package com.kg.yildizname.feature.readingDetail.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kg.yildizname.core.data.model.PeriodType
import com.kg.yildizname.core.data.model.ZodiacSign
import com.kg.yildizname.core.data.repository.HoroscopeRepository
import com.kg.yildizname.core.ui.utils.DateFormatter
import com.kg.yildizname.core.util.DateUtils
import com.kg.yildizname.core.util.LuckyInfo
import com.kg.yildizname.core.util.currentLanguageCode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString

class ReadingDetailViewModel(
    private val signKey: String,
    private val periodKey: String,
    private val date: String? = null,
    private val repository: HoroscopeRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReadingDetailUiState())
    val uiState: StateFlow<ReadingDetailUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    private fun load() {
        val sign = ZodiacSign.fromKey(signKey)
        val period = PeriodType.entries.firstOrNull { it.apiKey == periodKey.lowercase() }
            ?: PeriodType.DAILY
        val toFetched = date ?: DateUtils.today()
        if(date != null)
        {
            println("DATE NOT NULL ON READING DETAIL AND THIS IS RECEIVED : $date")
            _uiState.update {
                it.copy(
                    formattedDate = DateFormatter.formatDate(date)
                )
            }
        }
        println("ReadingDetailViewModel: load() sign=${sign.apiKey} period=${period.apiKey} date=$toFetched")

        viewModelScope.launch {
            repository.getReading(sign, period, toFetched)
                .catch { e ->
                    println("ReadingDetailViewModel: ERROR ${e.message}")
                    e.printStackTrace()
                    //_uiState.value = ReadingDetailUiState.Error(e.message ?: "error")
                    _uiState.update {
                        it.copy(
                            err = e.message ?: "error",
                            isLoading = false
                        )
                    }


                }
                .collect { reading ->
                    println("ReadingDetailViewModel: got reading text=${reading.text.take(40)} categoryDetail=${reading.categoryDetail}")
//                    _uiState.value = ReadingDetailUiState.Success(
//                        sign             = sign,
//                        signDisplayName  = getString(sign.nameRes),
//                        periodLabel      = period.displayLabel(),
//                        luckyNumber      = LuckyInfo.luckyNumber(sign.apiKey, toFetched),
//                        luckyColorName   = LuckyInfo.luckyColorName(sign.apiKey, toFetched),
//                        generalText      = reading.text,
//                        loveText         = reading.categoryDetail?.love.orEmpty(),
//                        careerText       = reading.categoryDetail?.work.orEmpty(),
//                        healthText       = reading.categoryDetail?.health.orEmpty(),
//                        luckText         = reading.categoryDetail?.luck.orEmpty(),
//                    )
                    _uiState.update {
                        it.copy(
                            sign             = sign,
                            signDisplayName  = getString(sign.nameRes),
                            periodLabel      = period.displayLabel(),
                            luckyNumber      = LuckyInfo.luckyNumber(sign.apiKey, toFetched),
                            luckyColorName   = LuckyInfo.luckyColorName(sign.apiKey, toFetched),
                            generalText      = reading.text,
                            loveText         = reading.categoryDetail?.love.orEmpty(),
                            careerText       = reading.categoryDetail?.work.orEmpty(),
                            healthText       = reading.categoryDetail?.health.orEmpty(),
                            luckText         = reading.categoryDetail?.luck.orEmpty(),
                            isLoading = false,
                            //formattedDate = toFetched
                        )
                    }
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
