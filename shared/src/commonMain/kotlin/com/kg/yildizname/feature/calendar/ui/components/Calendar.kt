package com.kg.yildizname.feature.calendar.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kg.yildizname.core.ui.theme.YzGold
import compose.icons.FeatherIcons
import compose.icons.feathericons.ChevronLeft
import compose.icons.feathericons.ChevronRight
import compose.icons.feathericons.ChevronsRight
import horoscope.shared.generated.resources.Res
import horoscope.shared.generated.resources.next_month
import horoscope.shared.generated.resources.previous_month
import kotlinx.datetime.LocalDate
import kotlinx.datetime.isoDayNumber

@Composable
fun Calendar(
    date: LocalDate,
    luckDays: List<Int> = mutableListOf<Int>(),
    onNextMonth: () -> Unit,
    onPreviousMonth: () -> Unit
)
{
    /**
     * < Month Year >
     *
     *  Days: Pzt Sa Ça Prş and english counterintiatives
     *  Each day
     *
     *  The lucky days bullet is shiny
     */
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)
        ) {

            Spacer(Modifier.height(48.dp))
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

            Spacer(Modifier.height(8.dp))
            // Days
            Row {  }

            Spacer(Modifier.height(8.dp))



    }
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

/**
 *  26 27 28 29 30 1 2
 *  3 4   5   6  7  8  9
 *  10                 16
 *  17                  23
 *  24                  30
 */