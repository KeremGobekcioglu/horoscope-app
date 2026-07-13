package com.kg.yildizname.feature.readingDetail.ui

import com.kg.yildizname.core.data.model.ZodiacSign

data class ReadingDetailUiState(
    val sign: ZodiacSign = ZodiacSign.SAGITTARIUS,
    val signDisplayName: String = "",
    val periodLabel: String= "",
    val luckyNumber: Int = 0,
    val luckyColorName: String= "",
    val generalText: String= "",
    val loveText: String= "",
    val careerText: String= "",
    val healthText: String= "",
    val luckText: String= "",
    val err: String? = null,
    val isLoading: Boolean = true
)
