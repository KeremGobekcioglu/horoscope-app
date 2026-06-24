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
    Canvas(modifier = modifier) {
        stars.forEach { s ->
            val px = s.x * size.width
            val py = s.y * size.height
            val r  = s.radius.dp.toPx()
            if (s.isGlow) {
                drawCircle(
                    brush  = Brush.radialGradient(
                        listOf(YzStarWhite.copy(alpha = s.alpha * 0.3f), Color.Transparent),
                        center = Offset(px, py), radius = r * 6f,
                    ),
                    radius = r * 6f, center = Offset(px, py),
                )
            }
            drawCircle(YzStarWhite.copy(alpha = s.alpha), r, Offset(px, py))
        }
    }
}
