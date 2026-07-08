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
import org.jetbrains.compose.resources.painterResource

@Composable
internal fun ConstellationHero(
    sign: ZodiacSign,
    modifier: Modifier = Modifier,
) {
    // Resolve PNG resource by sign — drop files as constellation_scorpio.png etc.
    // into composeResources/drawable/
    val painter: Painter = painterResource(sign.drawable)

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
