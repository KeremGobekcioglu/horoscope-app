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
                    CalendarRow(week = week , selectedDay = 30)
                }
            }
    }
}

data class CalendarWeek(
    val start: Int,
    val end: Int
)

data class CalendarDay(
    val day: Int,
    val currentMonth: Boolean,
    val isAvailable: Boolean = false
)

@Composable
fun DayComposable(modifier: Modifier = Modifier, isLuckyDay: Boolean = false, isSelected: Boolean = false, day: Int, isNextMonth: Boolean, isAvailable: Boolean)
{
    // if selected shine. if luck day , shine. if selected text bold.
    // if day belongs to next month, no dot below it and it barely visible.
    /**
     * day number
     * small dot
     * rectangle
     */
    val shape = RoundedCornerShape(16.dp)
    Box(
        modifier = modifier.size(48.dp)
            .then(
                if((isSelected || isLuckyDay ) && !isNextMonth)
                {
                    Modifier.shadow(
                        elevation = 0.dp,
                        shape = shape,
                        ambientColor = YzGold,
                        spotColor = YzGold
                    )
                }
                else
                {
                    Modifier
                }
            )
            .clip(shape)
//            .background(Color.Transparent)
            .background(if((isSelected || isLuckyDay ) && !isNextMonth) YzBg.copy(0.1f) else Color.Transparent)
            .then(
                if((isSelected || isLuckyDay ) && !isNextMonth)
                {
                    Modifier.border(
                        width = 2.dp,
                        color = YzGold,
                        shape = shape
                    )
                }
                else Modifier
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
                fontWeight = if(!isSelected || isNextMonth) FontWeight.Normal else FontWeight.Bold
            )
//            Text(
//                text = "$isAvailable",
//                color = if(isAvailable) YzGold else if(!isNextMonth) YzGold.copy(0.5f) else Color.LightGray.copy(0.4f),
//                fontSize = 20.sp,
//                fontWeight = if(!isSelected || isNextMonth) FontWeight.Normal else FontWeight.Bold
//            )

                Box(
                    modifier = Modifier.size(4.dp)
                        .clip(CircleShape)
                        .background(
                            if(isNextMonth)  Color.Transparent else if (isSelected) YzGold else YzGold.copy(0.5f)
                        )
                )

        }
    }
}


@Composable
fun CalendarRow(luckDays: List<Int> = mutableListOf() , week: List<CalendarDay> , selectedDay:Int)
{
    Row(modifier = Modifier.fillMaxWidth()) {
        week.forEach {
            day ->
                DayComposable(modifier = Modifier.weight(1f), day = day.day , isAvailable = day.isAvailable, isSelected = day.day == selectedDay , isNextMonth = !(day.currentMonth))
        }
    }
}

fun calculateCalendar(date: LocalDate) : List<List<CalendarDay>>
{
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
    val previousMonth = today.previousMonth()
    val isCurrentMonth = date.year == today.year && date.month == today.month
    val isPreviousMonth = date.year == previousMonth.year && date.month == previousMonth.month
    val firstOfTheMonth = LocalDate(year = date.year,
        month = date.month,
        day = 1
    )

    val previousMonthDayCount = date.previousMonth().daysInMonth()
    val daysInMonth = date.daysInMonth()
    val leading: Int = leadingOffset(firstOfTheMonth)
    val trailing: Int = trailingOffset(leading,daysInMonth)
    val calendarDays = mutableListOf<CalendarDay>()
    val firstPreviousDay = previousMonthDayCount - leading + 1

    // from firstPreviousDay we ll start adding numbers until how many day of its that month - for example if firstPrevDay is 28 , and prev month got 31 day
    // 28 29 30 31 should be added then we ll complete it to seven.
    for(day in firstPreviousDay..previousMonthDayCount)
    {
        calendarDays.add(CalendarDay(day = day, currentMonth = false , isAvailable = true))
    }
    for(day in 1..daysInMonth)
    {
        val isAvailable = if(isCurrentMonth) day <= today.day else isPreviousMonth
        calendarDays.add(CalendarDay(day = day, currentMonth = true , isAvailable = isAvailable))
    }
    for(day in 1..trailing)
    {
        calendarDays.add(CalendarDay(day = day, currentMonth = false))
    }
    return calendarDays.chunked(7)
}

fun leadingOffset(firstOfTheMonth: LocalDate) : Int
{
        val isoDay = firstOfTheMonth.dayOfWeek.isoDayNumber
        val offset = isoDay - 1
        return offset
}

fun trailingOffset(leadingOffset: Int, daysInMonth: Int) : Int
{
    val usedCells = leadingOffset + daysInMonth
    val trailing = (7 - (usedCells % 7)) % 7
    return trailing
}

fun LocalDate.daysInMonth(): Int = when (month) {
    Month.JANUARY -> 31
    Month.FEBRUARY -> if (isLeapYear()) 29 else 28
    Month.MARCH -> 31
    Month.APRIL -> 30
    Month.MAY -> 31
    Month.JUNE -> 30
    Month.JULY -> 31
    Month.AUGUST -> 31
    Month.SEPTEMBER -> 30
    Month.OCTOBER -> 31
    Month.NOVEMBER -> 30
    Month.DECEMBER -> 31
}
fun LocalDate.isLeapYear(): Boolean {
    val year = this.year
    return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
}

fun LocalDate.previousMonth() : LocalDate {
    return (if(month == Month.JANUARY)
    {
        LocalDate(
            year = year - 1,
            month = Month.DECEMBER,
            day = 1
        )
    }
    else
    {
        val prevMonth = Month.entries[month.ordinal - 1]
        LocalDate(
            year = year,
            month = prevMonth,
            day = 1
        )
    })
}

fun LocalDate.nextMonth() : LocalDate {
    return (if(month == Month.DECEMBER)
    {
        LocalDate(
            year = year + 1,
            month = Month.JANUARY,
            day = 1
        )
    }
    else
    {
        val prevMonth = Month.entries[month.ordinal + 1]
        LocalDate(
            year = year,
            month = prevMonth,
            day = 1
        )
    })
}
/**
 *  26 27 28 29 30 1 2
 *  3 4   5   6  7  8  9
 *  10                 16
 *  17                  23
 *  24                  30
 */