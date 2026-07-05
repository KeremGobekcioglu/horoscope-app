package com.kg.yildizname.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kg.yildizname.core.ui.theme.YzBg
import com.kg.yildizname.core.ui.theme.YzTheme
import com.kg.yildizname.feature.calendar.ui.components.ScoreCard
import com.kg.yildizname.feature.calendar.ui.components.SelectedDailyReadingCard
import compose.icons.FeatherIcons
import compose.icons.feathericons.Activity
import compose.icons.feathericons.Briefcase
import compose.icons.feathericons.Heart
import compose.icons.feathericons.Star

@Preview(name = "ScoreCard", showBackground = true)
@Composable
private fun ScoreCardPreview() {
    YzTheme {
        Column(
            modifier = Modifier
                .background(YzBg)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ScoreCard(score = 8, field = "AŞK", icon = FeatherIcons.Heart , modifier = Modifier.weight(1f))
                ScoreCard(score = 6, field = "İŞ", icon = FeatherIcons.Briefcase, modifier = Modifier.weight(1f))
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                ScoreCard(score = 7, field = "SAĞLIK", icon = FeatherIcons.Activity , modifier = Modifier.weight(1f))
                ScoreCard(score = 9, field = "ŞANS", icon = FeatherIcons.Star , modifier = Modifier.weight(1f))
            }
        }
    }
}

@Preview(name = "SelectedDailyReadingCard", showBackground = true)
@Composable
private fun SelectedDailyReadingCardPreview() {
    YzTheme {
        Column(modifier = Modifier.background(YzBg)) {
            SelectedDailyReadingCard(
                scoreLove = 8,
                scoreWork = 6,
                scoreHealth = 7,
                scoreLuck = 9,
                dailyComment = "Bugün gezegenler senin lehine hizalanıyor. Sezgilerine güven, " +
                    "özellikle ilişkilerinde sabırlı olman gereken bir konu gündeme gelebilir." + "Bugün gezegenler senin lehine hizalanıyor. Sezgilerine güven, " +
                        "özellikle ilişkilerinde sabırlı olman gereken bir konu gündeme gelebilir.",
                toReadingDetail = {},
                date = "5 Temmuz"
            )
        }
    }
}