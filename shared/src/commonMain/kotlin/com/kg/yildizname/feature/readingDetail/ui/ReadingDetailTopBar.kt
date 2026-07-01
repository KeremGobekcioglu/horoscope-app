package com.kg.yildizname.feature.readingDetail.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import com.kg.yildizname.core.ui.utils.yzStatusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kg.yildizname.core.ui.theme.YzInk
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowLeft
import compose.icons.feathericons.Share2
import horoscope.shared.generated.resources.Res
import horoscope.shared.generated.resources.cd_back
import horoscope.shared.generated.resources.cd_share
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ReadingDetailTopBar(
    signName: String,
    periodLabel: String,
    onBackClick: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .yzStatusBarsPadding()
            .padding(horizontal = 4.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = FeatherIcons.ArrowLeft,
                contentDescription = stringResource(Res.string.cd_back),
                tint = YzInk,
            )
        }

        Text(
            text = "$signName — $periodLabel",
            color = YzInk,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f),
        )

        IconButton(onClick = onShareClick) {
            Icon(
                imageVector = FeatherIcons.Share2,
                contentDescription = stringResource(Res.string.cd_share),
                tint = YzInk,
            )
        }
    }
}
