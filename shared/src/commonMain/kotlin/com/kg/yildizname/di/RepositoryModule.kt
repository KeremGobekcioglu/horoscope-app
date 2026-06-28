package com.kg.yildizname.di

import com.kg.yildizname.core.data.repository.HoroscopeRepository
import com.kg.yildizname.core.data.repository.UserRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { HoroscopeRepository(get(), get(), get()) }
    single { UserRepository(get()) }
}
