package com.kg.yildizname

import androidx.compose.ui.window.ComposeUIViewController
import com.kg.yildizname.di.appModule
import org.koin.core.context.startKoin

fun MainViewController() = ComposeUIViewController { App() }

fun initKoin() {
    startKoin {
        modules(appModule, iosPlatformModule)
    }
}
