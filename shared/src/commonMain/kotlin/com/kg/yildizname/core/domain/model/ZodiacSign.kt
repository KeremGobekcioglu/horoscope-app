package com.kg.yildizname.core.domain.model

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
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
import horoscope.shared.generated.resources.sign_name_aries
import horoscope.shared.generated.resources.sign_name_taurus
import horoscope.shared.generated.resources.sign_name_gemini
import horoscope.shared.generated.resources.sign_name_cancer
import horoscope.shared.generated.resources.sign_name_leo
import horoscope.shared.generated.resources.sign_name_virgo
import horoscope.shared.generated.resources.sign_name_libra
import horoscope.shared.generated.resources.sign_name_scorpio
import horoscope.shared.generated.resources.sign_name_sagittarius
import horoscope.shared.generated.resources.sign_name_capricorn
import horoscope.shared.generated.resources.sign_name_aquarius
import horoscope.shared.generated.resources.sign_name_pisces
import horoscope.shared.generated.resources.sign_dates_aries
import horoscope.shared.generated.resources.sign_dates_taurus
import horoscope.shared.generated.resources.sign_dates_gemini
import horoscope.shared.generated.resources.sign_dates_cancer
import horoscope.shared.generated.resources.sign_dates_leo
import horoscope.shared.generated.resources.sign_dates_virgo
import horoscope.shared.generated.resources.sign_dates_libra
import horoscope.shared.generated.resources.sign_dates_scorpio
import horoscope.shared.generated.resources.sign_dates_sagittarius
import horoscope.shared.generated.resources.sign_dates_capricorn
import horoscope.shared.generated.resources.sign_dates_aquarius
import horoscope.shared.generated.resources.sign_dates_pisces

data class ZodiacSign(
    val key: String,
    val nameRes: StringResource,
    val dateRangeRes: StringResource,
    val drawable: DrawableResource,
    val startDay: Int,
    val startMonth: Int,
)

val ZodiacSigns = listOf(
    ZodiacSign("koc",     Res.string.sign_name_aries,       Res.string.sign_dates_aries,       Res.drawable.aries_constellation_icon,       startDay = 21, startMonth = 3),
    ZodiacSign("boga",    Res.string.sign_name_taurus,      Res.string.sign_dates_taurus,      Res.drawable.taurus_constellation_icon,      startDay = 20, startMonth = 4),
    ZodiacSign("ikizler", Res.string.sign_name_gemini,      Res.string.sign_dates_gemini,      Res.drawable.gemini_constellation_icon,      startDay = 21, startMonth = 5),
    ZodiacSign("yengec",  Res.string.sign_name_cancer,      Res.string.sign_dates_cancer,      Res.drawable.cancer_constellation_icon,      startDay = 21, startMonth = 6),
    ZodiacSign("aslan",   Res.string.sign_name_leo,         Res.string.sign_dates_leo,         Res.drawable.leo_constellation_icon,         startDay = 23, startMonth = 7),
    ZodiacSign("basak",   Res.string.sign_name_virgo,       Res.string.sign_dates_virgo,       Res.drawable.virgo_constellation_icon,       startDay = 23, startMonth = 8),
    ZodiacSign("terazi",  Res.string.sign_name_libra,       Res.string.sign_dates_libra,       Res.drawable.libra_constellation_icon,       startDay = 23, startMonth = 9),
    ZodiacSign("akrep",   Res.string.sign_name_scorpio,     Res.string.sign_dates_scorpio,     Res.drawable.scorpio_constellation_icon,     startDay = 23, startMonth = 10),
    ZodiacSign("yay",     Res.string.sign_name_sagittarius, Res.string.sign_dates_sagittarius, Res.drawable.sagittarius_constellation_icon, startDay = 22, startMonth = 11),
    ZodiacSign("oglak",   Res.string.sign_name_capricorn,   Res.string.sign_dates_capricorn,   Res.drawable.capricorn_constellation_icon,   startDay = 22, startMonth = 12),
    ZodiacSign("kova",    Res.string.sign_name_aquarius,    Res.string.sign_dates_aquarius,    Res.drawable.aquarius_constellation_icon,    startDay = 20, startMonth = 1),
    ZodiacSign("balik",   Res.string.sign_name_pisces,      Res.string.sign_dates_pisces,      Res.drawable.pisces_constellation_icon,      startDay = 19, startMonth = 2),
)

@Composable
fun ZodiacSign.localizedName(): String = stringResource(nameRes)

@Composable
fun ZodiacSign.localizedDateRange(): String = stringResource(dateRangeRes)
