package com.kg.yildizname.feature.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kg.yildizname.core.data.prefs.UserPreferencesDataSource
import com.kg.yildizname.core.domain.model.ZodiacSigns
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface HomeEvent {
    data object NavigateToOnboarding : HomeEvent
}

class HomeViewModel(
    private val prefs: UserPreferencesDataSource,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _events = Channel<HomeEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        viewModelScope.launch { loadPrefs() }
    }

    private suspend fun loadPrefs() {
        val signKey = prefs.getZodiacSign()
        _uiState.update {
            it.copy(
                isLoading  = false,
                zodiacSign = ZodiacSigns.find { s -> s.key == signKey },
                birthDay   = prefs.getBirthDay(),
                birthMonth = prefs.getBirthMonth(),
                birthYear  = prefs.getBirthYear(),
                birthTime  = prefs.getBirthTime(),
                birthCity  = prefs.getBirthCity(),
                gender     = prefs.getGender(),
            )
        }
    }

    fun clearOnboarding() {
        viewModelScope.launch {
            prefs.clearAll()
            _events.send(HomeEvent.NavigateToOnboarding)
        }
    }
}
