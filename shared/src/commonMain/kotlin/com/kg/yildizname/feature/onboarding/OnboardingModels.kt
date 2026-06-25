package com.kg.yildizname.feature.onboarding

data class BirthDate(val day: Int, val month: Int, val year: Int)

enum class Gender { MALE, FEMALE, OTHER, PREFER_NOT }

data class OnboardingOptionalData(
    val birthTime: String? = null,
    val birthCity: String? = null,
    val gender: Gender? = null,
)
