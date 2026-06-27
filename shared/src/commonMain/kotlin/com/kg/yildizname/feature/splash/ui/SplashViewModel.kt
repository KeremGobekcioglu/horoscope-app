package com.kg.yildizname.feature.splash.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kg.yildizname.core.data.prefs.UserPreferencesDataSource
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
            if (prefs.isOnboardingComplete()) {
                _events.send(SplashEvent.NavigateToHome)
            } else {
                _events.send(SplashEvent.NavigateToOnboarding)
            }
        }
    }
}
