package com.kg.yildizname.feature.calendar.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kg.yildizname.core.ui.theme.YzBg
import com.kg.yildizname.core.ui.theme.YzGold
import com.kg.yildizname.core.ui.theme.YzOnSurface
import com.kg.yildizname.core.ui.utils.DateFormatter
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
    luckDays: List<Int> = emptyList(),
    selectedDay: CalendarDay?,
    onNextMonth: () -> Unit,
    onPreviousMonth: () -> Unit,
    onDaySelected: (CalendarDay) -> Unit,
    canGoToPreviousMonth: Boolean,   // new param

)
{
    val days : List<String> = DateFormatter.weekdayAbbreviations()
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
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // < Month Year >
        Row(modifier = Modifier.fillMaxWidth(),
            Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { onPreviousMonth() }, enabled = canGoToPreviousMonth) {
                Icon(
                    imageVector = FeatherIcons.ChevronLeft,
                    contentDescription = Res.string.previous_month.toString(),
                    tint = YzGold,
                    modifier = Modifier.alpha(if (canGoToPreviousMonth) 1f else 0f)
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
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,

        ) {
            days.forEach {
                    day ->
                Text(
                    text = day,
                    color = YzGold.copy(0.8f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        val rowSpacing = 4.dp

        // No weight here: this should wrap its own natural (width-derived) content height, not
        // stretch to fill whatever's left in the parent Column. The maxHeight BoxWithConstraints
        // reports is still a real, finite ceiling (inherited from the screen), so the min() below
        // remains a safety net for the rare case content would otherwise overflow it.
        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            val totalRowSpacing = rowSpacing * (weeks.size - 1)
            val cellSize = minOf(
                maxWidth / 7,
                (maxHeight - totalRowSpacing) / weeks.size,
            )

            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(rowSpacing)) {
                weeks.forEach {
                        week ->
                    CalendarRow(
                        week = week,
                        cellSize = cellSize,
                        selectedDay = selectedDay?.day ?: -1,
                        selectedRelation = selectedDay?.relation ?: MonthRelation.CURRENT,
                        onDayClick = onDaySelected,
                        )
                }
            }
        }

//        SelectedDailyReadingCard()
//        MonthlyReadingCard()
    }
}

@Composable
fun DayComposable(
    modifier: Modifier = Modifier, isLuckyDay: Boolean = false,
    isSelected: Boolean = false, day: Int, isNextMonth: Boolean, isAvailable: Boolean,
    onClick: () -> Unit
    )
{
    val canShine = (isSelected || isLuckyDay) && isAvailable
    val shape = RoundedCornerShape(16.dp)
    println("day = $day and avavilable info = $isAvailable")
    Box(
        modifier = modifier
            .clickable(isAvailable, onClick = onClick)
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
fun CalendarRow(
    modifier: Modifier = Modifier,
    week: List<CalendarDay>,
    cellSize: Dp,
    selectedDay: Int, selectedRelation: MonthRelation,
    onDayClick: (CalendarDay) -> Unit
    )
{
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        week.forEach { day ->
            DayComposable(
                modifier = Modifier.size(cellSize),
                day = day.day,
                isAvailable = day.isAvailable,
                isSelected = day.day == selectedDay && day.relation == selectedRelation,
                isNextMonth = day.relation != MonthRelation.CURRENT,
                onClick = { onDayClick(day) }
            )
        }
    }
}
