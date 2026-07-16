package com.kg.yildizname.platform

import androidx.room.Room
import androidx.room.RoomDatabase
import com.kg.yildizname.core.data.local.YildiznameDatabase
import platform.Foundation.NSHomeDirectory

actual fun getDatabaseBuilder(): RoomDatabase.Builder<YildiznameDatabase> {
    val dbFile = NSHomeDirectory() + "/Documents/yildizname.db"
    return Room.databaseBuilder<YildiznameDatabase>(
        name = dbFile,
    ).fallbackToDestructiveMigration(dropAllTables = true)
}
