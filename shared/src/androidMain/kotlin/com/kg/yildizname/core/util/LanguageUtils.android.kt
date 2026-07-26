package com.kg.yildizname.core.util

actual fun currentLanguageCode(): String = java.util.Locale.getDefault().language

// Koin starts in MainActivity.onCreate, which runs after attachBaseContext — so
// attachBaseContext can't resolve UserPreferencesDataSource from DI in time to read
// the stored language. LanguagePrefsMirror is a plain SharedPreferences-backed store
// that's synchronously readable at that point instead.
actual fun applyLanguage(lang: String) {
    LanguagePrefsMirror.save(lang)
}
