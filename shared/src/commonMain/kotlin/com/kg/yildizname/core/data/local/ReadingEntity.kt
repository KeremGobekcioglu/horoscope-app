package com.kg.yildizname.core.data.local

import androidx.room.Entity

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
