package com.kg.yildizname.feature.settings.ui

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.kg.yildizname.core.data.model.ZodiacSign
import com.kg.yildizname.core.data.model.compatGridIcon
import com.kg.yildizname.core.data.model.localizedDateRange
import com.kg.yildizname.core.data.model.localizedName
import com.kg.yildizname.core.ui.components.StarFieldBackground
import com.kg.yildizname.core.ui.theme.YzBorder
import com.kg.yildizname.core.ui.theme.YzGold
import com.kg.yildizname.core.ui.theme.YzInk
import com.kg.yildizname.core.ui.theme.YzMuted
import com.kg.yildizname.core.ui.theme.YzSurface
import com.kg.yildizname.core.ui.theme.YzTypography // ASSUMPTION: confirm real object/accessor name
import com.kg.yildizname.core.ui.utils.yzStatusBarsPadding
import compose.icons.feathericons.ChevronRight
import horoscope.shared.generated.resources.Res
import horoscope.shared.generated.resources.settings_about
import horoscope.shared.generated.resources.settings_daily_notification
import horoscope.shared.generated.resources.settings_language
import horoscope.shared.generated.resources.settings_language_en
import horoscope.shared.generated.resources.settings_language_tr
import horoscope.shared.generated.resources.settings_notification_time
import horoscope.shared.generated.resources.settings_notifications
import horoscope.shared.generated.resources.settings_privacy_policy
import horoscope.shared.generated.resources.settings_reset_data
import horoscope.shared.generated.resources.settings_share_app
import horoscope.shared.generated.resources.settings_title
import horoscope.shared.generated.resources.settings_version
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun SettingsScreen(
    currentSign: ZodiacSign,
    notificationsEnabled: Boolean,
    notificationTime: String,
    currentLanguage: String,
    appVersion: String,
    onChangeSignClick: () -> Unit,
    onNotificationsEnabledChange: (Boolean) -> Unit,
    onTimeClick: () -> Unit,
    onLanguageChange: (String) -> Unit,
    onPrivacyPolicyClick: () -> Unit,
    onShareAppClick: () -> Unit,
    onResetDataClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val YzError = Color.Red
    StarFieldBackground(modifier = Modifier.fillMaxSize())
    Box(
        modifier = modifier.fillMaxSize().yzStatusBarsPadding(),
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(bottom = 24.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(Modifier.height(16.dp))

            // Title
            Text(
                text = stringResource(Res.string.settings_title),
                color = YzGold,
                style = YzTypography.headlineLarge
            )

            Spacer(Modifier.height(20.dp))

            // Sign row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.small)
                    .background(YzSurface)
                    .clickable(onClick = onChangeSignClick)
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val iconPainter: Painter = painterResource(currentSign.compatGridIcon)
                    Icon(
                        painter = iconPainter,
                        contentDescription = null,
                        modifier = Modifier.size(44.dp),
                        tint = YzGold
                    )
                    Spacer(Modifier.width(16.dp))
                    Column {
                        Text(
                            text = currentSign.localizedName(),
                            color = YzInk,
                            style = YzTypography.bodyLarge
                        )
                        Text(
                            text = currentSign.localizedDateRange(),
                            color = YzMuted,
                            style = YzTypography.bodySmall
                        )
                    }
                }
                Icon(
                    imageVector = compose.icons.FeatherIcons.ChevronRight,
                    contentDescription = null,
                    tint = YzMuted,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(Modifier.height(20.dp))

            // Notifications section
            Text(
                text = stringResource(Res.string.settings_notifications),
                color = YzInk,
                style = YzTypography.labelMedium
            )
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.small)
                    .background(YzSurface)
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(Res.string.settings_daily_notification),
                    color = YzInk,
                    style = YzTypography.bodyLarge
                )
                Switch(
                    checked = notificationsEnabled,
                    onCheckedChange = onNotificationsEnabledChange,
                    colors = SwitchDefaults.colors(
                        checkedTrackColor = YzGold,
                        checkedThumbColor = YzSurface
                    )
                )
            }

            if (notificationsEnabled) {
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.small)
                        .background(YzSurface)
                        .clickable(onClick = onTimeClick)
                        .padding(horizontal = 12.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(Res.string.settings_notification_time),
                        color = YzMuted,
                        style = YzTypography.bodyLarge
                    )
                    Text(
                        text = notificationTime,
                        color = YzInk,
                        style = YzTypography.bodyLarge
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // Language
            Text(
                text = stringResource(Res.string.settings_language),
                color = YzInk,
                style = YzTypography.labelMedium
            )
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.small)
                    .background(YzSurface)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val trSelected = currentLanguage == "tr"
                val enSelected = currentLanguage == "en"
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(MaterialTheme.shapes.small)
                        .background(if (trSelected) YzGold else YzSurface)
                        .clickable { onLanguageChange("tr") }
                        .padding(vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(Res.string.settings_language_tr),
                        color = if (trSelected) YzSurface else YzInk,
                        style = YzTypography.labelMedium
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(MaterialTheme.shapes.small)
                        .background(if (enSelected) YzGold else YzSurface)
                        .clickable { onLanguageChange("en") }
                        .padding(vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(Res.string.settings_language_en),
                        color = if (enSelected) YzSurface else YzInk,
                        style = YzTypography.labelMedium
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // About
            Text(
                text = stringResource(Res.string.settings_about),
                color = YzInk,
                style = YzTypography.labelMedium
            )
            Spacer(Modifier.height(8.dp))   
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.small)
                    .background(YzSurface)
                    .padding(vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(Res.string.settings_version),
                        color = YzMuted,
                        style = YzTypography.bodyLarge
                    )
                    Text(text = appVersion, color = YzInk, style = YzTypography.bodyLarge)
                }
                HorizontalDivider(color = YzBorder)
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .clickable(onClick = onPrivacyPolicyClick)
                        .padding(horizontal = 12.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.settings_privacy_policy),
                        color = YzInk,
                        style = YzTypography.bodyLarge
                    )
                }
                HorizontalDivider(color = YzBorder)
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .clickable(onClick = onShareAppClick)
                        .padding(horizontal = 12.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.settings_share_app),
                        color = YzInk,
                        style = YzTypography.bodyLarge
                    )
                }
            }

            Spacer(Modifier.height(28.dp))

            HorizontalDivider(color = YzBorder)
            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onResetDataClick)
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(Res.string.settings_reset_data),
                    color = YzError,
                    style = YzTypography.bodyLarge
                )
            }

            Spacer(Modifier.height(96.dp)) // leave room for bottom nav
        }
    }
}