package com.kg.yildizname.feature.compatability.ui.CompatibilityDetailedResult

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kg.yildizname.core.ui.theme.YzBg
import com.kg.yildizname.core.ui.theme.YzBgLight
import com.kg.yildizname.core.ui.theme.YzCardBgAlt
import com.kg.yildizname.core.ui.theme.YzCardBgAltSecond
import com.kg.yildizname.core.ui.theme.YzGold
import com.kg.yildizname.core.ui.theme.YzInk
import com.kg.yildizname.feature.compatability.ui.components.Scores
import horoscope.shared.generated.resources.Res
import horoscope.shared.generated.resources.compat_category_points
import horoscope.shared.generated.resources.compat_score_communication
import horoscope.shared.generated.resources.compat_score_friendship
import horoscope.shared.generated.resources.compat_score_long_term
import horoscope.shared.generated.resources.compat_score_love
import org.jetbrains.compose.resources.stringResource

@Composable
fun ScoreCard(
    loveScore: Int,
    communicationScore: Int,
    friendshipScore: Int,
    longTermScore: Int,
) {
    Box(modifier = Modifier
        .padding(horizontal = 12.dp)
        .clip(RoundedCornerShape(16.dp))
        .background(YzBgLight.copy(0.65f))
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp).fillMaxWidth()
        ) {
            Text(
                text = stringResource(Res.string.compat_category_points),
                color = YzGold,
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(Modifier.height(4.dp))
            Scores(
                loveScore,
                stringResource(Res.string.compat_score_love),
                barColor = YzGold,
                scoreTextColor = YzGold
                , textColor = YzInk
                , textSize = 13.sp
            )
            Scores(
                communicationScore,
                stringResource(Res.string.compat_score_communication),
                barColor = YzGold,
                scoreTextColor = YzGold
                , textColor = YzInk
                , textSize = 13.sp
            )
            Scores(
                friendshipScore,
                stringResource(Res.string.compat_score_friendship),
                barColor = YzGold,
                scoreTextColor = YzGold
                , textColor = YzInk
                , textSize = 13.sp
            )
            Scores(
                longTermScore,
                stringResource(Res.string.compat_score_long_term),
                barColor = YzGold,
                scoreTextColor = YzGold
                , textColor = YzInk
                , textSize = 13.sp
            )

        }
    }
}