package com.kg.yildizname.core.data.remote

import com.kg.yildizname.core.data.model.PeriodType
import com.kg.yildizname.core.data.model.ZodiacSign
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class HoroscopeApiSource(private val client: HttpClient) {

    private val baseUrl = "https://freehoroscopeapi.com/api/v1/get-horoscope"

    suspend fun getReading(sign: ZodiacSign, period: PeriodType): HoroscopeApiDto? =
        client.get("$baseUrl/${period.apiKey}") {
            parameter("sign", sign.apiKey)
        }.body<HoroscopeApiResponse>().data
}