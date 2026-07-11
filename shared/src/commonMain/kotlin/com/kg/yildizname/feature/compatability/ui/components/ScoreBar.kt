package com.kg.yildizname.feature.compatability.ui.components

import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kg.yildizname.core.ui.theme.YzGold
import com.kg.yildizname.core.ui.theme.YzInk
import com.kg.yildizname.core.ui.theme.YzSurfaceAlt

@Composable
fun Scores(
    score: Int,
    field: String
) {
    var animationPlayed by remember { mutableStateOf(false) }
    val animatedFraction by animateFloatAsState(
        targetValue = if (animationPlayed) score / 10f else 0f,
        animationSpec = tween(durationMillis = 900, easing = EaseOutCubic),
        label = "score_bar_$field"
    )
    LaunchedEffect(score) { animationPlayed = true }
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    )
    {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = field,
                color = YzGold.copy(0.7f),
                fontSize = 10.sp
            )
            Text(
                text = "$score%",
                color = YzInk.copy(0.7f),
                fontSize = 10.sp
            )
        }
        Spacer(Modifier.height(4.dp))
        ScoreBar(animatedFraction)
    }
}

/**
 * ScoreBar — thin animated gold bar on a dark track.
 *
 * Visual layering:
 *   1. Dark track (full width, YzSurfaceAlt)
 *   2. Gold fill (animates from 0 → fraction)
 *   3. Bright gold tip — 4dp cap that glows at the leading edge
 *
 * This gives depth without shadows: the tip reads as the energy "cursor".
 */
@Composable
private fun ScoreBar(
    animatedFraction: Float,
    modifier: Modifier = Modifier,
) {
    val goldColor   = YzInk
    val trackColor  = YzSurfaceAlt   // #141830
    val tipColor    = Color(0xFFFFD980)  // brighter than YzGold for the tip highlight

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(3.dp)
            .clip(RoundedCornerShape(999.dp))
            .background(trackColor)
            .drawBehind {
                val barWidth = size.width * animatedFraction
                if (barWidth > 0f) {
                    // Gold fill
                    drawRect(
                        color = goldColor,
                        size = Size(barWidth, size.height)
                    )
                    // Bright tip cap — 6dp wide glow at leading edge
                    val tipWidth = 6.dp.toPx().coerceAtMost(barWidth)
                    drawRect(
                        color = tipColor,
                        topLeft = Offset(x = barWidth - tipWidth, y = 0f),
                        size = Size(tipWidth, size.height)
                    )
                }
            }
    )
}