package com.kg.yildizname.feature.calendar.ui

import com.kg.yildizname.core.data.model.Reading
import kotlinx.datetime.LocalDate
import kotlinx.datetime.number


sealed interface CalendarUiState{
    data object Loading: CalendarUiState

    data class Success(
        val date: LocalDate,
        val selectedDay: CalendarDay?,
        val selectedTab: PageTab = PageTab.MONTHLY,
        val luckDays: List<Int> = emptyList(),
        val dailyReading: Reading?,
        val monthlyReading: Reading?,
        val selectedDate: String? = null,
        val installDate: LocalDate
    ) : CalendarUiState
    {
        val canGoToPreviousMonth: Boolean
            get() = date.year > installDate.year ||
                    (date.year == installDate.year &&
                            date.month.number > installDate.month.number)
    }

    data class Error(val message: String) : CalendarUiState
}

enum class PageTab(val page: Int)
{
    DAILY(1),
    MONTHLY(0)
}