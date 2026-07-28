package com.kg.yildizname.core

interface SwiftUrlOpenerBridge {
    fun open(url: String)
}
var swiftUrlOpenerBridge: SwiftUrlOpenerBridge? = null