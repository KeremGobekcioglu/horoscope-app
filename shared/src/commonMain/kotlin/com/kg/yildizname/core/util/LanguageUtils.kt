package com.kg.yildizname.core.util

expect fun currentLanguageCode(): String

expect fun applyLanguage(lang: String)

/**
 * Root-locale uppercase() maps 'i' -> 'I', but Turkish requires 'İ' (U+0130).
 * Pre-mapping 'i' works because uppercase() leaves 'İ' unchanged.
 */
fun String.yzUppercase(isTurkish: Boolean = currentLanguageCode() == "tr"): String =
    if (isTurkish) replace('i', 'İ').uppercase() else uppercase()
