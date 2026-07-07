package com.kg.yildizname.feature.calendar.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.kg.yildizname.core.data.model.ZodiacSign
import com.kg.yildizname.core.data.repository.HoroscopeRepository
import com.kg.yildizname.core.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.datetime.LocalDate

class CalendarViewModel(
    private val horoscopeRepository: HoroscopeRepository,
    private val userRepository: UserRepository
) : ViewModel()
{
    private val _uiState = MutableStateFlow<CalendarUiState>(CalendarUiState.Loading)
    val uiState : StateFlow<CalendarUiState> = _uiState.asStateFlow()

    private var sign: ZodiacSign? = null
    private var lastFetchedData : String? = null

    private fun loadMonthlyReading(month: LocalDate)
    {

    }

    private fun loadDailyReading(date: LocalDate)
    {

    }

    private fun dailyKey(date: LocalDate) : String = date.toString()

    private fun monthlyKey(date: LocalDate) : String = "${date.year}-${date.monthNumber.toString().padStart(2,'0')}"
}