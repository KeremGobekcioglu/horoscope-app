package com.kg.yildizname.feature.compatability.ui.CompatibilityResult

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kg.yildizname.core.data.model.CompatibilityResult
import com.kg.yildizname.core.data.model.ZodiacSign
import com.kg.yildizname.core.data.repository.CompatibilityRepository
import com.kg.yildizname.feature.calendar.ui.CalendarUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CompatibilityResultViewModel(
    private val repository: CompatibilityRepository,
    private val signA: String,
    private val signB: String
) : ViewModel()
{
    // this result screen will fetch data.
    // inital is loading since it ll fetch data from remote source.
    private val _uiState = MutableStateFlow<CompatibilityResultUIState>(CompatibilityResultUIState.Loading)
    val uiState : StateFlow<CompatibilityResultUIState> = _uiState.asStateFlow()

    init {
        fetchResults()
    }
    private fun fetchResults() {
        val zodiacSignA = ZodiacSign.fromKey(signA)
        val zodiacSignB = ZodiacSign.fromKey(signB)
        viewModelScope.launch {
            try {
                /*
                * repository returns a flow. so it cant be directly assigned, it is neeeded to collect.
                * we use .first() since our data is static already calculated data.
                * i need to check this maybe it should not be flow. it does not return data which can change.
                * */
                val result = repository.getCompatibilityResult(signA = zodiacSignA, signB = zodiacSignB)
                if(result == null)
                {
                    _uiState.value = CompatibilityResultUIState.Error("No compatibility data is found")
                }
                else
                {
                    _uiState.value = CompatibilityResultUIState.Success(result)
                }
            } catch (e: Exception) {
                _uiState.value = CompatibilityResultUIState.Error(e.message ?: "Some error occurred.")
            }
        }
    }
}