package com.kg.yildizname.feature.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kg.yildizname.core.data.model.ZodiacSign
import com.kg.yildizname.core.data.repository.UserRepository
import com.kg.yildizname.core.domain.usecase.GetDailyReadingUseCase
import com.kg.yildizname.core.ui.utils.DateFormatter
import com.kg.yildizname.core.util.DateUtils
import com.kg.yildizname.platform.ForegroundObserver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getDailyReading: GetDailyReadingUseCase,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var lastFetchedDate: String? = null
    private var lastFetchedSign: ZodiacSign? = null
    private val foregroundObserver = ForegroundObserver { onForeground() }

    init {
        foregroundObserver.start()
        fetchIfNeeded()
    }

    override fun onCleared() {
        super.onCleared()
        foregroundObserver.stop()
    }

    private fun onForeground() {
        fetchIfNeeded()
    }

    fun fetchIfNeeded() {
        val today = DateUtils.today()
        viewModelScope.launch {
            val sign = userRepository.getSavedSign() ?: ZodiacSign.SCORPIO
            println("home screen sign = $sign")
            if (lastFetchedDate == today && lastFetchedSign == sign) return@launch
            lastFetchedDate = today
            lastFetchedSign = sign
            _uiState.value = HomeUiState.Loading
            try {

                getDailyReading(sign)
                    .catch { e -> _uiState.value = HomeUiState.Error(e.message ?: "error") }
                    .collect { reading ->
                        _uiState.value = HomeUiState.Success(
                            reading = reading,
                            todayLabel = DateFormatter.fullDate(DateUtils.todayLocalDate()),
                        )
                    }
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(e.message ?: "error")
            }
        }
    }

    fun retry() {
        lastFetchedDate = null   // reset so fetchIfNeeded() doesn't short-circuit
        fetchIfNeeded()
    }
}