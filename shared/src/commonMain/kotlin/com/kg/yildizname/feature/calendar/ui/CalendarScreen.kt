package com.kg.yildizname.feature.calendar.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kg.yildizname.core.ui.theme.YzBg
import com.kg.yildizname.core.ui.theme.YzInk
import com.kg.yildizname.core.util.DateUtils
import com.kg.yildizname.feature.calendar.ui.components.Calendar
import com.kg.yildizname.feature.calendar.ui.components.CalendarErrorContent
import com.kg.yildizname.feature.calendar.ui.components.CalendarLoadingContent
import com.kg.yildizname.feature.calendar.ui.components.MonthlyReadingCard
import com.kg.yildizname.feature.calendar.ui.components.SelectedDailyReadingCard
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus

// Prompt 08: monthly grid, energy dots, today pulse, month swipe, day-tap expand panel.
@Composable
fun CalendarScreen(
    uiState: CalendarUiState,
    onNextMonth: () -> Unit,
    onPreviousMonth: () -> Unit,
    onDaySelectedDay: (CalendarDay) -> Unit,
    onTabChange: (PageTab) -> Unit,
    onReadMoreClick: (sign: String, period: String) -> Unit,
    onRetryClick: () -> Unit
) {

    when(uiState)
    {
        is CalendarUiState.Loading -> CalendarLoadingContent()
        is CalendarUiState.Error -> CalendarErrorContent(uiState.message, onRetry = onRetryClick)
        is CalendarUiState.Success -> CalendarScreenSuccessContent(
            uiState = uiState,
            onNextMonth = onNextMonth,
            onPreviousMonth = onPreviousMonth,
            onDaySelected = onDaySelectedDay,
            onReadMoreClick = onReadMoreClick
        )
    }
}

@Composable
private fun CalendarScreenSuccessContent(
    uiState: CalendarUiState.Success,
    onNextMonth: () -> Unit,
    onPreviousMonth: () -> Unit,
    onDaySelected: (CalendarDay) -> Unit,
    onReadMoreClick: (sign: String, period: String) -> Unit,
) {

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


                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(YzBg)
                        //.verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Box(modifier = Modifier.weight(0.4f))
                    {
                        Calendar(
                            date = uiState.date,
                            luckDays = uiState.luckDays,
                            onNextMonth = onNextMonth,
                            onPreviousMonth = onPreviousMonth,
                            onDaySelected = { day -> onDaySelected(day) },
                            selectedDay = uiState.selectedDay
                        )
                    }

                    Box(modifier = Modifier.weight(0.4f))
                    {
                        SelectedDailyReadingCard(
                            scoreLove = uiState.dailyReading.scores.love,
                            scoreWork = uiState.dailyReading.scores.work,
                            scoreHealth = uiState.dailyReading.scores.health,
                            scoreLuck = uiState.dailyReading.scores.luck,
                            dailyComment = uiState.dailyReading.text,
                            toReadingDetail = {
                                onReadMoreClick(uiState.dailyReading.sign.apiKey, uiState.dailyReading.period.apiKey)
                            },
                            date = DateFormatter.fullDate(LocalDate.parse(uiState.dailyReading.date))
                        )
                    }
                    Box(modifier = Modifier.weight(0.2f)) {
                        MonthlyReadingCard(
                            monthlyComment = uiState.monthlyReading.text
                        )
                    }
                }
            }
}