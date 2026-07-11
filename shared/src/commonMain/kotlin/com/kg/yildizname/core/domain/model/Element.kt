package com.kg.yildizname.core.domain.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.kg.yildizname.core.ui.theme.YzAirBlue
import com.kg.yildizname.core.ui.theme.YzEarthGreen
import com.kg.yildizname.core.ui.theme.YzFireOrange
import com.kg.yildizname.core.ui.theme.YzWaterBlue
import horoscope.shared.generated.resources.Res
import horoscope.shared.generated.resources.element_name_air
import horoscope.shared.generated.resources.element_name_earth
import horoscope.shared.generated.resources.element_name_fire
import horoscope.shared.generated.resources.element_name_water
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

enum class Element(val nameRes: StringResource) {
    FIRE(Res.string.element_name_fire),
    EARTH(Res.string.element_name_earth),
    AIR(Res.string.element_name_air),
    WATER(Res.string.element_name_water)
}

@Composable
fun Element.localizedName(): String = stringResource(nameRes)

val Element.tintColor: Color
    get() = when (this) {
        Element.FIRE -> YzFireOrange   // or whatever tokens exist
        Element.EARTH -> YzEarthGreen
        Element.AIR -> YzAirBlue
        Element.WATER -> YzWaterBlue
    }