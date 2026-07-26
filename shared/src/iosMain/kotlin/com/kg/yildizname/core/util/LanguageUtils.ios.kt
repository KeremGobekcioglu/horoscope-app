package com.kg.yildizname.core.util

import platform.Foundation.NSLocale
import platform.Foundation.NSUserDefaults
import platform.Foundation.currentLocale
import platform.Foundation.languageCode

private const val LANGUAGE_MIRROR_KEY = "language_mirror"

// Cached on first read so it stays frozen for the rest of the process, mirroring how
// Android freezes the locale at attachBaseContext. NSLocale.currentLocale updates live
// as soon as AppleLanguages changes, which produced a partial in-session language switch.
private val launchLanguageCode: String by lazy { NSUserDefaults.standardUserDefaults.stringForKey("language_mirror") ?: "en" }

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
// for this entire run — the iOS equivalent of Android's attachBaseContext freeze. Defaults
// to "tr" when no choice has been mirrored yet, matching LanguagePrefsMirror's Android default.
fun bootstrapLanguage() {
    val lang = NSUserDefaults.standardUserDefaults.stringForKey(LANGUAGE_MIRROR_KEY) ?: "tr"
    NSUserDefaults.standardUserDefaults.setObject(listOf(lang), forKey = "AppleLanguages")
    NSUserDefaults.standardUserDefaults.synchronize()
}
