package com.kg.yildizname.core.util

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

object DateUtils {
    // kotlin.time.Instant and kotlinx.datetime.Instant are distinct types in 0.6.x.
    // Bridge via epoch millis so we can use kotlinx.datetime's toLocalDateTime extension.
    fun today(): String = todayLocalDate().toString()  // always "yyyy-MM-dd"
    val earliestAvailableDate: LocalDate = LocalDate(2026, Month.JULY, 1)  // whenever your Cloud Functions actually went live

    fun todayLocalDate(): LocalDate {
        val epochMs = kotlin.time.Clock.System.now().toEpochMilliseconds()
        return Instant.fromEpochMilliseconds(epochMs)
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date
    }
}
