package com.kg.yildizname.core.util

object PseudoScores {
    // Deterministic score for sign + date + category. Range 4–10.
    // Never shows "1/10 for Love". Same inputs always produce the same output.
    // Used only on the API fallback path when no Firestore doc exists.
    fun compute(sign: String, date: String, category: String): Int {
        val input = sign + date + category
        var hash = 0
        for (ch in input) {
            hash = hash * 31 + ch.code
        }
        return (kotlin.math.abs(hash) % 7) + 4
    }
}
