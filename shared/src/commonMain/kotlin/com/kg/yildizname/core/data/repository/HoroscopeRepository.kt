package com.kg.yildizname.core.data.repository

import com.kg.yildizname.core.data.local.ReadingDao
import com.kg.yildizname.core.data.local.ReadingEntity
import com.kg.yildizname.core.data.local.hasCategories
import com.kg.yildizname.core.data.local.isComplete
import com.kg.yildizname.core.data.local.toDomain
import com.kg.yildizname.core.data.model.PeriodType
import com.kg.yildizname.core.data.model.Reading
import com.kg.yildizname.core.data.model.CategoryDetail
import com.kg.yildizname.core.data.model.ScoreSet
import com.kg.yildizname.core.data.model.ZodiacSign
import com.kg.yildizname.core.data.remote.FirestoreSource
import com.kg.yildizname.core.data.remote.HoroscopeApiSource
import com.kg.yildizname.core.util.DateUtils
import com.kg.yildizname.core.util.PseudoScores
import com.kg.yildizname.core.util.currentLanguageCode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.LocalDate
import kotlinx.datetime.number

class HoroscopeRepository(
    private val dao: ReadingDao,
    private val firestoreSource: FirestoreSource,
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
        println("HoroscopeRepository: getReading sign=${sign.apiKey} period=${period.apiKey} date=$date")

        // A future reading doesn't exist yet anywhere in the pipeline (Room, Firestore, or
        // the API, which only ever returns "today"'s text). Refuse it here so no caller can
        // accidentally store/display today's or mislabelled English fallback text under a
        // future date key. Emitting nothing leaves the caller's reading state null, which the
        // UI already renders as the "no reading yet" placeholder.
        if (isFutureReading(date)) {
            println("HoroscopeRepository: refusing future reading sign=${sign.apiKey} period=${period.apiKey} date=$date")
            return@flow
        }

        // Tier 1: Room
        val cached = dao.getReading(sign.apiKey, period.apiKey, date)
        println("HoroscopeRepository: ROOM ${if (cached == null) "MISS" else "HIT (isComplete=${cached.isComplete(period)}, hasCategories=${cached.hasCategories()})"}")
        if (cached != null) {
            emit(cached.toDomain(sign, period))
            if (cached.isComplete(period)) return@flow
            // No categories yet (fallback or pre-split) — fall through to Firestore to enrich
        }

        // Tier 2: Firestore
        val firestoreDto = firestoreSource.getReading(sign, date, period)
        println("HoroscopeRepository: FIRESTORE ${if (firestoreDto == null) "MISS" else "HIT"}")
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

        println("HoroscopeRepository: falling back to API")
        val apiDto = apiSource.getReading(sign, period)
            ?: run {
                println("HoroscopeRepository: API MISS for ${sign.apiKey}")
                throw Exception("No reading available for ${sign.apiKey}")
            }
        println("HoroscopeRepository: API HIT")
        val hasScoreCategories = period != PeriodType.MONTHLY

        val entity = ReadingEntity(
            sign        = sign.apiKey,
            period      = period.apiKey,
            date        = apiDto.date,
            textTr      = apiDto.horoscope,
            textEn      = apiDto.horoscope,
            scoreLove   = if (hasScoreCategories) PseudoScores.compute(sign.apiKey, apiDto.date, "love") else 0,
            scoreWork   = if (hasScoreCategories) PseudoScores.compute(sign.apiKey, apiDto.date, "work") else 0,
            scoreHealth = if (hasScoreCategories) PseudoScores.compute(sign.apiKey, apiDto.date, "health") else 0,
            scoreLuck   = if (hasScoreCategories) PseudoScores.compute(sign.apiKey, apiDto.date, "luck") else 0,
            isFallback  = true,
        )
        dao.upsertReading(entity)
        pruneOldEntries()
        emit(entity.toDomain(sign, period))
    }

    // `date` is either a monthly key ("yyyy-MM", from CalendarViewModel) or a full
    // daily key ("yyyy-MM-dd", from everywhere else).
    private fun isFutureReading(date: String): Boolean {
        val today = DateUtils.todayLocalDate()
        return if (date.length == 7) {
            val year = date.substring(0, 4).toIntOrNull() ?: return false
            val month = date.substring(5, 7).toIntOrNull() ?: return false
            year > today.year || (year == today.year && month > today.month.number)
        } else {
            val requested = date.toLocalDateOrNull() ?: return false
            requested > today
        }
    }

    private fun String.toLocalDateOrNull(): LocalDate? = try {
        LocalDate.parse(this)
    } catch (e: Exception) {
        null
    }

    private suspend fun pruneOldEntries() {
        val cutoff = kotlin.time.Clock.System.now().toEpochMilliseconds() - (30L * 24 * 60 * 60 * 1000)
        dao.deleteOlderThan(cutoff)
    }
}



