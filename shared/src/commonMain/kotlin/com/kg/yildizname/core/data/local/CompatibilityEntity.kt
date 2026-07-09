package com.kg.yildizname.core.data.local

// data/local/CompatibilityEntity.kt
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.kg.yildizname.core.data.model.CompatibilityLocalizedContent

@Entity(tableName = "compatibility_results")
@TypeConverters(CompatibilityConverters::class)
data class CompatibilityEntity(
    @PrimaryKey val id: String,
    val signA: String,
    val signB: String,
    val matchPercent: Int,
    val love: Int,
    val communication: Int,
    val friendship: Int,
    val longTerm: Int,
    val content: CompatibilityLocalizedContent
)