package com.kg.yildizname.core.util

import platform.Foundation.NSLocale
import platform.Foundation.NSUserDefaults
import platform.Foundation.preferredLanguages

private const val LANGUAGE_MIRROR_KEY = "language_mirror"

// NSLocale.currentLocale.languageCode is bundle-negotiated: it picks the best language
// this app declares support for, so on an app with no "tr" localization declared it
// silently resolves to "en" even on a Turkish device (region stays "TR", language falls
// back — e.g. identifier "en_TR"). preferredLanguages reflects the OS-level language
// preference order unfiltered, so it's the one that actually reflects device language.
// Only tr/en are shipped, so anything else falls back to English.
private fun deviceLanguageCode(): String {
    val topPreference = NSLocale.preferredLanguages.firstOrNull() as? String
    val code = topPreference?.substringBefore("-")?.substringBefore("_")
    return if (code == "tr") "tr" else "en"
}

// Cached on first read so it stays frozen for the rest of the process, mirroring how
// Android freezes the locale at attachBaseContext. NSLocale.currentLocale updates live
// as soon as AppleLanguages changes, which produced a partial in-session language switch.
private val launchLanguageCode: String by lazy {
    NSUserDefaults.standardUserDefaults.stringForKey(LANGUAGE_MIRROR_KEY) ?: deviceLanguageCode()
}

actual fun currentLanguageCode(): String = launchLanguageCode

// Only mirrors the choice synchronously, for bootstrapLanguage() to pick up on the NEXT
// launch. Must NOT write AppleLanguages here — Compose Multiplatform resolves string
// resources against it, so writing it at change-time would flip UI strings live, mid-session.
actual fun applyLanguage(lang: String) {
    NSUserDefaults.standardUserDefaults.setObject(lang, forKey = LANGUAGE_MIRROR_KEY)
    NSUserDefaults.standardUserDefaults.synchronize()
}

// Must run once at process start, before the first Compose composition, so AppleLanguages
// (and therefore CMP string resolution) is frozen to the PREVIOUS session's chosen language
// for this entire run — the iOS equivalent of Android's attachBaseContext freeze. Falls back
// to the device language when no choice has been mirrored yet, matching deviceLanguageCode()
// so UI and data resolution can never disagree on the default.
fun bootstrapLanguage() {
    val lang = NSUserDefaults.standardUserDefaults.stringForKey(LANGUAGE_MIRROR_KEY) ?: deviceLanguageCode()
    NSUserDefaults.standardUserDefaults.setObject(listOf(lang), forKey = "AppleLanguages")
    NSUserDefaults.standardUserDefaults.synchronize()
}
