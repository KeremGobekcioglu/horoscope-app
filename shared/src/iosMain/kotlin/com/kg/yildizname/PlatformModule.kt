package com.kg.yildizname

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.kg.yildizname.core.IosNotificationPermissionRequester
import com.kg.yildizname.core.IosNotificationSettingsOpener
import com.kg.yildizname.core.data.remote.PushTokenProvider
import com.kg.yildizname.core.data.remote.iOSPushTokenProvider
import com.kg.yildizname.platform.NotificationPermissionRequester
import com.kg.yildizname.platform.NotificationSettingsOpener
import okio.Path.Companion.toPath
import org.koin.dsl.module
import platform.Foundation.NSHomeDirectory

val iosPlatformModule = module {
    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.createWithPath(
            produceFile = {
                (NSHomeDirectory() + "/Documents/user_prefs.preferences_pb").toPath()
            }
        )
    }
    single<PushTokenProvider> { iOSPushTokenProvider() }
    single<NotificationPermissionRequester> { IosNotificationPermissionRequester() }
    single<NotificationSettingsOpener> { IosNotificationSettingsOpener() }
}
