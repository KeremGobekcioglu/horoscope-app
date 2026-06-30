package com.kg.yildizname.feature.home.ui.components

import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import com.kg.yildizname.core.data.model.ZodiacSign
import com.kg.yildizname.core.ui.theme.YzGold
import com.kg.yildizname.core.ui.theme.YzViolet
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
import org.jetbrains.compose.resources.painterResource

@Composable
internal fun ConstellationHero(
    sign: ZodiacSign,
    modifier: Modifier = Modifier,
) {
    // Resolve PNG resource by sign — drop files as constellation_scorpio.png etc.
    // into composeResources/drawable/
    val painter: Painter = when (sign) {
        ZodiacSign.ARIES        -> painterResource(Res.drawable.aries_constellation_icon)
        ZodiacSign.TAURUS       -> painterResource(Res.drawable.taurus_constellation_icon)
        ZodiacSign.GEMINI       -> painterResource(Res.drawable.gemini_constellation_icon)
        ZodiacSign.CANCER       -> painterResource(Res.drawable.cancer_constellation_icon)
        ZodiacSign.LEO          -> painterResource(Res.drawable.leo_constellation_icon)
        ZodiacSign.VIRGO        -> painterResource(Res.drawable.virgo_constellation_icon)
        ZodiacSign.LIBRA        -> painterResource(Res.drawable.libra_constellation_icon)
        ZodiacSign.SCORPIO      -> painterResource(Res.drawable.scorpio_constellation_icon)
        ZodiacSign.SAGITTARIUS  -> painterResource(Res.drawable.sagittarius_constellation_icon)
        ZodiacSign.CAPRICORN    -> painterResource(Res.drawable.capricorn_constellation_icon)
        ZodiacSign.AQUARIUS     -> painterResource(Res.drawable.aquarius_constellation_icon)
        ZodiacSign.PISCES       -> painterResource(Res.drawable.pisces_constellation_icon)
    }

    val glowTransition = rememberInfiniteTransition(label = "heroGlow")
    val glowAlpha by glowTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "hero_glow_alpha"
    )

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        // Soft gold/violet halo behind the constellation — drawn oversized and unclipped
        // so it bleeds past the image bounds instead of looking boxed-in.
        Box(
            modifier = Modifier
                .matchParentSize()
                .drawBehind {
                    val glowRadius = size.minDimension * 0.68f
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                YzGold.copy(alpha = glowAlpha * 0.22f),
                                YzViolet.copy(alpha = glowAlpha * 0.12f),
                                Color.Transparent
                            ),
                            radius = glowRadius
                        ),
                        radius = glowRadius
                    )
                }
        )
        Image(
            painter = painter,
            contentDescription = sign.apiKey,
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize()
        )
    }
}
