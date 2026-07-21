package com.kg.yildizname.platform

interface NotificationPermissionRequester
{
    suspend fun requestPermission() : Boolean
}