package com.kg.yildizname.core.util

import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.languageCode

actual fun currentLanguageCode(): String = NSLocale.currentLocale.languageCode ?: "en"
