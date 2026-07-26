package com.kg.yildizname

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.kg.yildizname.R
import com.kg.yildizname.core.util.LanguagePrefsMirror
import com.kg.yildizname.di.appModule
import com.kg.yildizname.di.databaseModule
import com.kg.yildizname.di.domainModule
import com.kg.yildizname.di.networkModule
import com.kg.yildizname.di.repositoryModule
import com.kg.yildizname.platform.AndroidNotificationPermissionRequester
import com.kg.yildizname.platform.NotificationPermissionRequester
import com.kg.yildizname.platform.NotificationSettingsOpener
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.mp.KoinPlatformTools
import java.util.Locale

class MainActivity : ComponentActivity() {
    // Runs before onCreate/Koin startup, so the stored language is read from the
    // synchronous LanguagePrefsMirror rather than DataStore. Defaults to Turkish.
    override fun attachBaseContext(newBase: Context) {
        LanguagePrefsMirror.init(newBase)
        val lang = LanguagePrefsMirror.read() ?: "tr"
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration(newBase.resources.configuration)
        config.setLocale(locale)
        super.attachBaseContext(newBase.createConfigurationContext(config))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
        )
        val permissionRequester = AndroidNotificationPermissionRequester(this)
        val settingsIntent = AndroidNotificationsSettingsOpener(this)
        if (KoinPlatformTools.defaultContext().getOrNull() == null) {
            startKoin {
                androidContext(applicationContext)
                modules(
                    appModule,
                    androidPlatformModule,
                    databaseModule,
                    networkModule,
                    repositoryModule,
                    domainModule,
                    module { single<NotificationPermissionRequester> { permissionRequester } },
                    module { single<NotificationSettingsOpener> { settingsIntent } }
                )
            }
        }
        setContent {
            App()
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                getString(R.string.notification_channel_daily_readings_name),
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = getString(R.string.notification_channel_daily_readings_description)
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private companion object {
        const val NOTIFICATION_CHANNEL_ID = "daily_readings"
    }
}
