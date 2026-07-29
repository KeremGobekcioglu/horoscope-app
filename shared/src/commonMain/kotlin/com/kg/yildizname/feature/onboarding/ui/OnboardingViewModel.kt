package com.kg.yildizname.feature.onboarding.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kg.yildizname.core.data.prefs.UserPreferencesDataSource
import com.kg.yildizname.core.data.model.ZodiacSign
import com.kg.yildizname.feature.onboarding.BirthDate
import com.kg.yildizname.feature.onboarding.OnboardingOptionalData
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
            try {
                prefs.saveZodiacSign(sign.apiKey)
                prefs.markOnboardingComplete()
                _events.send(OnboardingEvent.NavigateToHome)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }

    fun setBirthDate(date: BirthDate) {
        _uiState.update { it.copy(birthDate = date) }
    }

    fun confirmBirthDate() {
        val date = _uiState.value.birthDate
        viewModelScope.launch {
            try {
                date?.let { prefs.saveBirthDate(it.day, it.month, it.year) }
                _events.send(OnboardingEvent.NavigateToStep3)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }

    fun skipBirthDate() {
        viewModelScope.launch {
            _events.send(OnboardingEvent.NavigateToStep3)
        }
    }

    fun setOptionalData(data: OnboardingOptionalData) {
        _uiState.update { it.copy(optionalData = data) }
    }

    fun completeOnboarding() {
        val data = _uiState.value.optionalData
        viewModelScope.launch {
            try {
                data.birthTime?.let { prefs.saveBirthTime(it) }
                data.birthCity?.let { prefs.saveBirthCity(it) }
                data.gender?.let { prefs.saveGender(it.name) }
                prefs.markOnboardingComplete()
                _events.send(OnboardingEvent.NavigateToHome)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }

    fun skipOptionalData() {
        viewModelScope.launch {
            try {
                prefs.markOnboardingComplete()
                _events.send(OnboardingEvent.NavigateToHome)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }
    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
