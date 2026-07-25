package com.kg.yildizname.feature.settings.ui.components


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kg.yildizname.core.ui.theme.YzBorder
import com.kg.yildizname.core.ui.theme.YzMuted
import com.kg.yildizname.core.ui.theme.YzSurfaceAlt

/**
 * Uppercase muted section label that sits above a [SettingsCard].
 */
@Composable
fun SettingsSectionLabel(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        color = YzMuted,
        // NOTE: swap for a YzTypography label token if one exists; MaterialTheme
        // isn't used here so the caller controls style via the theme wrapper.
        modifier = modifier.padding(start = 4.dp, bottom = 8.dp),
    )
}

/**
 * Bordered surface card. Lifts content off the page with YzSurfaceAlt fill
 * plus a 1px border — the border is what provides the separation the flat
 * surface-color difference alone can't on this dark a background.
 *
 * Place [SettingsRow]s inside, separated by [SettingsRowDivider] for the
 * inset-hairline grouped-list look.
 */
@Composable
fun SettingsCard(
    modifier: Modifier = Modifier,
    borderColor: Color = YzBorder,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(YzSurfaceAlt)
            .border(1.dp, borderColor, RoundedCornerShape(20.dp)),
        content = content,
    )
}

/**
 * Inset hairline divider between rows inside a [SettingsCard].
 * Inset from the left so it aligns past a leading icon, iOS-style.
 */
@Composable
fun SettingsRowDivider(
    modifier: Modifier = Modifier,
    startInset: androidx.compose.ui.unit.Dp = 48.dp,
) {
    androidx.compose.foundation.layout.Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = startInset)
            .height(1.dp)
            .background(YzBorder),
    )
}