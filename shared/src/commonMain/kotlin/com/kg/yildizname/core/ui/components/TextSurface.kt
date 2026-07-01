package com.kg.yildizname.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kg.yildizname.core.ui.theme.CardShape
import com.kg.yildizname.core.ui.theme.YzBg

/**
 * Semi-transparent panel that sits between the star canvas and text content.
 * Use for structured/body text blocks where a card boundary is appropriate.
 *
 * @param alpha       Opacity of the backing surface. 0.7–0.8 for body paragraphs,
 *                    0.55–0.65 for short headline/title blocks.
 * @param shape       Corner shape — defaults to [CardShape] (20dp) matching existing cards.
 * @param innerPadding Padding applied inside the surface, around the content.
 */
fun Modifier.yzTextSurfaceCard(
    alpha: Float = 0.72f,
    shape: Shape = CardShape,
    innerPadding: Dp = 12.dp,
): Modifier = this
    .clip(shape)
    .background(YzBg.copy(alpha = alpha))
    .padding(innerPadding)

/**
 * Soft vertical gradient wash with no hard edges — fades in from transparent at
 * the top and out to transparent at the bottom. Blends into the starfield rather
 * than reading as a card panel. Use for hero/headline moments (onboarding titles,
 * home greeting) where the fade itself provides contrast.
 *
 * @param alpha     Peak opacity at the centre of the wash. 0.5–0.65 suggested.
 * @param fadeRatio Fraction of the total height used for each fade edge (0..0.5).
 *                  0.25 means the top and bottom 25% fade from/to transparent.
 */
fun Modifier.yzTextSurfaceWash(
    alpha: Float = 0.58f,
    fadeRatio: Float = 0.28f,
): Modifier = this.background(
    Brush.verticalGradient(
        colorStops = arrayOf(
            0f           to Color.Transparent,
            fadeRatio    to YzBg.copy(alpha = alpha),
            1f - fadeRatio to YzBg.copy(alpha = alpha),
            1f           to Color.Transparent,
        )
    )
)
