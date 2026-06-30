package com.kg.yildizname.core.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class FirestoreReadingDto(
    val textTr: String = "",
    val textEn: String = "",
    val textLoveTr: String = "",
    val textLoveEn: String = "",
    val textWorkTr: String = "",
    val textWorkEn: String = "",
    val textHealthTr: String = "",
    val textHealthEn: String = "",
    val textLuckTr: String = "",
    val textLuckEn: String = "",
    val scoreLove: Int = 0,
    val scoreWork: Int = 0,
    val scoreHealth: Int = 0,
    val scoreLuck: Int = 0,
)
