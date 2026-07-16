package com.kg.yildizname.core.data.local

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters

@Database(
    entities = [ReadingEntity::class, CompatibilityEntity::class],
    version = 5
)
@TypeConverters(CompatibilityConverters::class)
@ConstructedBy(YildiznameDatabaseConstructor::class)
abstract class YildiznameDatabase : RoomDatabase() {
    abstract fun readingDao(): ReadingDao
    abstract fun compatibilityDao(): CompatibilityDao
}

// KSP generates the actual implementations for each platform target.
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object YildiznameDatabaseConstructor : RoomDatabaseConstructor<YildiznameDatabase>
