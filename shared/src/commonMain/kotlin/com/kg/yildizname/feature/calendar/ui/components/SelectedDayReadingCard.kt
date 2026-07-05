package com.kg.yildizname.feature.calendar.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kg.yildizname.core.data.model.Reading
import com.kg.yildizname.core.ui.theme.YzBorder
import com.kg.yildizname.core.ui.theme.YzCardBg
import com.kg.yildizname.core.ui.theme.YzGold
import com.kg.yildizname.core.ui.theme.YzMuted
import com.kg.yildizname.core.ui.theme.YzOnSurface
import com.kg.yildizname.core.ui.theme.YzPickerBg
import com.kg.yildizname.core.ui.theme.YzSurface
import compose.icons.FeatherIcons
import compose.icons.feathericons.Activity
import compose.icons.feathericons.Briefcase
import compose.icons.feathericons.Heart
import compose.icons.feathericons.Star
import horoscope.shared.generated.resources.Res
import horoscope.shared.generated.resources.home_score_health
import horoscope.shared.generated.resources.home_score_love
import horoscope.shared.generated.resources.home_score_luck
import horoscope.shared.generated.resources.home_score_work
import horoscope.shared.generated.resources.read_full
import horoscope.shared.generated.resources.this_month
import org.jetbrains.compose.resources.stringResource

/*
* Selected date , small star right. probably favorites.
* 4 card : love work health luck and scores side.
* daily reading but not whole ...
* a text which leads to reading detail.
* cross navigation , needed to check.
*
* */
// can pass Reading directly maybe.
@Composable
fun SelectedDailyReadingCard(
    scoreLove: Int,
    scoreWork: Int,
    scoreHealth: Int,
    scoreLuck: Int,
    dailyComment: String,
    toReadingDetail: () -> Unit,
    date: String
)
{
    val shape = RoundedCornerShape(20.dp)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(YzCardBg)
            .border(0.5.dp,YzBorder,shape)
            .padding(20.dp)
    )
    {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        )
        {
            Text(
                text = date,
                color = YzGold,
                fontSize = 24.sp
            )
            /*
            * Label grids
            * */
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    ScoreCard(score = scoreLove, field = stringResource(Res.string.home_score_love), icon = FeatherIcons.Heart , modifier = Modifier.weight(1f))
                    ScoreCard(score = scoreWork, field = stringResource(Res.string.home_score_work), icon = FeatherIcons.Briefcase, modifier = Modifier.weight(1f))
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    ScoreCard(score = scoreHealth, field = stringResource(Res.string.home_score_health), icon = FeatherIcons.Activity , modifier = Modifier.weight(1f))
                    ScoreCard(score = scoreLuck, field = stringResource(Res.string.home_score_luck), icon = FeatherIcons.Star , modifier = Modifier.weight(1f))
                }
            }
            Text(
                text = dailyComment,
                color = YzOnSurface,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )
            Row(modifier = Modifier.clickable(true, onClick = { toReadingDetail() })) {
                Text(
                    text = stringResource(Res.string.read_full),
                    color = YzGold.copy(0.7f)
                )
            }

        }
    }
}

@Composable
fun ScoreCard(score: Int, field: String, icon: ImageVector, modifier: Modifier = Modifier)
{
    val shape = RoundedCornerShape(12.dp)
    Box(
        modifier = modifier
            .clip(shape)
            .background(YzSurface)
            .border(0.5.dp, YzBorder, shape)
            .padding(16.dp,14.dp)
    )
    {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        )
        {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            )
            {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = YzMuted,
                    modifier = Modifier.size(12.dp)
                )
                Text(
                    text = field,
                    color = YzMuted,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Text(
                text = "$score/10",
                color = YzGold,
                fontSize = 12.sp,
                letterSpacing = 0.5.sp,
                fontWeight = FontWeight.SemiBold,
                )
        }
    }
}