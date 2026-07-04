package com.kg.yildizname.feature.calendar.ui

data class CalendarWeek(
    val start: Int,
    val end: Int
)

enum class MonthRelation { PREVIOUS, CURRENT, NEXT }

data class CalendarDay(
    val day: Int,
    val relation: MonthRelation,   // replaces currentMonth: Boolean
    val isAvailable: Boolean = false
)

data class SelectedDay(val day: Int, val relation: MonthRelation)
