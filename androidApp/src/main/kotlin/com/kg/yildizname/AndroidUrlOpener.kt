package com.kg.yildizname;

import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import com.kg.yildizname.platform.UrlOpener

class AndroidUrlOpener(
    private val activity: ComponentActivity
) : UrlOpener
{
    override fun open(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        activity.startActivity(intent)
    }
}