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
) : ViewModel()
{
    // this result screen will fetch data.
    // inital is loading since it ll fetch data from remote source.
    private val _uiState = MutableStateFlow<CompatibilityResultUIState>(CompatibilityResultUIState.Loading)
    val uiState : StateFlow<CompatibilityResultUIState> = _uiState.asStateFlow()

    // this ViewModel is shared (graph-scoped) between the result and detail screens,
    // so picking a new pair on the selection screen re-enters the same instance.
    // Track the last-loaded pair to avoid refetching when the detail screen re-observes
    // with the same signs, while still reloading when the user picks a new pair.
    private var loadedSignA: String? = null
    private var loadedSignB: String? = null

    fun loadResult(signA: String, signB: String) {
        if (loadedSignA == signA && loadedSignB == signB) return
        loadedSignA = signA
        loadedSignB = signB
        fetchResults(signA, signB)
    }

    private fun fetchResults(signA: String, signB: String) {
        val zodiacSignA = ZodiacSign.fromKey(signA)
        val zodiacSignB = ZodiacSign.fromKey(signB)
        _uiState.value = CompatibilityResultUIState.Loading
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