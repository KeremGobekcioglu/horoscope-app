package com.kg.yildizname;

import android.content.Intent
import android.provider.Settings
import androidx.activity.ComponentActivity
import com.kg.yildizname.platform.NotificationSettingsOpener

class AndroidNotificationsSettingsOpener(
    private val activity: ComponentActivity
) : NotificationSettingsOpener

{
    override fun open() {
        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
            putExtra(Settings.EXTRA_APP_PACKAGE, activity.packageName)
        }
        activity.startActivity(intent)
    }

}
