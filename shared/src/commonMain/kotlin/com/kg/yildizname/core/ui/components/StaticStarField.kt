package com.kg.yildizname.core.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kg.yildizname.core.ui.theme.YzStarWhite
import kotlin.random.Random

private data class StaticStar(
    val x: Float,
    val y: Float,
    val radius: Float,
    val baseAlpha: Float,
    val isGlow: Boolean,
)

private fun generateStaticStars(seed: Long): List<StaticStar> {
    val rng = Random(seed)
    return buildList {
        repeat(90) {
            add(
                StaticStar(
                    x = rng.nextFloat(),
                    y = rng.nextFloat(),
                    radius = rng.nextFloat() * 0.5f + 0.5f,
                    baseAlpha = rng.nextFloat() * 0.4f + 0.2f,
                    isGlow = false,
                )
            )
        }
        repeat(7) {
            add(
                StaticStar(
                    x = rng.nextFloat(),
                    y = rng.nextFloat(),
                    radius = 1.5f,
                    baseAlpha = 0.85f,
                    isGlow = true,
                )
            )
        }
    }
}

/**
 * Fixed-seed star backdrop with no animation or touch handling — same field used on
 * [com.kg.yildizname.feature.splash.ui.SplashScreen]. Safe to nest inside interactive
 * containers (e.g. a draggable bottom sheet) since it never consumes pointer input.
 */
@Composable
fun StaticStarField(modifier: Modifier = Modifier, seed: Long = 42L) {
    val stars = remember(seed) { generateStaticStars(seed) }
    Canvas(modifier = modifier) {
        stars.forEach { star ->
            val px = star.x * size.width
            val py = star.y * size.height
            val r = star.radius.dp.toPx()
            if (star.isGlow) {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            YzStarWhite.copy(alpha = star.baseAlpha * 0.35f),
                            Color.Transparent,
                        ),
                        center = Offset(px, py),
                        radius = r * 6f,
                    ),
                    radius = r * 6f,
                    center = Offset(px, py),
                )
            }
            drawCircle(
                color = YzStarWhite.copy(alpha = star.baseAlpha),
                radius = r,
                center = Offset(px, py),
            )
        }
    }
}
