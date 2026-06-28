package com.kg.yildizname.platform

expect class ForegroundObserver(onForeground: () -> Unit) {
    fun start()
    fun stop()
}
