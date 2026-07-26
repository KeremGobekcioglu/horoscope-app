package com.kg.yildizname

import androidx.compose.ui.window.ComposeUIViewController
import com.kg.yildizname.core.util.bootstrapLanguage
import com.kg.yildizname.di.appModule
import com.kg.yildizname.di.databaseModule
import com.kg.yildizname.di.domainModule
import com.kg.yildizname.di.networkModule
import com.kg.yildizname.di.repositoryModule
import org.koin.core.context.startKoin
import org.koin.mp.KoinPlatformTools // Import this if you still need the manual check

fun MainViewController() = ComposeUIViewController { App() }

fun initKoin() {
    // Must happen before the first Compose composition so CMP resolves string
    // resources against the frozen, previous-session language for this whole run.
    bootstrapLanguage()

    // Modern Koin way to check if already started
    if (KoinPlatformTools.defaultContext().getOrNull() != null) return

    startKoin {
        modules(
            appModule,
            iosPlatformModule,
            databaseModule,
            networkModule,
            repositoryModule,
            domainModule,
        )
    }
}