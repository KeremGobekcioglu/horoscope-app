package com.kg.yildizname.di

import com.kg.yildizname.core.domain.usecase.GetDailyReadingUseCase
import com.kg.yildizname.core.domain.usecase.RegisterDeviceForNotificationsUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { GetDailyReadingUseCase(get()) }
    factory { RegisterDeviceForNotificationsUseCase(get(),get(),get() , get()) }
}
