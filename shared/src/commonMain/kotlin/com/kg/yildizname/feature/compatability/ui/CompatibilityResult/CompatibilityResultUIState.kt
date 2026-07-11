package com.kg.yildizname.feature.compatability.ui.CompatibilityResult

import com.kg.yildizname.core.data.model.CompatibilityResult
import com.kg.yildizname.core.data.model.ZodiacSign

sealed interface CompatibilityResultUIState
{
    data object Loading : CompatibilityResultUIState
    data class Success(
        val result : CompatibilityResult
        ) : CompatibilityResultUIState
    data class Error(val message: String) : CompatibilityResultUIState
}