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

@Preview
@Composable
fun SettingsScreenPreview() {
    YzTheme {
        Box(modifier = Modifier.fillMaxSize().background(YzBg)) {
//            SettingsScreen(
//                currentSign = ZodiacSign.ARIES,
//                notificationsEnabled = true,
//                notificationTime = "09:00",
//                currentLanguage = "tr",
//                appVersion = "1.0.0",
//                onChangeSignClick = {},
//                onNotificationsEnabledChange = {},
//                onTimeClick = {},
//                onLanguageChange = {},
//                onPrivacyPolicyClick = {},
//                onShareAppClick = {},
//                onResetDataClick = {},
//            )
        }
    }
}