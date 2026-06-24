package com.kg.yildizname.core.domain.model

import org.jetbrains.compose.resources.DrawableResource
import horoscope.shared.generated.resources.Res
import horoscope.shared.generated.resources.aquarius_constellation_icon
import horoscope.shared.generated.resources.aries_constellation_icon
import horoscope.shared.generated.resources.cancer_constellation_icon
import horoscope.shared.generated.resources.capricorn_constellation_icon
import horoscope.shared.generated.resources.gemini_constellation_icon
import horoscope.shared.generated.resources.leo_constellation_icon
import horoscope.shared.generated.resources.libra_constellation_icon
import horoscope.shared.generated.resources.pisces_constellation_icon
import horoscope.shared.generated.resources.sagittarius_constellation_icon
import horoscope.shared.generated.resources.scorpio_constellation_icon
import horoscope.shared.generated.resources.taurus_constellation_icon
import horoscope.shared.generated.resources.virgo_constellation_icon

data class ZodiacSign(
    val key: String,
    val nameTr: String,
    val dateRange: String,
    val drawable: DrawableResource,
)

val ZodiacSigns = listOf(
    ZodiacSign("koc",     "Koç",     "21 Mar - 19 Nis", Res.drawable.aries_constellation_icon),
    ZodiacSign("boga",    "Boğa",    "20 Nis - 20 May", Res.drawable.taurus_constellation_icon),
    ZodiacSign("ikizler", "İkizler", "21 May - 20 Haz", Res.drawable.gemini_constellation_icon),
    ZodiacSign("yengec",  "Yengeç",  "21 Haz - 22 Tem", Res.drawable.cancer_constellation_icon),
    ZodiacSign("aslan",   "Aslan",   "23 Tem - 22 Ağu", Res.drawable.leo_constellation_icon),
    ZodiacSign("basak",   "Başak",   "23 Ağu - 22 Eyl", Res.drawable.virgo_constellation_icon),
    ZodiacSign("terazi",  "Terazi",  "23 Eyl - 22 Eki", Res.drawable.libra_constellation_icon),
    ZodiacSign("akrep",   "Akrep",   "23 Eki - 21 Kas", Res.drawable.scorpio_constellation_icon),
    ZodiacSign("yay",     "Yay",     "22 Kas - 21 Ara", Res.drawable.sagittarius_constellation_icon),
    ZodiacSign("oglak",   "Oğlak",   "22 Ara - 19 Oca", Res.drawable.capricorn_constellation_icon),
    ZodiacSign("kova",    "Kova",    "20 Oca - 18 Şub", Res.drawable.aquarius_constellation_icon),
    ZodiacSign("balik",   "Balık",   "19 Şub - 20 Mar", Res.drawable.pisces_constellation_icon),
)
