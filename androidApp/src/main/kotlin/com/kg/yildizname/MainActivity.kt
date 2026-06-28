package com.kg.yildizname

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.kg.yildizname.di.appModule
import com.kg.yildizname.di.databaseModule
import com.kg.yildizname.di.domainModule
import com.kg.yildizname.di.networkModule
import com.kg.yildizname.di.repositoryModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
        )
        if (GlobalContext.getOrNull() == null) {
            startKoin {
                androidContext(applicationContext)
                modules(
                    appModule,
                    androidPlatformModule,
                    databaseModule,
                    networkModule,
                    repositoryModule,
                    domainModule,
                )
            }
        }
        setContent {
            App()
        }
    }
}
