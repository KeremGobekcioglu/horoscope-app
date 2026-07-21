package com.kg.yildizname.core

import com.kg.yildizname.platform.NotificationPermissionRequester

// iosMain
interface SwiftNotificationPermissionBridge {
    suspend fun requestPermission(): Boolean
}

var swiftNotificationPermissionBridge: SwiftNotificationPermissionBridge? = null