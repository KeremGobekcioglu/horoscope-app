package com.kg.yildizname.core.data.repository

import com.kg.yildizname.core.data.local.CompatibilityDao
import com.kg.yildizname.core.data.local.CompatibilityEntity
import com.kg.yildizname.core.data.local.toDomain
import com.kg.yildizname.core.data.local.toEntity
import com.kg.yildizname.core.data.model.CompatibilityResult
import com.kg.yildizname.core.data.model.ZodiacSign
import com.kg.yildizname.core.data.model.toDomain
import com.kg.yildizname.core.data.remote.FirestoreSource
import com.kg.yildizname.core.util.pairId
import io.ktor.util.logging.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


/**
 * Fetches compatibility data for a pair of signs, using a two-tier cache strategy:
 * Room (local, fast) first, then Firestore (remote, source of truth) if Room is empty.
 *
 * Unlike HoroscopeRepository, there is NO third tier here (no third-party API fallback).
 * All 78 sign-pair combinations are pre-generated and already sitting in Firestore, so a
 * Firestore miss should never really happen in normal operation — if it does, it means
 * something is wrong (bad doc ID, missing doc), not "data just isn't ready yet."
 */

class CompatibilityRepository(
    private val dao: CompatibilityDao,
    private val firestoreSource: FirestoreSource
)
{
    /**
     * Why Flow instead of a plain suspend function:
     *
     * A suspend function can only return ONE value, once, to whoever called it directly.
     * Flow lets us emit MULTIPLE values over time to whoever is COLLECTING it (usually a
     * ViewModel). That matters here because of the two-tier flow below: we might emit once
     * from Room, and then emit AGAIN from Firestore a moment later — same function call,
     * two separate deliveries. A suspend function can't do that; Flow can.
     *
     * Impact for the caller (ViewModel): instead of awaiting one value, you collect this
     * Flow (e.g. `.onEach { result -> ... }` or `stateIn` into a StateFlow). The UI updates
     * itself automatically every time a new value is emitted, without you writing any
     * manual "check again" logic.
     */

    suspend fun getCompatibilityResult(signA: ZodiacSign,signB: ZodiacSign)
    : CompatibilityResult?
    {
        // tier 1: room

        // Doc IDs (both in Firestore and in our Room table) are alphabetical, e.g.
        // "aries_leo" — never "leo_aries". pairId() sorts the two signs so it doesn't
        // matter which order the caller passed signA/signB in; the lookup key comes out
        // the same either way.
        val id = pairId(signA, signB)

        // STEP 1 — Room (local cache).
        // This is a direct DB read, no network involved, so it's near-instant.
        // If we already fetched this pair before, it's sitting here from last time.
        val cached = dao.getById(id)
        if(cached != null)
        {
            println("CompatibilityRepository: ROOM HIT for id=$id")
           //emit(cached.toDomain())
           // return@flow
            return cached.toDomain()
        }
        println("CompatibilityRepository: ROOM MISS for id=$id, trying Firestore")

        // STEP 2 — Firestore (remote source of truth).
        // Only runs if Room had nothing for this pair (e.g. first time this exact
        // pair has ever been looked up on this device).


        val compatibilityResultDto = firestoreSource.getCompatibilityResult(signA,signB)
        if(compatibilityResultDto != null)
        {
            println("CompatibilityRepository: FIRESTORE HIT for id=$id (matchPercent=${compatibilityResultDto.matchPercent})")
            val domain = compatibilityResultDto.toDomain()
            // Save it to Room now, so next time this pair is requested, STEP 1 above
            // finds it instantly, and we skip Firestore entirely.
            dao.upsert(domain.toEntity())
            //emit(domain)
            return domain
        }
        else
        {
            println("CompatibilityRepository: FIRESTORE MISS for id=$id — no doc found, no fallback exists. This should not happen if all 78 pairs are seeded.")

            // STEP 3 — there is nothing further to fall back to.
            // Every one of the 78 possible pairs is supposed to already exist in
            // Firestore, generated ahead of time. Reaching here means something is
            // wrong — e.g. pairId() built an ID that doesn't match any real document —
            // NOT a normal "not ready yet" situation like daily readings can have.
            //emit(null)
            return null
        }
    }
}