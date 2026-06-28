package com.kg.yildizname.core.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class FirestoreReadingDto(
    val text: String = "",
    val scoreLove: Int = 0,
    val scoreWork: Int = 0,
    val scoreHealth: Int = 0,
    val scoreLuck: Int = 0,
)
