package com.kg.yildizname.feature.readingDetail.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kg.yildizname.core.ui.theme.ChipShape
import com.kg.yildizname.core.ui.theme.YzBorder
import com.kg.yildizname.core.ui.theme.YzGold
import com.kg.yildizname.core.ui.theme.YzMuted
import com.kg.yildizname.core.ui.theme.YzSurfaceAlt
import horoscope.shared.generated.resources.Res
import horoscope.shared.generated.resources.reading_detail_lucky_color_label
import horoscope.shared.generated.resources.reading_detail_lucky_number_label
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun LuckChipsRow(
    luckyNumber: Int,
    luckyColorName: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        LuckChip(
            label = stringResource(Res.string.reading_detail_lucky_number_label),
            value = luckyNumber.toString(),
        )
        Spacer(Modifier.width(12.dp))
        LuckChip(
            label = stringResource(Res.string.reading_detail_lucky_color_label),
            value = luckyColorName,
        )
    }
}

@Composable
private fun LuckChip(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .clip(ChipShape)
            .background(YzSurfaceAlt)
            .border(1.dp, YzBorder, ChipShape)
            .padding(horizontal = 14.dp, vertical = 8.dp),
    ) {
        Text(
            text = "$label: ",
            color = YzMuted,
            fontSize = 13.sp,
        )
        Text(
            text = value,
            color = YzGold,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}
