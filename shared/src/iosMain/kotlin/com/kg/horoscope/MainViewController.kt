package com.kg.horoscope

import androidx.compose.ui.window.ComposeUIViewController
import com.yildizname.App
import com.yildizname.di.appModule
import org.koin.core.context.startKoin

fun MainViewController() = ComposeUIViewController { App() }

fun initKoin() {
    startKoin {
        modules(appModule)
    }
}
