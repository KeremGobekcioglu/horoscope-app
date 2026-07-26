package com.kg.yildizname.core.util

import android.content.Context
import android.content.SharedPreferences

// Synchronous mirror of the persisted language choice, readable from
// MainActivity.attachBaseContext before Koin/DataStore are available.
object LanguagePrefsMirror {
    private const val PREFS_NAME = "language_prefs"
    private const val KEY_LANGUAGE = "language"

    private var prefs: SharedPreferences? = null

    fun init(context: Context) {
        if (prefs == null) {
            prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        }
    }

    fun save(lang: String) {
        prefs?.edit()?.putString(KEY_LANGUAGE, lang)?.apply()
    }

    fun read(): String? = prefs?.getString(KEY_LANGUAGE, null)
}
