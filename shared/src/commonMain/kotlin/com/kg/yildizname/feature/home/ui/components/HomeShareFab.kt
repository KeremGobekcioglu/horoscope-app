package com.kg.yildizname.feature.home.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import compose.icons.FeatherIcons
import compose.icons.feathericons.Share2
import horoscope.shared.generated.resources.Res
import horoscope.shared.generated.resources.home_share_cd
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun HomeShareFab(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(52.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFD4A843),   // warm gold top-left
                        Color(0xFFA07830)    // deeper gold bottom-right
                    )
                )
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = FeatherIcons.Share2,
            contentDescription = stringResource(Res.string.home_share_cd),
            tint = Color(0xFF1A1000),    // near-black on gold — better contrast than white
            modifier = Modifier.size(22.dp)
        )
    }
}
