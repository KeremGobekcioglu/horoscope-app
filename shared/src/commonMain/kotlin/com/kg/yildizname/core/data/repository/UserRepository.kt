package com.kg.yildizname.core.data.repository

import com.kg.yildizname.core.data.model.ZodiacSign
import com.kg.yildizname.core.data.prefs.UserPreferencesDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

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

    fun getSignFlow() : Flow<ZodiacSign?>
    {
        return dataSource.getSignFlow().map {
            name -> name?.let { runCatching { ZodiacSign.valueOf(it) }.getOrNull() }
        }
    }
}
