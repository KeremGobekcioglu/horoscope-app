package com.kg.yildizname.core.data.repository

import com.kg.yildizname.core.data.local.ReadingDao
import com.kg.yildizname.core.data.local.ReadingEntity
import com.kg.yildizname.core.data.model.PeriodType
import com.kg.yildizname.core.data.model.Reading
import com.kg.yildizname.core.data.model.CategoryDetail
import com.kg.yildizname.core.data.model.ScoreSet
import com.kg.yildizname.core.data.model.ZodiacSign
import com.kg.yildizname.core.data.remote.FirestoreReadingSource
import com.kg.yildizname.core.data.remote.HoroscopeApiSource
import com.kg.yildizname.core.util.PseudoScores
import com.kg.yildizname.core.util.currentLanguageCode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class HoroscopeRepository(
    private val dao: ReadingDao,
    private val firestoreSource: FirestoreReadingSource,
    private val apiSource: HoroscopeApiSource,
) {
    /**
     * 3-tier cache with stale-cache upgrade:
     *   Tier 1 — Room hit with full categories → emit instantly, done
     *   Tier 1b— Room hit without categories (pre-split cache) → emit optimistically,
     *            then fall through to Firestore to upgrade
     *   Tier 2 — Firestore hit → overwrite Room, emit enriched reading
     *   Tier 3 — API fallback  → only runs when no Room row exists at all
     *
     * A stale Room row (isFallback=false but no category text) is always preferred
     * over the API fallback, since both give only general text but Firestore might
     * have the enriched version ready.
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
            if (cached.hasCategories) return@flow
            // No categories yet (fallback or pre-split) — fall through to Firestore to enrich
        }

        // Tier 2: Firestore
        val firestoreDto = firestoreSource.getReading(sign, date, period)
        if (firestoreDto != null) {
            val entity = ReadingEntity(
                sign         = sign.apiKey,
                period       = period.apiKey,
                date         = date,
                textTr       = firestoreDto.textTr,
                textEn       = firestoreDto.textEn,
                textLoveTr   = firestoreDto.textLoveTr,
                textLoveEn   = firestoreDto.textLoveEn,
                textWorkTr   = firestoreDto.textWorkTr,
                textWorkEn   = firestoreDto.textWorkEn,
                textHealthTr = firestoreDto.textHealthTr,
                textHealthEn = firestoreDto.textHealthEn,
                textLuckTr   = firestoreDto.textLuckTr,
                textLuckEn   = firestoreDto.textLuckEn,
                scoreLove    = firestoreDto.scoreLove,
                scoreWork    = firestoreDto.scoreWork,
                scoreHealth  = firestoreDto.scoreHealth,
                scoreLuck    = firestoreDto.scoreLuck,
                isFallback   = false,
            )
            dao.upsertReading(entity)
            pruneOldEntries()
            emit(entity.toDomain(sign, period))
            return@flow
        }

        // Tier 3: API fallback — only when there is no Room row at all.
        // If a stale row exists, it is no worse than the API (both lack category
        // detail) and avoids an extra network call.
        if (cached != null) return@flow

        val apiDto = apiSource.getReading(sign, period)
            ?: throw Exception("No reading available for ${sign.apiKey}")
        val entity = ReadingEntity(
            sign        = sign.apiKey,
            period      = period.apiKey,
            date        = apiDto.date,
            textTr      = apiDto.horoscope,
            textEn      = apiDto.horoscope,
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

private val ReadingEntity.hasCategories: Boolean
    get() = !textLoveTr.isNullOrEmpty() && !textLoveEn.isNullOrEmpty() &&
            !textWorkTr.isNullOrEmpty() && !textWorkEn.isNullOrEmpty() &&
            !textHealthTr.isNullOrEmpty() && !textHealthEn.isNullOrEmpty() &&
            !textLuckTr.isNullOrEmpty() && !textLuckEn.isNullOrEmpty()

private fun ReadingEntity.toDomain(sign: ZodiacSign, period: PeriodType): Reading {
    val isTr = currentLanguageCode() == "tr"

    val detail = if (hasCategories) {
        CategoryDetail(
            love   = if (isTr) textLoveTr!! else textLoveEn!!,
            work   = if (isTr) textWorkTr!! else textWorkEn!!,
            health = if (isTr) textHealthTr!! else textHealthEn!!,
            luck   = if (isTr) textLuckTr!! else textLuckEn!!,
        )
    } else null

    return Reading(
        sign           = sign,
        period         = period,
        date           = date,
        text           = if (isTr) textTr.ifEmpty { textEn } else textEn.ifEmpty { textTr },
        scores         = ScoreSet(scoreLove, scoreWork, scoreHealth, scoreLuck),
        categoryDetail = detail,
        isFromCache    = true,
        isFallback     = isFallback,
    )
}
