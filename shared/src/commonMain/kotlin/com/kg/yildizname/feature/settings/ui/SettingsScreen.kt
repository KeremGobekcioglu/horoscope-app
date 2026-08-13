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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import compose.icons.FeatherIcons
import compose.icons.feathericons.Bell
import compose.icons.feathericons.ChevronRight
import compose.icons.feathericons.Clock
import compose.icons.feathericons.Info
import compose.icons.feathericons.Share2
import compose.icons.feathericons.Shield
import compose.icons.feathericons.Trash2
import com.kg.yildizname.core.data.model.ZodiacSign
import com.kg.yildizname.core.data.model.compatGridIcon
import com.kg.yildizname.core.data.model.localizedDateRange
import com.kg.yildizname.core.data.model.localizedName
import com.kg.yildizname.core.ui.components.SelectSignBottomSheet
import com.kg.yildizname.core.ui.components.StarFieldBackground
import com.kg.yildizname.core.ui.theme.YzBorder
import com.kg.yildizname.core.ui.theme.YzError
import com.kg.yildizname.core.ui.theme.YzErrorBorder
import com.kg.yildizname.core.ui.theme.YzGold
import com.kg.yildizname.core.ui.theme.YzInk
import com.kg.yildizname.core.ui.theme.YzMuted
import com.kg.yildizname.core.ui.theme.YzSurface
import com.kg.yildizname.core.ui.theme.YzSurfaceAlt
import com.kg.yildizname.core.ui.utils.yzStatusBarsPadding
import com.kg.yildizname.feature.settings.ui.components.SettingsCard
import com.kg.yildizname.feature.settings.ui.components.SettingsRow
import com.kg.yildizname.feature.settings.ui.components.SettingsRowDivider
import com.kg.yildizname.feature.settings.ui.components.SettingsSectionLabel
import com.kg.yildizname.feature.share.ui.CaptureHost
import com.kg.yildizname.feature.share.ui.ShareCard
import com.kg.yildizname.feature.share.ui.ShareCardExportDensity
import com.kg.yildizname.platform.ShareManager
import com.kg.yildizname.platform.ShareResult
import com.kg.yildizname.platform.toPngBytes
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
import horoscope.shared.generated.resources.settings_reset_dialog_cancel
import horoscope.shared.generated.resources.settings_reset_dialog_confirm
import horoscope.shared.generated.resources.settings_reset_dialog_message
import horoscope.shared.generated.resources.settings_reset_dialog_title
import horoscope.shared.generated.resources.settings_restart_message
import horoscope.shared.generated.resources.settings_restart_ok
import horoscope.shared.generated.resources.settings_restart_title
import horoscope.shared.generated.resources.settings_share_app
import horoscope.shared.generated.resources.settings_title
import horoscope.shared.generated.resources.settings_version
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun SettingsScreen(
    notificationsEnabled: Boolean,
    notificationTime: String,
    currentLanguage: String,
    appVersion: String,
    onChangeSignClick: (ZodiacSign) -> Unit,
    onNotificationSwitchTapped: () -> Unit,
    onTimeClick: () -> Unit,
    onLanguageChange: (String) -> Unit,
    onPrivacyPolicyClick: () -> Unit,
    onShareAppClick: () -> Unit,
    onResetDataClick: () -> Unit,
    onDismissRestartDialog: () -> Unit,
    onDismissResetDialog: () -> Unit,
    onConfirmResetClick: () -> Unit,
    onErrorShown: () -> Unit,
    modifier: Modifier = Modifier,
    refreshNotificationStatus: () -> Unit,
    state: SettingsState
) {

    var showSignSheet by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.error) {
        val error = state.error
        if (error != null) {
            snackbarHostState.showSnackbar(error)
            onErrorShown()
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner)
    {
        val observer = LifecycleEventObserver {
            _, event ->
            if(event == Lifecycle.Event.ON_RESUME)
            {
                refreshNotificationStatus()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

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
                .padding(bottom = 16.dp), // room for bottom nav
        ) {
            Spacer(Modifier.height(20.dp))

            Text(
                text = stringResource(Res.string.settings_title),
                color = YzGold,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
                // NOTE: apply your YzTypography display/headline token via the theme
                // wrapper if you have one — kept token-free here to avoid guessing the API.
            )

            Spacer(Modifier.height(22.dp))

            // ---------- Sign (profile anchor) ----------
            SettingsCard {
                val sign = state.sign
                val signPainter: Painter = painterResource(sign.compatGridIcon)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = { showSignSheet = true })
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(999.dp))
                                .background(YzSurface),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                painter = signPainter,
                                contentDescription = null,
                                tint = YzGold,
                                modifier = Modifier.size(24.dp),
                            )
                        }
                        Spacer(Modifier.width(14.dp))
                        Column {
                            Text(text = sign.localizedName(), color = YzInk)
                            Text(text = sign.localizedDateRange(), color = YzMuted)
                        }
                    }
                    Icon(
                        imageVector = FeatherIcons.ChevronRight,
                        contentDescription = null,
                        tint = YzMuted,
                        modifier = Modifier.size(18.dp),
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // ---------- Notifications ----------
            SettingsSectionLabel(text = stringResource(Res.string.settings_notifications))
            SettingsCard {
                SettingsRow(
                    title = stringResource(Res.string.settings_daily_notification),
                    leadingIcon = FeatherIcons.Bell,
                    trailing = {
                        Switch(
                            checked = state.notificationsEnabled ?: false,
                            onCheckedChange = { onNotificationSwitchTapped() },
                            colors = SwitchDefaults.colors(
                                checkedTrackColor = YzGold,
                                checkedThumbColor = YzSurface,
                            ),
                        )
                    },
                )
                if (notificationsEnabled) {
                    SettingsRowDivider()
                    SettingsRow(
                        title = stringResource(Res.string.settings_notification_time),
                        leadingIcon = FeatherIcons.Clock,
                        onClick = onTimeClick,
                        trailing = { Text(text = notificationTime, color = YzGold) },
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // ---------- Language ----------
            SettingsSectionLabel(text = stringResource(Res.string.settings_language))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(YzSurfaceAlt)
                    .padding(6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                LanguageOption(
                    label = stringResource(Res.string.settings_language_tr),
                    selected = currentLanguage == "tr",
                    modifier = Modifier.weight(1f),
                    onClick = { onLanguageChange("tr") },
                )
                LanguageOption(
                    label = stringResource(Res.string.settings_language_en),
                    selected = currentLanguage == "en",
                    modifier = Modifier.weight(1f),
                    onClick = { onLanguageChange("en") },
                )
            }

            Spacer(Modifier.height(24.dp))

            // ---------- About ----------
            SettingsSectionLabel(text = stringResource(Res.string.settings_about))
            SettingsCard {
                SettingsRow(
                    title = stringResource(Res.string.settings_version),
                    leadingIcon = FeatherIcons.Info,
                    iconTint = YzMuted,
                    trailing = { Text(text = appVersion, color = YzMuted) },
                )
                SettingsRowDivider()
                SettingsRow(
                    title = stringResource(Res.string.settings_privacy_policy),
                    leadingIcon = FeatherIcons.Shield,
                    iconTint = YzMuted,
                    onClick = onPrivacyPolicyClick,
                    trailing = {
                        Icon(
                            imageVector = FeatherIcons.ChevronRight,
                            contentDescription = null,
                            tint = YzMuted,
                            modifier = Modifier.size(18.dp),
                        )
                    },
                )
                SettingsRowDivider()
                SettingsRow(
                    title = stringResource(Res.string.settings_share_app),
                    leadingIcon = FeatherIcons.Share2,
                    iconTint = YzMuted,
                    onClick = onShareAppClick,
                    trailing = {
                        Icon(
                            imageVector = FeatherIcons.ChevronRight,
                            contentDescription = null,
                            tint = YzMuted,
                            modifier = Modifier.size(18.dp),
                        )
                    },
                )
            }

            Spacer(Modifier.height(24.dp))

            // ---------- Reset (destructive) ----------
            SettingsCard(borderColor = YzErrorBorder) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onResetDataClick)
                        .padding(15.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = FeatherIcons.Trash2,
                        contentDescription = null,
                        tint = YzError,
                        modifier = Modifier.size(19.dp),
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(text = stringResource(Res.string.settings_reset_data), color = YzError)
                }
            }

            Spacer(Modifier.height(24.dp))

        }
        if (showSignSheet) {
            SelectSignBottomSheet(
                onSignConfirmed = { sign ->
                    onChangeSignClick(sign)
                    showSignSheet = false
                },
                onDismiss = { showSignSheet = false },
            )
        }
        if (state.showRestartDialog) {
            AlertDialog(
                onDismissRequest = onDismissRestartDialog,
                containerColor = YzSurface,
                shape = RoundedCornerShape(20.dp),
                titleContentColor = YzGold,
                textContentColor = YzMuted,
                title = { Text(text = stringResource(Res.string.settings_restart_title)) },
                text = { Text(text = stringResource(Res.string.settings_restart_message)) },
                confirmButton = {
                    TextButton(onClick = onDismissRestartDialog) {
                        Text(
                            text = stringResource(Res.string.settings_restart_ok), color = YzGold,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
            )
        }
        if (state.showResetDialog) {
            AlertDialog(
                onDismissRequest = onDismissResetDialog,
                containerColor = YzSurface,
                shape = RoundedCornerShape(20.dp),
                titleContentColor = YzGold,
                textContentColor = YzMuted,
                title = { Text(text = stringResource(Res.string.settings_reset_dialog_title)) },
                text = { Text(text = stringResource(Res.string.settings_reset_dialog_message)) },
                dismissButton = {
                    TextButton(onClick = onDismissResetDialog) {
                        Text(text = stringResource(Res.string.settings_reset_dialog_cancel), color = YzMuted)
                    }
                },
                confirmButton = {
                    TextButton(onClick = onConfirmResetClick) {
                        Text(text = stringResource(Res.string.settings_reset_dialog_confirm), color = YzError)
                    }
                },
            )
        }
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp),
        )
    }
}

@Composable
private fun LanguageOption(
    label: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (selected) YzGold else YzSurfaceAlt)
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = label, color = if (selected) YzSurface else YzInk)
    }
}