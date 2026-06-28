package com.kg.yildizname.platform

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kg.yildizname.core.data.local.YildiznameDatabase
import org.koin.mp.KoinPlatform

actual fun getDatabaseBuilder(): RoomDatabase.Builder<YildiznameDatabase> {
    val context = KoinPlatform.getKoin().get<Context>()
    val dbFile = context.getDatabasePath("yildizname.db")
    return Room.databaseBuilder<YildiznameDatabase>(
        context = context,
        name = dbFile.absolutePath,
    )
}
