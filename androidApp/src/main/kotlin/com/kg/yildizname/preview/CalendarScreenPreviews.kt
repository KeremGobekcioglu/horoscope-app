package com.kg.yildizname.preview

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kg.yildizname.core.data.model.PeriodType
import com.kg.yildizname.core.data.model.Reading
import com.kg.yildizname.core.data.model.ScoreSet
import com.kg.yildizname.core.data.model.ZodiacSign
import com.kg.yildizname.core.ui.components.StarFieldBackground
import com.kg.yildizname.core.ui.theme.YzTheme
import com.kg.yildizname.core.util.DateUtils
import com.kg.yildizname.feature.calendar.ui.CalendarDay
import com.kg.yildizname.feature.calendar.ui.CalendarScreen
import com.kg.yildizname.feature.calendar.ui.CalendarUiState
import com.kg.yildizname.feature.calendar.ui.MonthRelation
import com.kg.yildizname.feature.calendar.ui.PageTab
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.plus

private val previewDailyReading = Reading(
    sign = ZodiacSign.SCORPIO,
    period = PeriodType.DAILY,
    date = "2026-07-05",
    text = "Bugün gezegenler senin lehine hizalanıyor. Sezgilerine güven, " +
        "özellikle ilişkilerinde sabırlı olman gereken bir konu gündeme gelebilir.",
    scores = ScoreSet(love = 8, work = 6, health = 7, luck = 9),
)

private val previewMonthlyReading = Reading(
    sign = ZodiacSign.SCORPIO,
    period = PeriodType.MONTHLY,
    date = "2026-07-01",
    text = "Bu ay genel olarak kariyerinde yükseliş var. Temmuz ayı boyunca " +
        "yeni fırsatlar seni bulacak, açık fikirli olmaya devam et.",
    scores = ScoreSet(love = 7, work = 9, health = 6, luck = 8),
)

private val previewSelectedDay = CalendarDay(day = 5, relation = MonthRelation.CURRENT, isAvailable = true)

@Composable
private fun CalendarPreviewScaffold(uiState: CalendarUiState) {
    YzTheme {
        StarFieldBackground(Modifier.fillMaxSize())
        CalendarScreen(
            uiState = uiState,
            onNextMonth = {},
            onPreviousMonth = {},
            onDaySelectedDay = {},
            onTabChange = {},
            onReadMoreClick = { _, _ -> },
            onRetryClick = {},
            {}
        )
    }
}

@Preview(name = "Calendar — Loading", showBackground = true)
@Composable
private fun CalendarScreenLoadingPreview() {
    CalendarPreviewScaffold(uiState = CalendarUiState.Loading)
}

@Preview(name = "Calendar — Error", showBackground = true)
@Composable
private fun CalendarScreenErrorPreview() {
    CalendarPreviewScaffold(
        uiState = CalendarUiState.Error(message = "Takvim yüklenemedi. Lütfen tekrar deneyin.")
    )
}

@Preview(name = "Calendar — Monthly tab, with reading", showBackground = true, heightDp = 900)
@Composable
private fun CalendarScreenMonthlyPreview() {
    CalendarPreviewScaffold(
        uiState = CalendarUiState.Success(
            date = DateUtils.todayLocalDate(),
            selectedDay = null,
            selectedTab = PageTab.MONTHLY,
            luckDays = listOf(3, 7, 14, 21, 27),
            dailyReading = null,
            monthlyReading = previewMonthlyReading,
        )
    )
}

@Preview(name = "Calendar — Daily tab, day selected", showBackground = true, heightDp = 900)
@Composable
private fun CalendarScreenDailyPreview() {
    CalendarPreviewScaffold(
        uiState = CalendarUiState.Success(
            date = DateUtils.todayLocalDate(),
            selectedDay = previewSelectedDay,
            selectedTab = PageTab.DAILY,
            luckDays = listOf(3, 7, 14, 21, 27),
            dailyReading = previewDailyReading,
            monthlyReading = previewMonthlyReading,
        )
    )
}

@Preview(name = "Calendar — Daily tab, no day picked", showBackground = true, heightDp = 900)
@Composable
private fun CalendarScreenDailyEmptyPreview() {
    CalendarPreviewScaffold(
        uiState = CalendarUiState.Success(
            date = DateUtils.todayLocalDate(),
            selectedDay = null,
            selectedTab = PageTab.DAILY,
            luckDays = listOf(3, 7, 14, 21, 27),
            dailyReading = null,
            monthlyReading = previewMonthlyReading,
        )
    )
}

@Preview(name = "Calendar — Monthly tab, future month (no reading)", showBackground = true, heightDp = 900)
@Composable
private fun CalendarScreenMonthlyEmptyPreview() {
    CalendarPreviewScaffold(
        uiState = CalendarUiState.Success(
            date = DateUtils.todayLocalDate().plus(DatePeriod(months = 1)),
            selectedDay = null,
            selectedTab = PageTab.MONTHLY,
            luckDays = emptyList(),
            dailyReading = null,
            monthlyReading = null,
        )
    )
}