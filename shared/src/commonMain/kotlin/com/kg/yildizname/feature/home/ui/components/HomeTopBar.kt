package com.kg.yildizname.feature.home.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import com.kg.yildizname.core.ui.utils.yzStatusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kg.yildizname.core.ui.theme.YzBorder
import com.kg.yildizname.core.ui.theme.YzInk
import com.kg.yildizname.core.ui.theme.YzSurface
import com.kg.yildizname.core.util.yzUppercase
import compose.icons.FeatherIcons
import compose.icons.feathericons.Bell
import compose.icons.feathericons.Image
import horoscope.shared.generated.resources.Res
import horoscope.shared.generated.resources.home_notification_cd
import horoscope.shared.generated.resources.home_share_card_cd
import org.jetbrains.compose.resources.stringResource
import androidx.compose.foundation.layout.Box

@Composable
internal fun HomeTopBar(
    dateLabel: String,
    onNotificationClick: () -> Unit,
    onShareCardClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .yzStatusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Date pill
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(999.dp))
                .background(YzSurface)          // #0F1428
                .border(0.5.dp, YzBorder, RoundedCornerShape(999.dp))
                .padding(horizontal = 14.dp, vertical = 7.dp)
        ) {
            Text(
                text = dateLabel.yzUppercase(),
                color = YzInk,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 1.2.sp
            )
        }

        // Right icons
//        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
//            TopBarIconButton(
//                contentDescription = stringResource(Res.string.home_notification_cd),
//                onClick = onNotificationClick,
//                icon = FeatherIcons.Bell,
//            )
//            TopBarIconButton(
//                contentDescription = stringResource(Res.string.home_share_card_cd),
//                onClick = onShareCardClick,
//                icon = FeatherIcons.Image,
//            )
//        }
    }
}

@Composable
private fun TopBarIconButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(38.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(YzSurface)
            .border(0.5.dp, YzBorder, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = YzInk,
            modifier = Modifier.size(18.dp)
        )
    }
}
