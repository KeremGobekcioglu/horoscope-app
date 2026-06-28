package com.kg.yildizname.platform

import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSOperationQueue
import platform.UIKit.UIApplicationDidBecomeActiveNotification

actual class ForegroundObserver actual constructor(
    private val onForeground: () -> Unit,
) {
    private var observer: Any? = null

    actual fun start() {
        observer = NSNotificationCenter.defaultCenter.addObserverForName(
            name = UIApplicationDidBecomeActiveNotification,
            `object` = null,
            queue = NSOperationQueue.mainQueue,
        ) { _ -> onForeground() }
    }

    actual fun stop() {
        observer?.let { NSNotificationCenter.defaultCenter.removeObserver(it) }
        observer = null
    }
}
