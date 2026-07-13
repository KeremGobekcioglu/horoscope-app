package com.kg.yildizname.feature.calendar.ui

import com.kg.yildizname.core.data.model.Reading
import com.kg.yildizname.core.util.DateUtils
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
        val selectedDate: String? = null
    ) : CalendarUiState
    {
        val canGoToPreviousMonth: Boolean
            get() = date.year > DateUtils.earliestAvailableDate.year ||
                    (date.year == DateUtils.earliestAvailableDate.year &&
                            date.month.number > DateUtils.earliestAvailableDate.month.number)
    }

    data class Error(val message: String) : CalendarUiState
}

enum class PageTab(val page: Int)
{
    DAILY(1),
    MONTHLY(0)
}