package com.kg.yildizname.platform


import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asSkiaBitmap
import org.jetbrains.skia.EncodedImageFormat
import org.jetbrains.skia.Image

actual fun ImageBitmap.toPngBytes(): ByteArray {
    val skiaImage = Image.makeFromBitmap(this.asSkiaBitmap())
    val encoded = skiaImage.encodeToData(EncodedImageFormat.PNG)
        ?: error("Skia PNG encoding failed")
    return encoded.bytes
}