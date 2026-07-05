package com.kg.yildizname.feature.calendar.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kg.yildizname.core.ui.theme.YzBg
import com.kg.yildizname.core.ui.theme.YzGold
import com.kg.yildizname.core.ui.theme.YzOnSurface
import com.kg.yildizname.feature.calendar.ui.CalendarDay
import com.kg.yildizname.feature.calendar.ui.MonthRelation
import compose.icons.FeatherIcons
import compose.icons.feathericons.ChevronLeft
import compose.icons.feathericons.ChevronRight
import compose.icons.feathericons.ChevronsRight
import horoscope.shared.generated.resources.Res
import horoscope.shared.generated.resources.next_month
import horoscope.shared.generated.resources.previous_month
import io.ktor.client.request.invoke
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.number
import kotlinx.datetime.todayIn
import kotlin.time.Clock

@Composable
fun Calendar(
    date: LocalDate,
    luckDays: List<Int> = mutableListOf<Int>(),
    onNextMonth: () -> Unit,
    onPreviousMonth: () -> Unit
)
{
    val days : List<String> = listOf(
        "Pzt","Sa","Ça","Pe","Cu","Cmt","Pzr"
    )
    val weeks  = calculateCalendar(date)
    /**
     * < Month Year >
     *
     *  Days: Pzt Sa Ça Prş and english counterintiatives
     *  Each day
     *
     *  The lucky days bullet is shiny
     */
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Spacer(Modifier.height(32.dp))
        // < Month Year >
        Row(modifier = Modifier.fillMaxWidth(),
            Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = { onPreviousMonth() }
            )
            {
                Icon(
                    imageVector = FeatherIcons.ChevronLeft,
                    contentDescription = Res.string.previous_month.toString(),
                    tint = YzGold
                )
            }
            Text(text = DateFormatter.monthYear(date),
                color = YzGold
            )
            IconButton(
                onClick = { onNextMonth() }
            )
            {
                Icon(
                    imageVector = FeatherIcons.ChevronRight,
                    contentDescription = Res.string.next_month.toString(),
                    tint = YzGold
                )
            }
        }

        // Days
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            days.forEach {
                    day ->
                Text(
                    text = day,
                    color = YzGold.copy(0.8f),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Column(modifier = Modifier.fillMaxWidth())
        {
            weeks.forEach {
                    week ->
                CalendarRow(week = week, selectedDay = 30, selectedRelation = MonthRelation.PREVIOUS)            }
        }

//        SelectedDailyReadingCard()
//        MonthlyReadingCard()
    }
}

@Composable
fun DayComposable(modifier: Modifier = Modifier, isLuckyDay: Boolean = false, isSelected: Boolean = false, day: Int, isNextMonth: Boolean, isAvailable: Boolean)
{
    val canShine = (isSelected || isLuckyDay) && isAvailable
    val shape = RoundedCornerShape(16.dp)

    Box(
        modifier = modifier.size(48.dp)
            .then(
                if(canShine) {
                    Modifier.shadow(elevation = 0.dp, shape = shape, ambientColor = YzGold, spotColor = YzGold)
                } else Modifier
            )
            .clip(shape)
            .background(if(canShine) YzBg.copy(0.1f) else Color.Transparent)
            .then(
                if(canShine) {
                    Modifier.border(width = 2.dp, color = YzGold, shape = shape)
                } else Modifier
            ),
        contentAlignment = Alignment.Center
    )
    {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ){
            Text(
                text = "$day",
                color = if(isAvailable) YzGold else if(!isNextMonth) YzGold.copy(0.5f) else Color.LightGray.copy(0.4f),
                fontSize = 20.sp,
                fontWeight = if(isSelected && isAvailable) FontWeight.Bold else FontWeight.Normal
            )
            Box(
                modifier = Modifier.size(4.dp)
                    .clip(CircleShape)
                    .background(
                        if(isNextMonth) Color.Transparent
                        else if(isSelected) YzGold
                        else YzGold.copy(0.5f)
                    )
            )
        }
    }
}

@Composable
fun CalendarRow(luckDays: List<Int> = emptyList(), week: List<CalendarDay>, selectedDay: Int, selectedRelation: MonthRelation)
{
    Row(modifier = Modifier.fillMaxWidth()) {
        week.forEach { day ->
            DayComposable(
                modifier = Modifier.weight(1f),
                day = day.day,
                isAvailable = day.isAvailable,
                isSelected = day.day == selectedDay && day.relation == selectedRelation,
                isNextMonth = day.relation != MonthRelation.CURRENT
            )
        }
    }
}
