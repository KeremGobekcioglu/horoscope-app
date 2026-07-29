package com.kg.yildizname.core

import com.kg.yildizname.platform.NotificationPermissionRequester
import com.kg.yildizname.platform.NotificationSettingsOpener
import com.kg.yildizname.platform.PermissionStatus
import com.kg.yildizname.platform.UrlOpener

class IosNotificationPermissionRequester : NotificationPermissionRequester {
    override suspend fun requestPermission(): Boolean =
        swiftNotificationPermissionBridge?.requestPermission() ?: false

    override suspend fun currentStatus(): PermissionStatus =
        when (swiftNotificationPermissionBridge?.currentStatus()) {
            2 -> PermissionStatus.GRANTED
            1 -> PermissionStatus.DENIED
            else -> PermissionStatus.NOT_DETERMINED
        }
}

class IosNotificationSettingsOpener : NotificationSettingsOpener {
    override fun open() { swiftSettingsOpenerBridge?.open() }
}

class IosUrlOpener : UrlOpener {
    override fun open(url: String) { swiftUrlOpenerBridge?.open(url) }
}