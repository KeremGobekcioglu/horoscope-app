package com.kg.horoscope

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform