package com.kg.yildizname.feature.home.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kg.yildizname.core.data.model.ZodiacSign
import com.kg.yildizname.core.ui.theme.YzGold
import com.kg.yildizname.core.ui.theme.YzMuted
import horoscope.shared.generated.resources.Res
import horoscope.shared.generated.resources.sign_dates_aquarius
import horoscope.shared.generated.resources.sign_dates_aries
import horoscope.shared.generated.resources.sign_dates_cancer
import horoscope.shared.generated.resources.sign_dates_capricorn
import horoscope.shared.generated.resources.sign_dates_gemini
import horoscope.shared.generated.resources.sign_dates_leo
import horoscope.shared.generated.resources.sign_dates_libra
import horoscope.shared.generated.resources.sign_dates_pisces
import horoscope.shared.generated.resources.sign_dates_sagittarius
import horoscope.shared.generated.resources.sign_dates_scorpio
import horoscope.shared.generated.resources.sign_dates_taurus
import horoscope.shared.generated.resources.sign_dates_virgo
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
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun SignHeader(
    sign: ZodiacSign,
    modifier: Modifier = Modifier,
) {
    val signNameRes = when (sign) {
        ZodiacSign.ARIES        -> Res.string.sign_name_aries
        ZodiacSign.TAURUS       -> Res.string.sign_name_taurus
        ZodiacSign.GEMINI       -> Res.string.sign_name_gemini
        ZodiacSign.CANCER       -> Res.string.sign_name_cancer
        ZodiacSign.LEO          -> Res.string.sign_name_leo
        ZodiacSign.VIRGO        -> Res.string.sign_name_virgo
        ZodiacSign.LIBRA        -> Res.string.sign_name_libra
        ZodiacSign.SCORPIO      -> Res.string.sign_name_scorpio
        ZodiacSign.SAGITTARIUS  -> Res.string.sign_name_sagittarius
        ZodiacSign.CAPRICORN    -> Res.string.sign_name_capricorn
        ZodiacSign.AQUARIUS     -> Res.string.sign_name_aquarius
        ZodiacSign.PISCES       -> Res.string.sign_name_pisces
    }

    val dateRangeRes = when (sign) {
        ZodiacSign.ARIES        -> Res.string.sign_dates_aries
        ZodiacSign.TAURUS       -> Res.string.sign_dates_taurus
        ZodiacSign.GEMINI       -> Res.string.sign_dates_gemini
        ZodiacSign.CANCER       -> Res.string.sign_dates_cancer
        ZodiacSign.LEO          -> Res.string.sign_dates_leo
        ZodiacSign.VIRGO        -> Res.string.sign_dates_virgo
        ZodiacSign.LIBRA        -> Res.string.sign_dates_libra
        ZodiacSign.SCORPIO      -> Res.string.sign_dates_scorpio
        ZodiacSign.SAGITTARIUS  -> Res.string.sign_dates_sagittarius
        ZodiacSign.CAPRICORN    -> Res.string.sign_dates_capricorn
        ZodiacSign.AQUARIUS     -> Res.string.sign_dates_aquarius
        ZodiacSign.PISCES       -> Res.string.sign_dates_pisces
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = stringResource(signNameRes).uppercase(),
            color = YzGold,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 5.sp,
            // Uses YzTypography display/serif family — ensure it's applied at theme level
        )
        Text(
            text = stringResource(dateRangeRes).uppercase(),
            color = YzMuted,
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            letterSpacing = 1.5.sp
        )
    }
}
