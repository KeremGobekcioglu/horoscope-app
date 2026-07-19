package com.kg.yildizname

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.kg.yildizname.core.data.remote.PushTokenProvider
import com.kg.yildizname.di.appModule
import com.kg.yildizname.di.databaseModule
import com.kg.yildizname.di.domainModule
import com.kg.yildizname.di.networkModule
import com.kg.yildizname.di.repositoryModule
import kotlinx.coroutines.launch
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
            lifecycleScope.launch {
                val provider: PushTokenProvider = GlobalContext.get().get()
                val token = provider.currentToken()
                Log.d("FCM", "Token: $token")
            }
        }
        setContent {
            App()
        }
    }
}
