package com.kg.yildizname.core.data.prefs

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.kg.yildizname.core.data.model.ZodiacSign
import com.kg.yildizname.core.util.DateUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate

class UserPreferencesDataSource(
    private val dataStore: DataStore<Preferences>,
) {
    private val zodiacSignKey         = stringPreferencesKey("zodiac_sign_key")
    private val onboardingCompleteKey = booleanPreferencesKey("onboarding_complete")
    private val KEY_BIRTH_DAY         = intPreferencesKey("birth_day")
    private val KEY_BIRTH_MONTH       = intPreferencesKey("birth_month")
    private val KEY_BIRTH_YEAR        = intPreferencesKey("birth_year")
    private val KEY_BIRTH_TIME        = stringPreferencesKey("birth_time")
    private val KEY_BIRTH_CITY        = stringPreferencesKey("birth_city")
    private val KEY_GENDER            = stringPreferencesKey("gender")

    private val KEY_LANGUAGE = stringPreferencesKey("language_key")
    private val KEY_INSTALL_DATE = stringPreferencesKey("install_date")
    suspend fun saveZodiacSign(key: String) {
        dataStore.edit { it[zodiacSignKey] = key }
    }

    suspend fun getZodiacSign(): String? {
        return dataStore.data.first()[zodiacSignKey]
    }
    suspend fun saveLanguage(lang: String) {
        dataStore.edit { it[KEY_LANGUAGE] = lang }
    }

    suspend fun getLanguage(): String? =
        dataStore.data.first()[KEY_LANGUAGE]
    
    fun getSignFlow() : Flow<String>
    {
        return dataStore.data.map {
            it[zodiacSignKey] ?: ""
        }
    }

    suspend fun markOnboardingComplete() {
        dataStore.edit { it[onboardingCompleteKey] = true }
    }

    suspend fun isOnboardingComplete(): Boolean {
        return dataStore.data.first()[onboardingCompleteKey] ?: false
    }

    suspend fun saveBirthDate(day: Int, month: Int, year: Int) {
        dataStore.edit {
            it[KEY_BIRTH_DAY]   = day
            it[KEY_BIRTH_MONTH] = month
            it[KEY_BIRTH_YEAR]  = year
        }
    }

    suspend fun saveBirthTime(time: String) {
        dataStore.edit { it[KEY_BIRTH_TIME] = time }
    }

    suspend fun saveBirthCity(city: String) {
        dataStore.edit { it[KEY_BIRTH_CITY] = city }
    }

    suspend fun saveGender(gender: String) {
        dataStore.edit { it[KEY_GENDER] = gender }
    }

    suspend fun getBirthDay(): Int?     = dataStore.data.first()[KEY_BIRTH_DAY]
    suspend fun getBirthMonth(): Int?   = dataStore.data.first()[KEY_BIRTH_MONTH]
    suspend fun getBirthYear(): Int?    = dataStore.data.first()[KEY_BIRTH_YEAR]
    suspend fun getBirthTime(): String? = dataStore.data.first()[KEY_BIRTH_TIME]
    suspend fun getBirthCity(): String? = dataStore.data.first()[KEY_BIRTH_CITY]
    suspend fun getGender(): String?    = dataStore.data.first()[KEY_GENDER]

    // Idempotent: returns the stored first-open date, or seeds it with
    // [fallback] on the very first read. Never overwrites once set. Callers
    // pick the fallback because "never set yet" is ambiguous: it means today
    // for a genuinely new install, but for an existing user updating into a
    // version that just added this key, it should be a date that doesn't
    // clip calendar history they already had access to.
    suspend fun getOrCreateInstallDate(fallback: LocalDate = DateUtils.todayLocalDate()): LocalDate {
        val stored = dataStore.data.first()[KEY_INSTALL_DATE]
        if (stored != null) return LocalDate.parse(stored)

        dataStore.edit { it[KEY_INSTALL_DATE] = fallback.toString() }
        return fallback
    }

    suspend fun clearAll() {
        dataStore.edit { it.clear() }
    }

    // Language is a UI/accessibility preference, not user data — it must survive
    // a data reset intact, so it's read out and restored inside the same atomic edit.
    // install_date is likewise exempt: it marks when this device/app started
    // existing for calendar-availability purposes, not personal profile data,
    // so a "reset my data" action must not shrink the user's calendar history.
    suspend fun clearAllExceptLanguage() {
        dataStore.edit { prefs ->
            val lang = prefs[KEY_LANGUAGE]
            val installDate = prefs[KEY_INSTALL_DATE]
            prefs.clear()
            if (lang != null) prefs[KEY_LANGUAGE] = lang
            if (installDate != null) prefs[KEY_INSTALL_DATE] = installDate
        }
    }
}
