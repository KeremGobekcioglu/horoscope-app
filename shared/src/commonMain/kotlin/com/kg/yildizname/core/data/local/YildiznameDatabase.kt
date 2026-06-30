package com.kg.yildizname.core.data.local

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor

@Database(entities = [ReadingEntity::class], version = 3)
@ConstructedBy(YildiznameDatabaseConstructor::class)
abstract class YildiznameDatabase : RoomDatabase() {
    abstract fun readingDao(): ReadingDao
}

// KSP generates the actual implementations for each platform target.
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object YildiznameDatabaseConstructor : RoomDatabaseConstructor<YildiznameDatabase>
