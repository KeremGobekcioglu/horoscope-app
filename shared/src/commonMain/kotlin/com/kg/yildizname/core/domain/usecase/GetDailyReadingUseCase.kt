package com.kg.yildizname.core.domain.usecase

import com.kg.yildizname.core.data.model.PeriodType
import com.kg.yildizname.core.data.model.Reading
import com.kg.yildizname.core.data.repository.HoroscopeRepository
import com.kg.yildizname.core.data.model.ZodiacSign
import com.kg.yildizname.core.util.DateUtils
import kotlinx.coroutines.flow.Flow

class GetDailyReadingUseCase(
    private val repository: HoroscopeRepository,
) {
    operator fun invoke(sign: ZodiacSign): Flow<Reading> =
        repository.getReading(sign, PeriodType.DAILY, DateUtils.today())
}
