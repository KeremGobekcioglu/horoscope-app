package com.kg.yildizname.platform

enum class PermissionStatus { NOT_DETERMINED, DENIED, GRANTED }

interface NotificationPermissionRequester {
    suspend fun requestPermission(): Boolean
    suspend fun currentStatus(): PermissionStatus
}

interface NotificationSettingsOpener {
    fun open()
}