package com.kg.yildizname.core.data.local

// data/local/CompatibilityMappers.kt


import com.kg.yildizname.core.data.model.CompatibilityResult
import com.kg.yildizname.core.data.model.CompatibilityScores
import com.kg.yildizname.core.data.model.ZodiacSign

fun CompatibilityResult.toEntity(): CompatibilityEntity =
    CompatibilityEntity(
        id = id,
        signA = signs[0].firestoreKey,
        signB = signs[1].firestoreKey,
        matchPercent = matchPercent,
        love = scores.love,
        communication = scores.communication,
        friendship = scores.friendship,
        longTerm = scores.longTerm,
        content = content
    )

fun CompatibilityEntity.toDomain(): CompatibilityResult =
    CompatibilityResult(
        id = id,
        signs = listOf(ZodiacSign.fromKey(signA), ZodiacSign.fromKey(signB)),
        matchPercent = matchPercent,
        scores = CompatibilityScores(love, communication, friendship, longTerm),
        content = content
    )