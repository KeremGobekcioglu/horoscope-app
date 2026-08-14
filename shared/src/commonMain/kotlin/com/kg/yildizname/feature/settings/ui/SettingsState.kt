package com.kg.yildizname.feature.settings.ui

import com.kg.yildizname.core.data.model.ZodiacSign

data class SettingsState(
    val notificationsEnabled: Boolean? = null,
    val sign: ZodiacSign = ZodiacSign.SCORPIO,
    val currentLanguage: String = "tr",
    val notificationTime: String = "11:00",
    val appVersion: String = "1.0.0",
    val showRestartDialog: Boolean = false,
    val showResetDialog: Boolean = false,
    val navigateToOnboarding: Boolean = false,
    val error: String? = null,
)