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
import com.kg.yildizname.feature.calendar.ui.components.MonthlyReadingCard
import com.kg.yildizname.feature.calendar.ui.components.SelectedDailyReadingCard
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.plus

// Prompt 08: monthly grid, energy dots, today pulse, month swipe, day-tap expand panel.
@Composable
fun CalendarScreen(
    //uiState: CalendarUIState
) {
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
                    date = date,
                    luckDays = mockLuckDays,
                    onNextMonth = { date = date.plus(DatePeriod(months = 1)) },
                    onPreviousMonth = { date = date.plus(DatePeriod(months = -1)) }
                )
            }

            Box(modifier = Modifier.weight(0.4f))
            {
                SelectedDailyReadingCard(
                    scoreLove = 8,
                    scoreWork = 6,
                    scoreHealth = 7,
                    scoreLuck = 9,
                    dailyComment = "Bugün gezegenler senin lehine hizalanıyor. Sezgilerine güven, " +
                            "özellikle ilişkilerinde sabırlı olman gereken bir konu gündeme gelebilir.",
                    toReadingDetail = {},
                    date = "5 Temmuz"
                )
            }
            Box(modifier = Modifier.weight(0.2f)) {
                MonthlyReadingCard(
                    monthlyComment = "Temmuz ayı boyunca yeni fırsatlar seni bulacak, açık fikirli olmaya devam et."
                )
            }
        }
    }
}
