package com.kg.yildizname.core.domain.model


import androidx.compose.runtime.Composable
import horoscope.shared.generated.resources.Res
import horoscope.shared.generated.resources.compat_band_excellent
import horoscope.shared.generated.resources.compat_band_strong
import horoscope.shared.generated.resources.compat_band_balanced
import horoscope.shared.generated.resources.compat_band_variable
import horoscope.shared.generated.resources.compat_band_challenging
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

enum class CompatibilityBand(val descRes: StringResource) {
    EXCELLENT(Res.string.compat_band_excellent),
    STRONG(Res.string.compat_band_strong),
    BALANCED(Res.string.compat_band_balanced),
    VARIABLE(Res.string.compat_band_variable),
    CHALLENGING(Res.string.compat_band_challenging);

    companion object {
        fun fromScore(matchPercent: Int): CompatibilityBand = when {
            matchPercent >= 90 -> EXCELLENT
            matchPercent >= 75 -> STRONG
            matchPercent >= 60 -> BALANCED
            matchPercent >= 40 -> VARIABLE
            else -> CHALLENGING
        }
    }
}

@Composable
fun CompatibilityBand.localizedDesc(): String = stringResource(descRes)