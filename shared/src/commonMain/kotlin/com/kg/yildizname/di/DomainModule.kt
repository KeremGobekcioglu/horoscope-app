package com.kg.yildizname.di

import com.kg.yildizname.core.domain.usecase.GetDailyReadingUseCase
import com.kg.yildizname.core.domain.usecase.RegisterDeviceForNotificationsUseCase
import com.kg.yildizname.core.domain.usecase.ResetAppDataUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { GetDailyReadingUseCase(get()) }
    factory { RegisterDeviceForNotificationsUseCase(get(),get(),get() , get()) }
    factory { ResetAppDataUseCase(get(), get(), get(), get(), get()) }
}
