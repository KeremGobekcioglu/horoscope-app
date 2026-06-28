package com.kg.yildizname.core.data.repository

import com.kg.yildizname.core.data.model.ZodiacSign
import com.kg.yildizname.core.data.prefs.UserPreferencesDataSource

class UserRepository(
    private val dataSource: UserPreferencesDataSource,
) {
    suspend fun getSavedSign(): ZodiacSign? =
        dataSource.getZodiacSign()?.let { ZodiacSign.fromKey(it) }

    suspend fun saveSign(sign: ZodiacSign) =
        dataSource.saveZodiacSign(sign.apiKey)

    suspend fun hasCompletedOnboarding(): Boolean =
        dataSource.isOnboardingComplete()

    suspend fun setOnboarded() =
        dataSource.markOnboardingComplete()
}
