package com.kg.yildizname.feature.compatibility.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kg.yildizname.core.ui.theme.YzGold
import com.kg.yildizname.core.ui.theme.YzOnSurface
import com.kg.yildizname.core.ui.theme.YzSurface
import com.kg.yildizname.core.ui.theme.YzTypography
import com.kg.yildizname.core.ui.theme.YzViolet
import kotlin.math.roundToInt

/**
 * Circular animated score ring for compatibility results between two zodiac signs.
 * Ring sweep and the percentage number animate together from a single progress source.
 */
@Composable
fun CompatibilityScoreRing(
    matchPercent: Int,
    modifier: Modifier = Modifier,
    size: Dp = 180.dp,
    strokeWidth: Dp = 14.dp,
) {
    val clampedTarget = matchPercent.coerceIn(0, 100)
    val progress = remember { Animatable(0f) }

    LaunchedEffect(clampedTarget) {
        progress.snapTo(0f)
        progress.animateTo(
            targetValue = clampedTarget / 100f,
            animationSpec = tween(durationMillis = 1200, easing = EaseOutCubic),
        )
    }

    Box(
        modifier = modifier
            .size(size)
            .drawBehind {
                val stroke = strokeWidth.toPx()
                val diameter = this.size.minDimension - stroke
                val topLeft = Offset(
                    (this.size.width - diameter) / 2f,
                    (this.size.height - diameter) / 2f,
                )
                val arcSize = Size(diameter, diameter)

                // Track (unfilled background ring)
                drawArc(
                    color = YzSurface,
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = stroke, cap = StrokeCap.Round),
                )

                // Rotate so the sweep gradient's 0° aligns with the arc start (12 o'clock)
                rotate(degrees = -90f) {
                    drawArc(
                        brush = Brush.sweepGradient(
                            colors = listOf(YzGold, YzViolet, YzGold),
                            center = center,
                        ),
                        startAngle = 0f,
                        sweepAngle = 360f * progress.value,
                        useCenter = false,
                        topLeft = topLeft,
                        size = arcSize,
                        style = Stroke(width = stroke, cap = StrokeCap.Round),
                    )
                }
            },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "${(progress.value * 100).roundToInt()}%",
            style = YzTypography.headlineLarge,
            color = YzOnSurface,
        )
    }
}