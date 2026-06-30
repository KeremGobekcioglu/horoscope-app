package com.kg.yildizname.core.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kg.yildizname.core.ui.theme.YzBg
import com.kg.yildizname.core.ui.theme.YzStarWhite
import com.kg.yildizname.core.ui.theme.YzViolet
import kotlin.math.PI
import kotlin.math.sin

private fun buildStars(seed: Long = 99L): List<Star> {
    val rng = kotlin.random.Random(seed)
    return buildList {
        repeat(90) {
            add(Star(rng.nextFloat(), rng.nextFloat(),
                rng.nextFloat() * 0.5f + 0.5f,
                rng.nextFloat() * 0.4f + 0.2f, false))
        }
        repeat(6) {
            add(Star(rng.nextFloat(), rng.nextFloat(), 1.5f, 0.8f, true))
        }
    }
}

@Composable
fun StarFieldBackground(modifier: Modifier = Modifier) {
    val stars = remember { buildStars() }
    val twinkle by rememberInfiniteTransition(label = "starTwinkle").animateFloat(
        initialValue = 0f,
        targetValue = 2f * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "twinkle_phase"
    )
    Canvas(modifier = modifier) {
        // Deep-sky gradient backdrop — subtle violet glow fading into the base navy
        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(YzViolet.copy(alpha = 0.16f), YzBg),
                center = Offset(size.width * 0.5f, size.height * 0.18f),
                radius = size.width.coerceAtLeast(size.height) * 0.9f
            )
        )
        stars.forEachIndexed { index, s ->
            val px = s.x * size.width
            val py = s.y * size.height
            val r  = s.radius.dp.toPx()
            // Stagger each star's phase so they don't all twinkle in unison
            val twinkleAlpha = s.alpha * (0.7f + 0.3f * sin(twinkle + index * 0.7f))
            if (s.isGlow) {
                drawCircle(
                    brush  = Brush.radialGradient(
                        listOf(YzStarWhite.copy(alpha = twinkleAlpha * 0.3f), Color.Transparent),
                        center = Offset(px, py), radius = r * 6f,
                    ),
                    radius = r * 6f, center = Offset(px, py),
                )
            }
            drawCircle(YzStarWhite.copy(alpha = twinkleAlpha), r, Offset(px, py))
        }
    }
}
