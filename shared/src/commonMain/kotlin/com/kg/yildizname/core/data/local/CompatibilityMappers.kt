package com.kg.yildizname.core.data.local

// data/local/CompatibilityMappers.kt

import com.kg.yildizname.core.data.model.CompatibilityContent
import com.kg.yildizname.core.data.model.CompatibilityLocalizedContent
import com.kg.yildizname.core.data.model.CompatibilityResult
import com.kg.yildizname.core.data.model.CompatibilityScores
import com.kg.yildizname.core.data.model.ZodiacSign
import com.kg.yildizname.core.data.model.localized
import com.kg.yildizname.core.data.remote.CompatibilityContentDto
import com.kg.yildizname.core.data.remote.CompatibilityLocalizedContentDto
import com.kg.yildizname.core.data.remote.CompatibilityResultDto
import com.kg.yildizname.core.util.currentLanguageCode

// Both languages are cached in Room (CompatibilityEntity.content), same as
// ReadingEntity keeps textTr/textEn side by side. The language choice itself is
// resolved only when reading OUT of Room, in CompatibilityEntity.toDomain() below —
// mirrors ReadingEntity.toDomain(), so UI/ViewModel code never touches currentLanguageCode().
fun CompatibilityResultDto.toEntity(): CompatibilityEntity {
    val signA = ZodiacSign.fromKey(signs[0])
    val signB = ZodiacSign.fromKey(signs[1])
    return CompatibilityEntity(
        id = id,
        signA = signA.firestoreKey,
        signB = signB.firestoreKey,
        matchPercent = matchPercent,
        love = scores.love,
        communication = scores.communication,
        friendship = scores.friendship,
        longTerm = scores.longTerm,
        content = content.toEntityContent()
    )
}

fun CompatibilityEntity.toDomain(): CompatibilityResult {
    val isTr = currentLanguageCode() == "tr"
    return CompatibilityResult(
        id = id,
        signs = listOf(ZodiacSign.fromKey(signA), ZodiacSign.fromKey(signB)),
        matchPercent = matchPercent,
        scores = CompatibilityScores(love, communication, friendship, longTerm),
        content = content.localized(isTr)
    )
}

private fun CompatibilityLocalizedContentDto.toEntityContent(): CompatibilityLocalizedContent =
    CompatibilityLocalizedContent(en = en.toEntityContent(), tr = tr.toEntityContent())

private fun CompatibilityContentDto.toEntityContent(): CompatibilityContent =
    CompatibilityContent(summary, strengths, challenges, communication, loveAndIntimacy, advice, pros, cons)
