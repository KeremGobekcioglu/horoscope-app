package com.kg.yildizname.core.data.model

data class Reading(
    val sign: ZodiacSign,
    val period: PeriodType,
    val date: String,           // "yyyy-MM-dd" — from API response, NOT device clock
    val text: String,
    val scores: ScoreSet,
    val isFromCache: Boolean = false,
    val isFallback: Boolean = false,  // true = freehoroscopeapi fallback, not Firestore
)
