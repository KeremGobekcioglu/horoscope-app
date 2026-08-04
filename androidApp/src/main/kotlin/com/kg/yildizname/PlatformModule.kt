package com.kg.yildizname

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.kg.yildizname.core.data.remote.AndroidPushTokenProvider
import com.kg.yildizname.core.data.remote.PushTokenProvider
import com.kg.yildizname.platform.AndroidShareManager
import com.kg.yildizname.platform.ShareManager
import okio.Path.Companion.toPath
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val androidPlatformModule = module {
    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.createWithPath(
            produceFile = {
                androidContext()
                    .filesDir
                    .resolve("user_prefs.preferences_pb")
                    .absolutePath
                    .toPath()
            }
        )
    }
    single<PushTokenProvider> { AndroidPushTokenProvider() }
    single<ShareManager> { AndroidShareManager(androidContext()) }
}
