package com.kg.yildizname.di

import com.kg.yildizname.core.data.prefs.UserPreferencesDataSource
import com.kg.yildizname.feature.onboarding.ui.OnboardingViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule: Module = module {
    single { UserPreferencesDataSource(get()) }
    viewModel { OnboardingViewModel(get()) }
}
