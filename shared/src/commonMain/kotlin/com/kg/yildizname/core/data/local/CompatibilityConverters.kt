package com.kg.yildizname.core.data.local

// data/local/CompatibilityConverters.kt

import androidx.room.TypeConverter
import com.kg.yildizname.core.data.model.CompatibilityLocalizedContent
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class CompatibilityConverters {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromContent(content: CompatibilityLocalizedContent): String =
        json.encodeToString(content)

    @TypeConverter
    fun toContent(value: String): CompatibilityLocalizedContent =
        json.decodeFromString(value)
}