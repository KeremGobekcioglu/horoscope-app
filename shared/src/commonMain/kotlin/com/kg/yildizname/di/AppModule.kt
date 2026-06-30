package com.kg.yildizname.di

import com.kg.yildizname.core.data.prefs.UserPreferencesDataSource
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
    viewModel { params -> ReadingDetailViewModel(params.get(), params.get(), get()) }
}
