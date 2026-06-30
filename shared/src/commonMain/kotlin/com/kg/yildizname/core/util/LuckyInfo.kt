package com.kg.yildizname.core.util

import kotlin.math.abs

object LuckyInfo {

    private val trColors = listOf(
        "Kırmızı", "Mavi", "Yeşil", "Sarı", "Mor", "Turuncu", "Pembe",
        "Bordo", "Lacivert", "Altın", "Gümüş", "Turkuaz", "Zümrüt", "Bej",
    )

    private val enColors = listOf(
        "Red", "Blue", "Green", "Yellow", "Purple", "Orange", "Pink",
        "Maroon", "Navy", "Gold", "Silver", "Turquoise", "Emerald", "Beige",
    )

    // Deterministic 1–99 lucky number for a given sign + date.
    fun luckyNumber(sign: String, date: String): Int {
        val hash = djb2("${sign}${date}lucky_number")
        return (abs(hash) % 99) + 1
    }

    // Deterministic color name (language-aware) for a given sign + date.
    fun luckyColorName(sign: String, date: String): String {
        val hash = djb2("${sign}${date}lucky_color")
        val index = abs(hash) % trColors.size
        return if (currentLanguageCode() == "tr") trColors[index] else enColors[index]
    }

    private fun djb2(input: String): Int {
        var hash = 0
        for (ch in input) hash = hash * 31 + ch.code
        return hash
    }
}
