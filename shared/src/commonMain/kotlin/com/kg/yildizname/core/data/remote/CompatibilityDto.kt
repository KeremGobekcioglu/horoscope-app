package com.kg.yildizname.core.data.remote
import kotlinx.serialization.Serializable

@Serializable
data class CompatibilityResultDto(
    val id: String,
    val signs: List<String>,
    val matchPercent: Int,
    val scores: CompatibilityScoresDto,
    val content: CompatibilityLocalizedContentDto
)

@Serializable
data class CompatibilityScoresDto(
    val love: Int,
    val communication: Int,
    val friendship: Int,
    val longTerm: Int
)

@Serializable
data class CompatibilityLocalizedContentDto(
    val en: CompatibilityContentDto,
    val tr: CompatibilityContentDto
)

@Serializable
data class CompatibilityContentDto(
    val summary: String,
    val strengths: String,
    val challenges: String,
    val communication: String,
    val loveAndIntimacy: String,
    val advice: String,
    val pros: List<String>,
    val cons: List<String>,
    val friendship: String,
    val longTerm: String,
    val finalVerdict: String
)