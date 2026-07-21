package com.kg.yildizname.core

import com.kg.yildizname.platform.NotificationPermissionRequester

class IosNotificationPermissionRequester : NotificationPermissionRequester {
    override suspend fun requestPermission(): Boolean =
        swiftNotificationPermissionBridge?.requestPermission() ?: false
}