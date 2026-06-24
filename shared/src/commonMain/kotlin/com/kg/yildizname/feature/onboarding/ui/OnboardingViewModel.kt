package com.kg.yildizname.feature.onboarding.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kg.yildizname.core.data.prefs.UserPreferencesDataSource
import com.kg.yildizname.core.domain.model.ZodiacSign
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val prefs: UserPreferencesDataSource,
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    private val _events = Channel<OnboardingEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun selectSign(sign: ZodiacSign) {
        _uiState.update { it.copy(selectedSign = sign) }
    }

    fun confirmSign() {
        val sign = _uiState.value.selectedSign ?: return
        viewModelScope.launch {
            prefs.saveZodiacSign(sign.key)
            _events.send(OnboardingEvent.NavigateToStep2)
        }
    }
}
