package com.kg.yildizname.core.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class HoroscopeApiResponse(val data: HoroscopeApiDto)

@Serializable
data class HoroscopeApiDto(
    val date: String,
    val period: String,
    val sign: String,
    val horoscope: String,
)
