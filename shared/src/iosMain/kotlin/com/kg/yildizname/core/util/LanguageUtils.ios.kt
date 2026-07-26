package com.kg.yildizname.core.util

import platform.Foundation.NSLocale
import platform.Foundation.NSUserDefaults
import platform.Foundation.currentLocale
import platform.Foundation.languageCode

actual fun currentLanguageCode(): String = NSLocale.currentLocale.languageCode ?: "en"

actual fun applyLanguage(lang: String) {
    NSUserDefaults.standardUserDefaults.setObject(listOf(lang), forKey = "AppleLanguages")
    NSUserDefaults.standardUserDefaults.synchronize()
}
