package com.kg.yildizname.feature.calendar.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kg.yildizname.core.ui.components.StarFieldBackground
import com.kg.yildizname.core.ui.theme.YzBg
import com.kg.yildizname.core.ui.theme.YzGold
import com.kg.yildizname.core.ui.theme.YzInk
import com.kg.yildizname.core.ui.utils.YzWindowWidth
import com.kg.yildizname.core.ui.utils.rememberWindowWidth
import com.kg.yildizname.core.util.DateUtils
import com.kg.yildizname.feature.calendar.ui.components.Calendar
import com.kg.yildizname.feature.calendar.ui.components.CalendarErrorContent
import com.kg.yildizname.feature.calendar.ui.components.CalendarLoadingContent
import com.kg.yildizname.feature.calendar.ui.components.MonthlyReadingCard
import com.kg.yildizname.feature.calendar.ui.components.SelectedDailyReadingCard
import horoscope.shared.generated.resources.Res
import horoscope.shared.generated.resources.calendar_title
import horoscope.shared.generated.resources.period_daily
import horoscope.shared.generated.resources.period_monthly
import kotlinx.coroutines.launch
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus
import org.jetbrains.compose.resources.stringResource

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

    when (uiState) {
        is CalendarUiState.Loading -> CalendarLoadingContent()
        is CalendarUiState.Error -> CalendarErrorContent(uiState.message, onRetry = onRetryClick)
        is CalendarUiState.Success -> CalendarScreenSuccessContent(
            uiState = uiState,
            onNextMonth = onNextMonth,
            onPreviousMonth = onPreviousMonth,
            onDaySelected = onDaySelectedDay,
            onReadMoreClick = onReadMoreClick,
            onTabChange = onTabChange
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
    onTabChange: (PageTab) -> Unit
) {
    val pagerState: PagerState = rememberPagerState { 2 }
    val scope = rememberCoroutineScope()
    LaunchedEffect(pagerState)
    {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            onTabChange(PageTab.entries.first { it.page == page})
        }
    }
    LaunchedEffect(uiState.selectedTab)
    {
        if(pagerState.currentPage != uiState.selectedTab.page)
        {
            pagerState.animateScrollToPage(uiState.selectedTab.page)
        }
    }

    val windowWidth = rememberWindowWidth()
    val contentMaxWidth = when (windowWidth) {
        YzWindowWidth.Compact -> Dp.Unspecified
        YzWindowWidth.Medium -> 600.dp
        YzWindowWidth.Expanded -> 720.dp
    }
    StarFieldBackground(Modifier.fillMaxSize())

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
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
                .widthIn(max = contentMaxWidth)
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(16.dp))
            Text(
                text = stringResource(Res.string.calendar_title),
                style = MaterialTheme.typography.headlineMedium,
                color = YzGold
            )
            Box(modifier = Modifier.weight(1f) , contentAlignment = Alignment.TopCenter)
            {
                Calendar(
                    date = uiState.date,
                    luckDays = uiState.luckDays,
                    onNextMonth = onNextMonth,
                    onPreviousMonth = onPreviousMonth,
                    onDaySelected = onDaySelected,
                    selectedDay = uiState.selectedDay
                )
            }


            SecondaryTabRow(
                selectedTabIndex = pagerState.currentPage,
                containerColor = Color.Transparent,
                contentColor = YzGold,
//                divider = {}
            )
            {
                Tab(
                    selected = pagerState.currentPage == PageTab.MONTHLY.page,
                    onClick = {
                        scope.launch { pagerState.animateScrollToPage(PageTab.MONTHLY.page) }
                    },
                    text = {
                        Text(
                            text = stringResource(Res.string.period_monthly)
                        )
                    }
                )


                Tab(
                    selected = pagerState.currentPage == PageTab.DAILY.page,
                    onClick = {
                        scope.launch { pagerState.animateScrollToPage(PageTab.DAILY.page) }
                    },
                    text = {
                        Text(
                            text = stringResource(Res.string.period_daily)
                        )
                    }
                )
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            { page ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
//                        .verticalScroll(rememberScrollState())
                ) {
                    when (page) {
                        PageTab.MONTHLY.page -> {
                            MonthlyReadingCard(
                                monthlyComment = uiState.monthlyReading?.text ?: "Monthly comment is empty"
                            )
                        }

                        PageTab.DAILY.page -> {
                            SelectedDailyReadingCard(
                                scoreLove = uiState.dailyReading?.scores?.love ?: 0,
                                scoreWork = uiState.dailyReading?.scores?.work ?: 0,
                                scoreHealth = uiState.dailyReading?.scores?.health ?: 0,
                                scoreLuck = uiState.dailyReading?.scores?.luck ?: 0,
                                dailyComment = uiState.dailyReading?.text ?: "Daily comment is empty.",
                                toReadingDetail = {
                                    onReadMoreClick(
                                        uiState.dailyReading?.sign?.apiKey ?: "",
                                        uiState.dailyReading?.period?.apiKey ?: ""
                                    )
                                },
                                date = DateFormatter.fullDate(
                                    LocalDate.parse(uiState.dailyReading?.date ?: DateUtils.todayLocalDate().toString())
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}