package com.kg.yildizname.core.data.model

import kotlinx.serialization.Serializable

data class CompatibilityResult(
    val id: String,
    val signs: List<ZodiacSign>,
    val matchPercent: Int,
    val scores: CompatibilityScores,
    val content: CompatibilityLocalizedContent
)

data class CompatibilityScores(
    val love: Int,
    val communication: Int,
    val friendship: Int,
    val longTerm: Int
)

@Serializable
data class CompatibilityLocalizedContent(
    val en: CompatibilityContent,
    val tr: CompatibilityContent
)

@Serializable
data class CompatibilityContent(
    val summary: String,
    val strengths: String,
    val challenges: String,
    val communication: String,
    val loveAndIntimacy: String,
    val advice: String,
    val pros: List<String>,
    val cons: List<String>
)

fun CompatibilityLocalizedContent.localized(isTurkish: Boolean): CompatibilityContent =
    if (isTurkish) tr else en