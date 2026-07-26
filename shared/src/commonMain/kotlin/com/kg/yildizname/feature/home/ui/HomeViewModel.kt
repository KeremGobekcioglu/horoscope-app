package com.kg.yildizname.feature.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kg.yildizname.core.data.model.PeriodType
import com.kg.yildizname.core.data.model.ZodiacSign
import com.kg.yildizname.core.data.repository.HoroscopeRepository
import com.kg.yildizname.core.data.repository.UserRepository
import com.kg.yildizname.core.domain.usecase.GetDailyReadingUseCase
import com.kg.yildizname.core.ui.utils.DateFormatter
import com.kg.yildizname.core.util.DateUtils
import com.kg.yildizname.platform.ForegroundObserver
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class HomeViewModel(
    private val remoteRepository: HoroscopeRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var lastFetchedDate: String? = null
    private var lastFetchedSign: ZodiacSign? = null
    private val foregroundObserver = ForegroundObserver { onForeground() }

    init {
        //foregroundObserver.start()
        fetchWhenSignChangesToo()
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
            getReading(sign)
        }
    }
    suspend fun getReading(sign: ZodiacSign)
    {
        try {

            remoteRepository.getReading(sign, PeriodType.DAILY, DateUtils.today())
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
    @OptIn(ExperimentalCoroutinesApi::class)
    fun fetchWhenSignChangesToo()
    {
        userRepository.getSignFlow()
            .map { it ?: ZodiacSign.SCORPIO }
            .distinctUntilChanged()
            .onEach { _uiState.value = HomeUiState.Loading }
            .flatMapLatest {
                sign ->
                _uiState.value = HomeUiState.Loading
                remoteRepository.getReading(sign, PeriodType.DAILY, DateUtils.today())
                    .map {
                        reading ->
                        HomeUiState.Success(
                            reading = reading,
                            todayLabel = DateFormatter.fullDate(DateUtils.todayLocalDate()),
                        ) as HomeUiState
                    }
                    .catch { e -> emit(HomeUiState.Error(e.message ?: "error")) }
            }
            .onEach { _uiState.value = it }
            .launchIn(viewModelScope)
    }
    fun retry() {
        lastFetchedDate = null   // reset so fetchIfNeeded() doesn't short-circuit
        fetchIfNeeded()
    }
}