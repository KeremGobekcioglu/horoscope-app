package com.kg.yildizname.di

import com.kg.yildizname.core.data.prefs.UserPreferencesDataSource
import com.kg.yildizname.feature.calendar.ui.CalendarViewModel
import com.kg.yildizname.feature.compatability.ui.CompatibilityResult.CompatibilityResultViewModel
import com.kg.yildizname.feature.home.ui.HomeViewModel
import com.kg.yildizname.feature.onboarding.ui.OnboardingViewModel
import com.kg.yildizname.feature.readingDetail.ui.ReadingDetailViewModel
import com.kg.yildizname.feature.splash.ui.SplashViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule: Module = module {
    single { UserPreferencesDataSource(get()) }
    viewModel { SplashViewModel(get()) }
    viewModel { OnboardingViewModel(get()) }
    viewModel { HomeViewModel(get(), get()) }
    viewModel { CalendarViewModel(get(),get()) }
    viewModel { params -> CompatibilityResultViewModel(get() , params.get() , params.get()) }
    // Positional destructuring, not params.get(): get<T>() matches by type, and a nullable
    // String? match is satisfied by any element (including index 0), so it silently grabbed
    // signKey instead of date. Destructuring resolves strictly by index.
    viewModel { (signKey: String, periodKey: String, date: String?) ->
        ReadingDetailViewModel(signKey, periodKey, date, get())
    }
}
