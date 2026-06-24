package com.kg.yildizname.core.data.prefs

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first

class UserPreferencesDataSource(
    private val dataStore: DataStore<Preferences>,
) {
    private val zodiacSignKey         = stringPreferencesKey("zodiac_sign_key")
    private val onboardingCompleteKey = booleanPreferencesKey("onboarding_complete")

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
}
