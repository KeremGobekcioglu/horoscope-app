package com.kg.yildizname.core.data.repository

import com.kg.yildizname.core.data.local.ReadingDao
import com.kg.yildizname.core.data.local.ReadingEntity
import com.kg.yildizname.core.data.model.PeriodType
import com.kg.yildizname.core.data.model.Reading
import com.kg.yildizname.core.data.model.ScoreSet
import com.kg.yildizname.core.data.model.ZodiacSign
import com.kg.yildizname.core.data.remote.FirestoreReadingSource
import com.kg.yildizname.core.data.remote.HoroscopeApiSource
import com.kg.yildizname.core.util.PseudoScores
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class HoroscopeRepository(
    private val dao: ReadingDao,
    private val firestoreSource: FirestoreReadingSource,
    private val apiSource: HoroscopeApiSource,
) {
    /**
     * 3-tier cache:
     *   Tier 1 — Room hit      → emit instantly
     *   Tier 2 — Firestore hit → save to Room, emit
     *   Tier 3 — API fallback  → pseudo-scores, save to Room, emit
     */
    fun getReading(
        sign: ZodiacSign,
        period: PeriodType,
        date: String,
    ): Flow<Reading> = flow {

        // Tier 1: Room
        val cached = dao.getReading(sign.apiKey, period.apiKey, date)
        if (cached != null) {
            emit(cached.toDomain(sign, period))
            return@flow
        }

        // Tier 2: Firestore
        val firestoreDto = firestoreSource.getReading(sign, date, period)
        if (firestoreDto != null) {
            val entity = ReadingEntity(
                sign        = sign.apiKey,
                period      = period.apiKey,
                date        = date,
                text        = firestoreDto.text,
                scoreLove   = firestoreDto.scoreLove,
                scoreWork   = firestoreDto.scoreWork,
                scoreHealth = firestoreDto.scoreHealth,
                scoreLuck   = firestoreDto.scoreLuck,
                isFallback  = false,
            )
            dao.upsertReading(entity)
            pruneOldEntries()
            emit(entity.toDomain(sign, period))
            return@flow
        }

        // Tier 3: API fallback + pseudo-scores
        val apiDto = apiSource.getReading(sign, period)
            ?: throw Exception("No reading available for ${sign.apiKey}")
        val entity = ReadingEntity(
            sign        = sign.apiKey,
            period      = period.apiKey,
            date        = apiDto.date,
            text        = apiDto.horoscope,
            scoreLove   = PseudoScores.compute(sign.apiKey, apiDto.date, "love"),
            scoreWork   = PseudoScores.compute(sign.apiKey, apiDto.date, "work"),
            scoreHealth = PseudoScores.compute(sign.apiKey, apiDto.date, "health"),
            scoreLuck   = PseudoScores.compute(sign.apiKey, apiDto.date, "luck"),
            isFallback  = true,
        )
        dao.upsertReading(entity)
        pruneOldEntries()
        emit(entity.toDomain(sign, period))
    }

    private suspend fun pruneOldEntries() {
        val cutoff = kotlin.time.Clock.System.now().toEpochMilliseconds() - (30L * 24 * 60 * 60 * 1000)
        dao.deleteOlderThan(cutoff)
    }
}

private fun ReadingEntity.toDomain(sign: ZodiacSign, period: PeriodType) = Reading(
    sign        = sign,
    period      = period,
    date        = date,
    text        = text,
    scores      = ScoreSet(scoreLove, scoreWork, scoreHealth, scoreLuck),
    isFromCache = true,
    isFallback  = isFallback,
)
