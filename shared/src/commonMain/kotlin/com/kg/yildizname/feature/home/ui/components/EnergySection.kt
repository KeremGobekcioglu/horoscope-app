package com.kg.yildizname.feature.home.ui.components

import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kg.yildizname.core.data.model.ScoreSet
import com.kg.yildizname.core.ui.theme.YzBorder
import com.kg.yildizname.core.ui.theme.YzGold
import com.kg.yildizname.core.ui.theme.YzMuted
import com.kg.yildizname.core.ui.theme.YzSurface
import com.kg.yildizname.core.ui.theme.YzSurfaceAlt
import compose.icons.FeatherIcons
import compose.icons.feathericons.Activity
import compose.icons.feathericons.Briefcase
import compose.icons.feathericons.Heart
import compose.icons.feathericons.Star
import horoscope.shared.generated.resources.Res
import horoscope.shared.generated.resources.home_daily_energy_label
import horoscope.shared.generated.resources.home_score_health
import horoscope.shared.generated.resources.home_score_luck
import horoscope.shared.generated.resources.home_score_love
import horoscope.shared.generated.resources.home_score_work
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun EnergySection(
    scores: ScoreSet,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Section label
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(RoundedCornerShape(999.dp))
                    .background(YzGold)
            )
            Text(
                text = stringResource(Res.string.home_daily_energy_label),
                color = YzGold,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 2.sp
            )
        }

        // 2×2 grid
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                EnergyScoreTile(
                    label = stringResource(Res.string.home_score_love),
                    score = scores.love,
                    icon = FeatherIcons.Heart,
                    modifier = Modifier.weight(1f)
                )
                EnergyScoreTile(
                    label = stringResource(Res.string.home_score_work),
                    score = scores.work,
                    icon = FeatherIcons.Briefcase,
                    modifier = Modifier.weight(1f)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                EnergyScoreTile(
                    label = stringResource(Res.string.home_score_health),
                    score = scores.health,
                    icon = FeatherIcons.Activity,
                    modifier = Modifier.weight(1f)
                )
                EnergyScoreTile(
                    label = stringResource(Res.string.home_score_luck),
                    score = scores.luck,
                    icon = FeatherIcons.Star,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun EnergyScoreTile(
    label: String,
    score: Int,
    icon: ImageVector,
    modifier: Modifier = Modifier,
) {
    // Animate the score bar width from 0 → fraction on first composition
    var animationPlayed by remember { mutableStateOf(false) }
    val animatedFraction by animateFloatAsState(
        targetValue = if (animationPlayed) score / 10f else 0f,
        animationSpec = tween(durationMillis = 900, easing = EaseOutCubic),
        label = "score_bar_$label"
    )
    LaunchedEffect(score) { animationPlayed = true }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(YzSurface)
            .border(0.5.dp, YzBorder, RoundedCornerShape(20.dp))
            .padding(start = 16.dp, end = 16.dp, top = 14.dp, bottom = 14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {

            // Top row: icon (left) + score (right)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = YzMuted,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "$score/10",
                    color = YzGold,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.5.sp
                )
            }

            // Label
            Text(
                text = label,
                color = YzMuted,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 1.5.sp
            )

            // Animated score bar — layered: track + fill + shimmer glow
            ScoreBar(animatedFraction = animatedFraction)
        }
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
    val goldColor   = YzGold
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
