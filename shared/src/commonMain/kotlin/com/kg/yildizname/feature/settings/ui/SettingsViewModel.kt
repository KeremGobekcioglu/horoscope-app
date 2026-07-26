package com.kg.yildizname.feature.settings.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kg.yildizname.core.data.model.ZodiacSign
import com.kg.yildizname.core.data.repository.UserRepository
import com.kg.yildizname.platform.NotificationPermissionRequester
import com.kg.yildizname.platform.NotificationSettingsOpener
import com.kg.yildizname.platform.PermissionStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val notificationPermissionRequester: NotificationPermissionRequester,
    private val settingsOpener: NotificationSettingsOpener,
    private val userRepository: UserRepository
) : ViewModel()
{
    private val _state = MutableStateFlow(SettingsState())
    val state : StateFlow<SettingsState> = _state.asStateFlow()
    init {
        refreshNotificationStatus()
    }
    fun refreshNotificationStatus()
    {
        viewModelScope.launch {
            val sign = userRepository.getSavedSign() ?: ZodiacSign.SCORPIO
            println("sign = $sign")
            val granted = notificationPermissionRequester.currentStatus() == PermissionStatus.GRANTED
            _state.update {
                it.copy(
                    sign = sign,
                    notificationsEnabled = granted
                )
            }
        }
    }

    fun updateSign(sign: ZodiacSign)
    {
        viewModelScope.launch {
            try {
                userRepository.saveSign(sign)
                _state.update { it.copy(
                    sign = sign
                ) }
            }
            catch (e: Exception)
            {
                println("updateSign exception = ${e.message}")
            }
        }
    }
    // Tapping the switch — either direction — just routes to system Settings,
    // where the OS is the real source of truth. We don't try to set state here;
    // refreshNotificationStatus() on resume will reflect whatever they chose.
    fun onNotificationsSwitchTapped()
    {
        settingsOpener.open()
    }
}