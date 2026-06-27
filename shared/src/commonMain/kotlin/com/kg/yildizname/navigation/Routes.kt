package com.kg.yildizname.navigation

import kotlinx.serialization.Serializable

@Serializable object Splash
@Serializable object OnboardingGraph
@Serializable object OnboardingStep1
@Serializable object OnboardingStep2
@Serializable object OnboardingStep3
@Serializable object Home
@Serializable data class ReadingDetail(val sign: String, val period: String)
@Serializable object Calendar
@Serializable object Compatibility
@Serializable object Settings
