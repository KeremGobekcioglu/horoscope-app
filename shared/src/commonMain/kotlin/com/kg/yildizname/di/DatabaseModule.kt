package com.kg.yildizname.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.kg.yildizname.core.data.local.YildiznameDatabase
import com.kg.yildizname.platform.getDatabaseBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.dsl.module

val databaseModule = module {
    single<YildiznameDatabase> {
        getDatabaseBuilder()
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }
    single { get<YildiznameDatabase>().readingDao() }
}
