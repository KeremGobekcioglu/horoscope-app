package com.kg.yildizname.feature.onboarding.ui

import com.kg.yildizname.core.domain.model.ZodiacSign
import com.kg.yildizname.feature.onboarding.BirthDate
import com.kg.yildizname.feature.onboarding.OnboardingOptionalData

data class OnboardingUiState(
    val selectedSign: ZodiacSign? = null,
    val birthDate: BirthDate? = null,
    val optionalData: OnboardingOptionalData = OnboardingOptionalData(),
)
