package com.kg.yildizname.feature.settings.ui

import com.kg.yildizname.core.data.model.ZodiacSign

data class SettingsState(
    val notificationsEnabled: Boolean? = null,
    val sign: ZodiacSign = ZodiacSign.SCORPIO
)