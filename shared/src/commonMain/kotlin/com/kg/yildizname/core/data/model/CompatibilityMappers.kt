package com.kg.yildizname.core.data.model

// data/model/CompatibilityMappers.kt

import com.kg.yildizname.core.data.remote.CompatibilityContentDto
import com.kg.yildizname.core.data.remote.CompatibilityLocalizedContentDto
import com.kg.yildizname.core.data.remote.CompatibilityResultDto
import com.kg.yildizname.core.data.remote.CompatibilityScoresDto

fun CompatibilityResultDto.toDomain(): CompatibilityResult =
    CompatibilityResult(
        id = id,
        signs = signs.map { ZodiacSign.fromKey(it) },
        matchPercent = matchPercent,
        scores = scores.toDomain(),
        content = content.toDomain()
    )

private fun CompatibilityScoresDto.toDomain(): CompatibilityScores =
    CompatibilityScores(love, communication, friendship, longTerm)

private fun CompatibilityLocalizedContentDto.toDomain(): CompatibilityLocalizedContent =
    CompatibilityLocalizedContent(en = en.toDomain(), tr = tr.toDomain())

private fun CompatibilityContentDto.toDomain(): CompatibilityContent =
    CompatibilityContent(summary, strengths, challenges, communication, loveAndIntimacy, advice, pros, cons)