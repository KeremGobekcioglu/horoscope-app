package com.kg.yildizname.feature.settings.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kg.yildizname.core.data.model.ZodiacSign
import com.kg.yildizname.core.data.repository.UserRepository
import com.kg.yildizname.core.domain.usecase.ResetAppDataUseCase
import com.kg.yildizname.core.util.applyLanguage
import com.kg.yildizname.platform.NotificationPermissionRequester
import com.kg.yildizname.platform.NotificationSettingsOpener
import com.kg.yildizname.platform.PermissionStatus
import com.kg.yildizname.platform.UrlOpener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val PRIVACY_POLICY_URL = "https://keremgobekcioglu.github.io/yildizname-privacy/"

class SettingsViewModel(
    private val notificationPermissionRequester: NotificationPermissionRequester,
    private val settingsOpener: NotificationSettingsOpener,
    private val userRepository: UserRepository,
    private val resetAppDataUseCase: ResetAppDataUseCase,
    private val urlOpener: UrlOpener
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
            val language = userRepository.getLanguage() ?: "tr"
            _state.update {
                it.copy(
                    sign = sign,
                    notificationsEnabled = granted,
                    currentLanguage = language
                )
            }
        }
    }

    fun onLanguageChange(lang: String) {
        if (lang == _state.value.currentLanguage) return
        viewModelScope.launch {
            userRepository.saveLanguage(lang)
            applyLanguage(lang)
            _state.update { it.copy(currentLanguage = lang, showRestartDialog = true) }
        }
    }

    fun dismissRestartDialog() {
        _state.update { it.copy(showRestartDialog = false) }
    }

    fun onResetDataClick() {
        _state.update { it.copy(showResetDialog = true) }
    }

    fun dismissResetDialog() {
        _state.update { it.copy(showResetDialog = false) }
    }

    // Only the server-side cleanup (FCM token, users/{uid} doc, anonymous auth
    // user) runs here — it's safe on this ViewModel's own viewModelScope since
    // it never touches DataStore/Room. The local wipe (clearLocal()) is
    // deliberately NOT called from here: it has to run after navigation has
    // torn down Home/Calendar's sign-flow collectors, by which point this
    // ViewModel (and viewModelScope) is already cleared. The nav graph kicks
    // it off on an app-scoped coroutine right after navigating — see
    // YildiznameNavGraph's Settings composable.
    fun confirmReset() {
        viewModelScope.launch {
            try {
                resetAppDataUseCase.clearRemote()
                _state.update {
                    it.copy(showResetDialog = false, navigateToOnboarding = true)
                }
            } catch (e: Exception) {
                println("confirmReset exception = ${e.message}")
                _state.update { it.copy(showResetDialog = false, error = e.message) }
            }
        }
    }

    fun onNavigatedToOnboarding() {
        _state.update { it.copy(navigateToOnboarding = false) }
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
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

    fun onPrivacyClick()
    {
        urlOpener.open(PRIVACY_POLICY_URL)
    }
}