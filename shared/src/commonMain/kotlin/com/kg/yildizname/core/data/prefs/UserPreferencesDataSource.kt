package com.kg.yildizname.core.data.prefs

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first

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

    suspend fun saveZodiacSign(key: String) {
        dataStore.edit { it[zodiacSignKey] = key }
    }

    suspend fun getZodiacSign(): String? {
        return dataStore.data.first()[zodiacSignKey]
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
}
