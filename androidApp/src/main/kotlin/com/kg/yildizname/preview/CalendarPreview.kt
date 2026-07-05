package com.kg.yildizname.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kg.yildizname.core.ui.theme.YzBg
import com.kg.yildizname.core.ui.theme.YzTheme
import com.kg.yildizname.core.util.DateUtils
import com.kg.yildizname.feature.calendar.ui.components.Calendar
import com.kg.yildizname.feature.calendar.ui.components.MonthlyReadingCard
import com.kg.yildizname.feature.calendar.ui.components.SelectedDailyReadingCard
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.plus

@Preview(name = "Calendar — with reading cards", showBackground = true, heightDp = 1000)
@Composable
private fun CalendarWithReadingCardsPreview() {
    YzTheme {
        var date by remember { mutableStateOf(DateUtils.todayLocalDate()) }
        val mockLuckDays = remember { listOf(3, 7, 14, 21, 27) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(YzBg)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Calendar(
                date = date,
                luckDays = mockLuckDays,
                onNextMonth = { date = date.plus(DatePeriod(months = 1)) },
                onPreviousMonth = { date = date.plus(DatePeriod(months = -1)) }
            )

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

            MonthlyReadingCard(
                monthlyComment = "Bu ay genel olarak kariyerinde yükseliş var. Temmuz ayı boyunca " +
                        "yeni fırsatlar seni bulacak, açık fikirli olmaya devam et."
            )
        }
    }
}