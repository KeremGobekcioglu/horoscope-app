package com.kg.yildizname.feature.calendar.ui

import com.kg.yildizname.core.data.model.Reading
import kotlinx.datetime.LocalDate


sealed interface CalendarUiState{
    data object Loading: CalendarUiState

    data class Success(
        val date: LocalDate,
        val selectedDay: CalendarDay?,
        val selectedTab: PageTab = PageTab.MONTHLY,
        val luckDays: List<Int> = emptyList(),
        val dailyReading: Reading,
        val monthlyReading: Reading
    ) : CalendarUiState

    data class Error(val message: String) : CalendarUiState
}

//data class CalendarUIState(
//    val date: LocalDate,
//    val selectedDay: CalendarDay?,
//    val selectedTab: PageTab = PageTab.MONTHLY,
//    val mockLuckDays: List<Int> = emptyList()
//)

enum class PageTab(val page: Int)
{
    DAILY(1),
    MONTHLY(0)
}