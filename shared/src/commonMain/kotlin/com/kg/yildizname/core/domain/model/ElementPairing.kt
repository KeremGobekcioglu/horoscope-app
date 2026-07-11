package com.kg.yildizname.core.domain.model


import androidx.compose.runtime.Composable
import horoscope.shared.generated.resources.Res
import horoscope.shared.generated.resources.compat_elem_fire_fire
import horoscope.shared.generated.resources.compat_elem_earth_earth
import horoscope.shared.generated.resources.compat_elem_air_air
import horoscope.shared.generated.resources.compat_elem_water_water
import horoscope.shared.generated.resources.compat_elem_fire_earth
import horoscope.shared.generated.resources.compat_elem_fire_air
import horoscope.shared.generated.resources.compat_elem_fire_water
import horoscope.shared.generated.resources.compat_elem_earth_air
import horoscope.shared.generated.resources.compat_elem_earth_water
import horoscope.shared.generated.resources.compat_elem_air_water
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

enum class ElementPairing(val descRes: StringResource) {
    FIRE_FIRE(Res.string.compat_elem_fire_fire),
    EARTH_EARTH(Res.string.compat_elem_earth_earth),
    AIR_AIR(Res.string.compat_elem_air_air),
    WATER_WATER(Res.string.compat_elem_water_water),
    FIRE_EARTH(Res.string.compat_elem_fire_earth),
    FIRE_AIR(Res.string.compat_elem_fire_air),
    FIRE_WATER(Res.string.compat_elem_fire_water),
    EARTH_AIR(Res.string.compat_elem_earth_air),
    EARTH_WATER(Res.string.compat_elem_earth_water),
    AIR_WATER(Res.string.compat_elem_air_water);

    companion object {
        fun from(a: Element, b: Element): ElementPairing {
            val pair = setOf(a, b)
            return entries.first { it.matches(pair) }
        }

        private fun ElementPairing.matches(pair: Set<Element>): Boolean = when (this) {
            FIRE_FIRE -> pair == setOf(Element.FIRE)
            EARTH_EARTH -> pair == setOf(Element.EARTH)
            AIR_AIR -> pair == setOf(Element.AIR)
            WATER_WATER -> pair == setOf(Element.WATER)
            FIRE_EARTH -> pair == setOf(Element.FIRE, Element.EARTH)
            FIRE_AIR -> pair == setOf(Element.FIRE, Element.AIR)
            FIRE_WATER -> pair == setOf(Element.FIRE, Element.WATER)
            EARTH_AIR -> pair == setOf(Element.EARTH, Element.AIR)
            EARTH_WATER -> pair == setOf(Element.EARTH, Element.WATER)
            AIR_WATER -> pair == setOf(Element.AIR, Element.WATER)
        }
    }
}

@Composable
fun ElementPairing.localizedDesc(): String = stringResource(descRes)