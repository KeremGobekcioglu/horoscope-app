package com.kg.yildizname.core

import com.kg.yildizname.platform.NotificationPermissionRequester

// iosMain
interface SwiftNotificationPermissionBridge {
    suspend fun requestPermission(): Boolean
    suspend fun currentStatus(): Int  // 0 = NOT_DETERMINED, 1 = DENIED, 2 = GRANTED
}

var swiftNotificationPermissionBridge: SwiftNotificationPermissionBridge? = null