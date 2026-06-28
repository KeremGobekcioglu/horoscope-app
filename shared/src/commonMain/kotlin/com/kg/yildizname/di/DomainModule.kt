package com.kg.yildizname.di

import com.kg.yildizname.core.domain.usecase.GetDailyReadingUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { GetDailyReadingUseCase(get()) }
}
