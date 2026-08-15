package com.kg.yildizname;

import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.browser.customtabs.CustomTabsIntent
import com.kg.yildizname.platform.UrlOpener

class AndroidUrlOpener(
    private val activity: ComponentActivity
) : UrlOpener
{
    override fun open(url: String) {
        CustomTabsIntent.Builder().build().launchUrl(activity, Uri.parse(url))
    }
}