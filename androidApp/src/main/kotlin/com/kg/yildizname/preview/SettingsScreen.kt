package com.kg.yildizname.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kg.yildizname.core.data.model.ZodiacSign
import com.kg.yildizname.core.ui.theme.YzBg
import com.kg.yildizname.core.ui.theme.YzTheme
import com.kg.yildizname.feature.settings.ui.SettingsScreen
import com.kg.yildizname.feature.settings.ui.SettingsState

@Preview
@Composable
fun SettingsScreenPreview() {
    YzTheme {
        Box(modifier = Modifier.fillMaxSize().background(YzBg)) {
            SettingsScreen(
                notificationsEnabled = true,
                notificationTime = "09:00",
                currentLanguage = "tr",
                appVersion = "1.0.0",
                onChangeSignClick = {},
                onNotificationSwitchTapped = {},
                onTimeClick = {},
                onLanguageChange = {},
                onPrivacyPolicyClick = {},
                onShareAppClick = {},
                onResetDataClick = {},
                onDismissRestartDialog = {},
                onDismissResetDialog = {},
                onConfirmResetClick = {},
                onErrorShown = {},
                refreshNotificationStatus = {},
                state = SettingsState(
                    notificationsEnabled = true,
                    sign = ZodiacSign.ARIES,
                ),
            )
        }
    }
}