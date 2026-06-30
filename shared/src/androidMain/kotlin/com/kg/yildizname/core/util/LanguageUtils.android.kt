package com.kg.yildizname.core.util

actual fun currentLanguageCode(): String = java.util.Locale.getDefault().language
