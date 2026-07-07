package com.kg.yildizname.feature.calendar.ui.components

import com.kg.yildizname.feature.calendar.ui.CalendarDay
import com.kg.yildizname.feature.calendar.ui.MonthRelation
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.todayIn
import kotlin.compareTo
import kotlin.time.Clock



fun calculateCalendar(date: LocalDate) : List<List<CalendarDay>>
{
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
    val previousMonth = today.previousMonth()
    val isCurrentMonth = date.year == today.year && date.month == today.month
    val isPreviousMonth = date.year == previousMonth.year && date.month == previousMonth.month
    val firstOfTheMonth = LocalDate(year = date.year,
        month = date.month,
        day = 1
    )

    val previousMonthDayCount = date.previousMonth().daysInMonth()
    val daysInMonth = date.daysInMonth()
    val leading: Int = leadingOffset(firstOfTheMonth)
    val trailing: Int = trailingOffset(leading,daysInMonth)
    val calendarDays = mutableListOf<CalendarDay>()
    val firstPreviousDay = previousMonthDayCount - leading + 1

    // from firstPreviousDay we ll start adding numbers until how many day of it's that month - for example if firstPrevDay is 28 , and prev month got 31 day
    // 28 29 30 31 should be added then we ll complete it to seven.
    for(day in firstPreviousDay..previousMonthDayCount)
    {
        val cellDate = LocalDate(date.previousMonth().year, date.previousMonth().month, day)
        calendarDays.add(CalendarDay(day = day, relation = MonthRelation.PREVIOUS , isAvailable = cellDate <= today))
    }
    for(day in 1..daysInMonth)
    {
        val cellDate = LocalDate(date.year, date.month, day)
        val isAvailable = if(isCurrentMonth) day <= today.day else isPreviousMonth
        calendarDays.add(CalendarDay(day = day, relation = MonthRelation.CURRENT , isAvailable = cellDate<=today))
    }
    for(day in 1..trailing)
    {
        val cellDate = LocalDate(date.nextMonth().year, date.nextMonth().month, day)
        calendarDays.add(CalendarDay(day = day, relation = MonthRelation.NEXT , isAvailable = cellDate<=today))
    }
    return calendarDays.chunked(7)
}

fun leadingOffset(firstOfTheMonth: LocalDate) : Int
{
    val isoDay = firstOfTheMonth.dayOfWeek.isoDayNumber
    val offset = isoDay - 1
    return offset
}

fun trailingOffset(leadingOffset: Int, daysInMonth: Int) : Int
{
    val usedCells = leadingOffset + daysInMonth
    val trailing = (7 - (usedCells % 7)) % 7
    return trailing
}

fun LocalDate.daysInMonth(): Int = when (month) {
    Month.JANUARY -> 31
    Month.FEBRUARY -> if (isLeapYear()) 29 else 28
    Month.MARCH -> 31
    Month.APRIL -> 30
    Month.MAY -> 31
    Month.JUNE -> 30
    Month.JULY -> 31
    Month.AUGUST -> 31
    Month.SEPTEMBER -> 30
    Month.OCTOBER -> 31
    Month.NOVEMBER -> 30
    Month.DECEMBER -> 31
}
fun LocalDate.isLeapYear(): Boolean {
    val year = this.year
    return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
}

fun LocalDate.previousMonth() : LocalDate {
    return (if(month == Month.JANUARY)
    {
        LocalDate(
            year = year - 1,
            month = Month.DECEMBER,
            day = 1
        )
    }
    else
    {
        val prevMonth = Month.entries[month.ordinal - 1]
        LocalDate(
            year = year,
            month = prevMonth,
            day = 1
        )
    })
}

fun LocalDate.nextMonth() : LocalDate {
    return (if(month == Month.DECEMBER)
    {
        LocalDate(
            year = year + 1,
            month = Month.JANUARY,
            day = 1
        )
    }
    else
    {
        val prevMonth = Month.entries[month.ordinal + 1]
        LocalDate(
            year = year,
            month = prevMonth,
            day = 1
        )
    })
}
/**
 *  26 27 28 29 30 1 2
 *  3 4   5   6  7  8  9
 *  10                 16
 *  17                  23
 *  24                  30
 */