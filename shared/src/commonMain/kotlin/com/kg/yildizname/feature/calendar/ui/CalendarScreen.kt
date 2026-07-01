package com.kg.yildizname.feature.calendar.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.kg.yildizname.core.ui.theme.YzInk
import com.kg.yildizname.core.util.DateUtils
import com.kg.yildizname.feature.calendar.ui.components.Calendar
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.plus

// Prompt 08: monthly grid, energy dots, today pulse, month swipe, day-tap expand panel.
@Composable
fun CalendarScreen() {
    var date by remember { mutableStateOf(DateUtils.todayLocalDate()) }
    val mockLuckDays = remember { listOf(3, 7, 14, 21, 27) }

    Box(
        modifier         = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        /**
         * Title Burç takvimi
         *
         * < Month Year >
         *
         *  Days : Pt Sa Ça : CALENDAR
         *
         *  card of the day
         *
         *  card of the month
         */
        Calendar(
            date            = date,
            luckDays        = mockLuckDays,
            onNextMonth     = { date = date.plus(DatePeriod(months = 1)) },
            onPreviousMonth = { date = date.plus(DatePeriod(months = -1)) }
        )
    }
}
