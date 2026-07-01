package com.kg.yildizname.core.util

import com.kg.yildizname.core.data.model.ZodiacSign
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
import horoscope.shared.generated.resources.sign_name_aquarius
import horoscope.shared.generated.resources.sign_name_aries
import horoscope.shared.generated.resources.sign_name_cancer
import horoscope.shared.generated.resources.sign_name_capricorn
import horoscope.shared.generated.resources.sign_name_gemini
import horoscope.shared.generated.resources.sign_name_leo
import horoscope.shared.generated.resources.sign_name_libra
import horoscope.shared.generated.resources.sign_name_pisces
import horoscope.shared.generated.resources.sign_name_sagittarius
import horoscope.shared.generated.resources.sign_name_scorpio
import horoscope.shared.generated.resources.sign_name_taurus
import horoscope.shared.generated.resources.sign_name_virgo
import horoscope.shared.generated.resources.taurus_constellation_icon
import horoscope.shared.generated.resources.virgo_constellation_icon
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

fun ZodiacSign.constellationDrawable(): DrawableResource = when (this) {
    ZodiacSign.ARIES       -> Res.drawable.aries_constellation_icon
    ZodiacSign.TAURUS      -> Res.drawable.taurus_constellation_icon
    ZodiacSign.GEMINI      -> Res.drawable.gemini_constellation_icon
    ZodiacSign.CANCER      -> Res.drawable.cancer_constellation_icon
    ZodiacSign.LEO         -> Res.drawable.leo_constellation_icon
    ZodiacSign.VIRGO       -> Res.drawable.virgo_constellation_icon
    ZodiacSign.LIBRA       -> Res.drawable.libra_constellation_icon
    ZodiacSign.SCORPIO     -> Res.drawable.scorpio_constellation_icon
    ZodiacSign.SAGITTARIUS -> Res.drawable.sagittarius_constellation_icon
    ZodiacSign.CAPRICORN   -> Res.drawable.capricorn_constellation_icon
    ZodiacSign.AQUARIUS    -> Res.drawable.aquarius_constellation_icon
    ZodiacSign.PISCES      -> Res.drawable.pisces_constellation_icon
}

fun ZodiacSign.nameStringResource(): StringResource = when (this) {
    ZodiacSign.ARIES       -> Res.string.sign_name_aries
    ZodiacSign.TAURUS      -> Res.string.sign_name_taurus
    ZodiacSign.GEMINI      -> Res.string.sign_name_gemini
    ZodiacSign.CANCER      -> Res.string.sign_name_cancer
    ZodiacSign.LEO         -> Res.string.sign_name_leo
    ZodiacSign.VIRGO       -> Res.string.sign_name_virgo
    ZodiacSign.LIBRA       -> Res.string.sign_name_libra
    ZodiacSign.SCORPIO     -> Res.string.sign_name_scorpio
    ZodiacSign.SAGITTARIUS -> Res.string.sign_name_sagittarius
    ZodiacSign.CAPRICORN   -> Res.string.sign_name_capricorn
    ZodiacSign.AQUARIUS    -> Res.string.sign_name_aquarius
    ZodiacSign.PISCES      -> Res.string.sign_name_pisces
}
