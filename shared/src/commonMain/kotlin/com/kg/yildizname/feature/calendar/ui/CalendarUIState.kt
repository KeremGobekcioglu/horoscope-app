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
        val selectedDate: String? = null,
        val installDate: LocalDate
    ) : CalendarUiState
    {
        val canGoToPreviousMonth: Boolean
            get() = date.year > installDate.year ||
                    (date.year == installDate.year &&
                            date.month.number > installDate.month.number)

        // One month ahead of "today" is allowed so users can see that the calendar
        // keeps going and isn't stuck on the current month; beyond that there's
        // nothing to show since readings aren't generated that far in advance.
        val canGoToNextMonth: Boolean
            get() {
                val today = DateUtils.todayLocalDate()
                val maxMonthNumber = today.month.number + 1
                val maxYear = if (maxMonthNumber > 12) today.year + 1 else today.year
                val maxMonth = if (maxMonthNumber > 12) 1 else maxMonthNumber
                return date.year < maxYear ||
                        (date.year == maxYear && date.month.number < maxMonth)
            }
    }

    data class Error(val message: String) : CalendarUiState
}

enum class PageTab(val page: Int)
{
    DAILY(1),
    MONTHLY(0)
}