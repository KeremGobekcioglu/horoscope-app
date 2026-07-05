package com.kg.yildizname.feature.calendar.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kg.yildizname.core.ui.theme.YzBorder
import com.kg.yildizname.core.ui.theme.YzCardBg
import com.kg.yildizname.core.ui.theme.YzGold
import com.kg.yildizname.core.ui.theme.YzOnSurface
import horoscope.shared.generated.resources.Res
import horoscope.shared.generated.resources.this_month
import org.jetbrains.compose.resources.stringResource

@Composable
fun MonthlyReadingCard(
    monthlyComment: String
)
{
    val shape = RoundedCornerShape(20.dp)
    val sentences  = remember(monthlyComment)
    {
        monthlyComment.split(".").map { it.trim() }.filter { it.isNotEmpty() }
    }

    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit)
    {
        visible  = true
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(YzCardBg.copy(0.6f))
            //.border(0.5.dp,YzBorder,shape)
            .padding(20.dp)
    )
    {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(Res.string.this_month),
                style = MaterialTheme.typography.headlineSmall,
                color = YzGold)

            Text(
                text = monthlyComment,
                style = MaterialTheme.typography.bodyLarge,
                color = YzOnSurface
            )

        }
    }
}