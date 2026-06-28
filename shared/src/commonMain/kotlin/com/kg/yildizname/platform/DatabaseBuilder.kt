package com.kg.yildizname.platform

import androidx.room.RoomDatabase
import com.kg.yildizname.core.data.local.YildiznameDatabase

expect fun getDatabaseBuilder(): RoomDatabase.Builder<YildiznameDatabase>
