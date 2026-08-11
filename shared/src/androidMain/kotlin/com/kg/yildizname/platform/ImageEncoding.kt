package com.kg.yildizname.platform

import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import java.io.ByteArrayOutputStream

actual fun ImageBitmap.toPngBytes(): ByteArray {
    var bitmap = this.asAndroidBitmap()
    // Bitmap.Config.HARDWARE didn't exist before API 26 — the enum constant itself isn't
    // present in the OS below that, so referencing it unconditionally would crash with
    // NoSuchFieldError on API 24/25 even though nothing about *our* code is wrong there.
    // GraphicsLayer capture can only hand back a HARDWARE bitmap on API 26+ in the first
    // place, so skipping this check below 26 isn't a gap — HARDWARE literally can't occur.
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && bitmap.config == Bitmap.Config.HARDWARE) {
        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, false)
    }
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
    return stream.toByteArray()
}