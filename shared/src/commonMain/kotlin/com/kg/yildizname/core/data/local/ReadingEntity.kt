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
    val text: String,
    val scoreLove: Int,
    val scoreWork: Int,
    val scoreHealth: Int,
    val scoreLuck: Int,
    val isFallback: Boolean,
    val cachedAt: Long = kotlin.time.Clock.System.now().toEpochMilliseconds(),
)
