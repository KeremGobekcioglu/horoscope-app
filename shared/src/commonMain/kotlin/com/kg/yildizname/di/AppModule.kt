package com.kg.yildizname.di

import com.kg.yildizname.core.data.prefs.UserPreferencesDataSource
import com.kg.yildizname.feature.calendar.ui.CalendarViewModel
import com.kg.yildizname.feature.compatability.ui.CompatibilityResult.CompatibilityResultViewModel
import com.kg.yildizname.feature.home.ui.HomeViewModel
import com.kg.yildizname.feature.onboarding.ui.OnboardingViewModel
import com.kg.yildizname.feature.readingDetail.ui.ReadingDetailViewModel
import com.kg.yildizname.feature.settings.ui.SettingsViewModel
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
    viewModel { CompatibilityResultViewModel(get()) }
    // Using (date, signKey) = params instead of params.get<T>().
    // Our route param was nullable (String?), and get<T>() can't reliably
    // type-match on a nullable type. So instead of failing, it silently
    // grabbed the wrong param (signKey) and handed it back as "date."
    // That bad value only blew up later, deep in the screen, when code
    // tried to actually use it as a date. Destructuring avoids this
    // because it unpacks by position, not by type.
    viewModel { (signKey: String, periodKey: String, date: String?) ->
        ReadingDetailViewModel(signKey, periodKey, date, get())
    }
    viewModel { SettingsViewModel(get(),get() , get() , get(), get()) }
}
