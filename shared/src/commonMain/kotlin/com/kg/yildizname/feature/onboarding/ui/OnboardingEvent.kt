package com.kg.yildizname.feature.onboarding.ui

sealed interface OnboardingEvent {
    data object NavigateToStep2 : OnboardingEvent
    data object NavigateToStep3 : OnboardingEvent
    data object NavigateToHome  : OnboardingEvent
}
