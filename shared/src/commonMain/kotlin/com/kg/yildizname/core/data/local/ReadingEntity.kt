package com.kg.yildizname.core.data.local

import androidx.room.Entity
import com.kg.yildizname.core.data.model.CategoryDetail
import com.kg.yildizname.core.data.model.PeriodType
import com.kg.yildizname.core.data.model.Reading
import com.kg.yildizname.core.data.model.ScoreSet
import com.kg.yildizname.core.data.model.ZodiacSign
import com.kg.yildizname.core.util.currentLanguageCode

@Entity(
    tableName = "readings",
    primaryKeys = ["sign", "period", "date"],
)
data class ReadingEntity(
    val sign: String,
    val period: String,
    val date: String,
    val textTr: String,
    val textEn: String,
    val textLoveTr: String? = null,
    val textLoveEn: String? = null,
    val textWorkTr: String? = null,
    val textWorkEn: String? = null,
    val textHealthTr: String? = null,
    val textHealthEn: String? = null,
    val textLuckTr: String? = null,
    val textLuckEn: String? = null,
    val scoreLove: Int,
    val scoreWork: Int,
    val scoreHealth: Int,
    val scoreLuck: Int,
    val isFallback: Boolean,
    val cachedAt: Long = kotlin.time.Clock.System.now().toEpochMilliseconds(),
)

public fun ReadingEntity.hasCategories(): Boolean {
    return !textLoveTr.isNullOrEmpty() && !textLoveEn.isNullOrEmpty() &&
            !textWorkTr.isNullOrEmpty() && !textWorkEn.isNullOrEmpty() &&
            !textHealthTr.isNullOrEmpty() && !textHealthEn.isNullOrEmpty() &&
            !textLuckTr.isNullOrEmpty() && !textLuckEn.isNullOrEmpty()
}


public fun ReadingEntity.toDomain(sign: ZodiacSign, period: PeriodType): Reading {
    val isTr = currentLanguageCode() == "tr"

    val detail = if (hasCategories()) {
        CategoryDetail(
            love   = if (isTr) textLoveTr!! else textLoveEn!!,
            work   = if (isTr) textWorkTr!! else textWorkEn!!,
            health = if (isTr) textHealthTr!! else textHealthEn!!,
            luck   = if (isTr) textLuckTr!! else textLuckEn!!,
        )
    } else null

    return Reading(
        sign           = sign,
        period         = period,
        date           = date,
        text           = if (isTr) textTr.ifEmpty { textEn } else textEn.ifEmpty { textTr },
        scores         = ScoreSet(scoreLove, scoreWork, scoreHealth, scoreLuck),
        categoryDetail = detail,
        isFromCache    = true,
        isFallback     = isFallback,
    )
}

fun ReadingEntity.isComplete(period: PeriodType): Boolean =
    when (period) {
        PeriodType.MONTHLY -> textTr.isNotEmpty() && !isFallback
        else -> hasCategories()
    }
