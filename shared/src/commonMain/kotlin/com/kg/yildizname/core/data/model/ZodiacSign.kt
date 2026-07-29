package com.kg.yildizname.core.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Air
import androidx.compose.material.icons.rounded.Landscape
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.kg.yildizname.core.domain.model.Element
import horoscope.shared.generated.resources.Res
import horoscope.shared.generated.resources.aquarius_constellation_icon
import horoscope.shared.generated.resources.aquarius_svgrepo_com
import horoscope.shared.generated.resources.aries_constellation_icon
import horoscope.shared.generated.resources.aries_svgrepo_com
import horoscope.shared.generated.resources.cancer_constellation_icon
import horoscope.shared.generated.resources.cancer_svgrepo_com
import horoscope.shared.generated.resources.capricorn_constellation_icon
import horoscope.shared.generated.resources.capricorn_svgrepo_com
import horoscope.shared.generated.resources.gemini_constellation_icon
import horoscope.shared.generated.resources.gemini_svgrepo_com
import horoscope.shared.generated.resources.leo_constellation_icon
import horoscope.shared.generated.resources.leo_svgrepo_com
import horoscope.shared.generated.resources.libra_constellation_icon
import horoscope.shared.generated.resources.libra_svgrepo_com
import horoscope.shared.generated.resources.pisces_constellation_icon
import horoscope.shared.generated.resources.pisces_svgrepo_com
import horoscope.shared.generated.resources.sagittarius_constellation_icon
import horoscope.shared.generated.resources.sagittarius_svgrepo_com
import horoscope.shared.generated.resources.scorpio_constellation_icon
import horoscope.shared.generated.resources.scorpio_svgrepo_com
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
import horoscope.shared.generated.resources.taurus_svgrepo_com
import horoscope.shared.generated.resources.virgo_svgrepo_com
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

enum class ZodiacSign(
    val apiKey: String,
    val firestoreKey: String,
    val turkishKey: String,
    val nameRes: StringResource,
    val dateRangeRes: StringResource,
    val drawable: DrawableResource,
    val startDay: Int,
    val startMonth: Int,
    val element: Element,
) {
    ARIES("aries", "aries", "koc", Res.string.sign_name_aries, Res.string.sign_dates_aries, Res.drawable.aries_constellation_icon, startDay = 21, startMonth = 3, element = Element.FIRE),
    TAURUS("taurus", "taurus", "boga", Res.string.sign_name_taurus, Res.string.sign_dates_taurus, Res.drawable.taurus_constellation_icon, startDay = 20, startMonth = 4, element = Element.EARTH),
    GEMINI("gemini", "gemini", "ikizler", Res.string.sign_name_gemini, Res.string.sign_dates_gemini, Res.drawable.gemini_constellation_icon, startDay = 21, startMonth = 5, element = Element.AIR),
    CANCER("cancer", "cancer", "yengec", Res.string.sign_name_cancer, Res.string.sign_dates_cancer, Res.drawable.cancer_constellation_icon, startDay = 21, startMonth = 6, element = Element.WATER),
    LEO("leo", "leo", "aslan", Res.string.sign_name_leo, Res.string.sign_dates_leo, Res.drawable.leo_constellation_icon, startDay = 23, startMonth = 7, element = Element.FIRE),
    VIRGO("virgo", "virgo", "basak", Res.string.sign_name_virgo, Res.string.sign_dates_virgo, Res.drawable.virgo_constellation_icon, startDay = 23, startMonth = 8, element = Element.EARTH),
    LIBRA("libra", "libra", "terazi", Res.string.sign_name_libra, Res.string.sign_dates_libra, Res.drawable.libra_constellation_icon, startDay = 23, startMonth = 9, element = Element.AIR),
    SCORPIO("scorpio", "scorpio", "akrep", Res.string.sign_name_scorpio, Res.string.sign_dates_scorpio, Res.drawable.scorpio_constellation_icon, startDay = 23, startMonth = 10, element = Element.WATER),
    SAGITTARIUS("sagittarius", "sagittarius", "yay", Res.string.sign_name_sagittarius, Res.string.sign_dates_sagittarius, Res.drawable.sagittarius_constellation_icon, startDay = 22, startMonth = 11, element = Element.FIRE),
    CAPRICORN("capricorn", "capricorn", "oglak", Res.string.sign_name_capricorn, Res.string.sign_dates_capricorn, Res.drawable.capricorn_constellation_icon, startDay = 22, startMonth = 12, element = Element.EARTH),
    AQUARIUS("aquarius", "aquarius", "kova", Res.string.sign_name_aquarius, Res.string.sign_dates_aquarius, Res.drawable.aquarius_constellation_icon, startDay = 20, startMonth = 1, element = Element.AIR),
    PISCES("pisces", "pisces", "balik", Res.string.sign_name_pisces, Res.string.sign_dates_pisces, Res.drawable.pisces_constellation_icon, startDay = 19, startMonth = 2, element = Element.WATER);

    companion object {
        // NOTE: matches against apiKey / turkishKey only — does NOT check firestoreKey.
        // This currently works for Firestore data too because firestoreKey == apiKey
        // for every sign (see the enum entries above). If that ever stops being true
        // (e.g. a sign is added/renamed and firestoreKey diverges from apiKey), this
        // will silently fall through to the SCORPIO fallback below instead of matching
        // correctly. Used by CompatibilityMappers.kt / CompatibilityRoomMappers.kt to
        // convert Firestore's `signs: List<String>` into ZodiacSign — if compatibility
        // pairs ever start showing up wrong (e.g. everything resolving to Scorpio),
        // check here first.
        //
        // Falls back to SCORPIO on no match instead of throwing/returning null — a bad
        // or unrecognized key is silently mislabeled rather than surfaced as an error.
        fun fromKey(key: String): ZodiacSign {
            val lower = key.lowercase()
            return entries.firstOrNull { it.apiKey == lower || it.turkishKey == lower } ?: SCORPIO
        }
    }
}

@Composable
fun ZodiacSign.localizedName(): String = stringResource(nameRes)

@Composable
fun ZodiacSign.localizedDateRange(): String = stringResource(dateRangeRes)

@Composable
fun ZodiacSign.localizedElementName(): String = stringResource(element.nameRes)

/** Icon used to represent [this] sign in the compatibility feature's sign picker/button. */
val ZodiacSign.compatGridIcon: DrawableResource
    get() = when (this) {
        ZodiacSign.ARIES -> Res.drawable.aries_svgrepo_com
        ZodiacSign.TAURUS -> Res.drawable.taurus_svgrepo_com
        ZodiacSign.GEMINI -> Res.drawable.gemini_svgrepo_com
        ZodiacSign.CANCER -> Res.drawable.cancer_svgrepo_com
        ZodiacSign.LEO -> Res.drawable.leo_svgrepo_com
        ZodiacSign.VIRGO -> Res.drawable.virgo_svgrepo_com
        ZodiacSign.LIBRA -> Res.drawable.libra_svgrepo_com
        ZodiacSign.SCORPIO -> Res.drawable.scorpio_svgrepo_com
        ZodiacSign.SAGITTARIUS -> Res.drawable.sagittarius_svgrepo_com
        ZodiacSign.CAPRICORN -> Res.drawable.capricorn_svgrepo_com
        ZodiacSign.AQUARIUS -> Res.drawable.aquarius_svgrepo_com
        ZodiacSign.PISCES -> Res.drawable.pisces_svgrepo_com
    }

val ZodiacSign.elementIcon: ImageVector
    get() = element.icon

val Element.icon: ImageVector
    get() = when (this) {
        Element.FIRE -> Icons.Rounded.LocalFireDepartment
        Element.WATER -> Icons.Rounded.WaterDrop
        Element.AIR -> Icons.Rounded.Air
        Element.EARTH -> Icons.Rounded.Landscape
    }