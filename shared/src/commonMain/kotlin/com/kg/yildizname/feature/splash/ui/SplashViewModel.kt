package com.kg.yildizname.feature.splash.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kg.yildizname.core.data.prefs.UserPreferencesDataSource
import com.kg.yildizname.core.util.DateUtils
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

sealed interface SplashEvent {
    data object NavigateToHome        : SplashEvent
    data object NavigateToOnboarding  : SplashEvent
}

class SplashViewModel(
    private val prefs: UserPreferencesDataSource,
) : ViewModel() {

    private val _events = Channel<SplashEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun onAnimationDone() {
        viewModelScope.launch {
            val onboardingComplete = prefs.isOnboardingComplete()

            // Seeded here rather than in Home/Onboarding: this runs before both,
            // on every cold start, so it's the earliest universal point to
            // capture the true first-open date regardless of which path a
            // user (new or returning) takes next.
            //
            // Fallback differs by path: a brand-new user (onboarding not yet
            // complete) is genuinely opening the app today. An existing user
            // who already finished onboarding but has no install date yet is
            // just updating into the version that added this key — falling
            // back to today for them would clip calendar history they already
            // had access to, so they fall back to the earliest available date
            // instead.
            val fallback = if (onboardingComplete) DateUtils.earliestAvailableDate else DateUtils.todayLocalDate()
            prefs.getOrCreateInstallDate(fallback)

            if (onboardingComplete) {
                _events.send(SplashEvent.NavigateToHome)
            } else {
                _events.send(SplashEvent.NavigateToOnboarding)
            }
        }
    }
}
