package com.kg.yildizname.feature.home.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.border
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kg.yildizname.core.ui.theme.YzBorder
import com.kg.yildizname.core.ui.theme.YzSurface

@Composable
internal fun HomeLoadingContent(
    contentMaxWidth: Dp,
    onShareCardClick: () -> Unit,
    onNotificationClick: () -> Unit,
) {
    // Shimmer animation — linear loop
    val shimmerTransition = rememberInfiniteTransition(label = "shimmer")
    val shimmerOffset by shimmerTransition.animateFloat(
        initialValue = -1f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_offset"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .let { if (contentMaxWidth != Dp.Infinity) it.widthIn(max = contentMaxWidth) else it }
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Top bar skeleton
        HomeTopBar(
            dateLabel = "── ──────, ─────",
            onNotificationClick = onNotificationClick,
            onShareCardClick = onShareCardClick,
        )

        Spacer(Modifier.height(24.dp))

        // Constellation placeholder
        ShimmerBox(
            width = 200.dp, height = 200.dp,
            shape = RoundedCornerShape(999.dp),
            shimmerOffset = shimmerOffset
        )

        Spacer(Modifier.height(20.dp))

        // Sign name skeleton
        ShimmerBox(width = 120.dp, height = 24.dp, shimmerOffset = shimmerOffset)
        Spacer(Modifier.height(8.dp))
        ShimmerBox(width = 80.dp, height = 14.dp, shimmerOffset = shimmerOffset)

        Spacer(Modifier.height(28.dp))

        // Reading card skeleton
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(YzSurface)
                .border(0.5.dp, YzBorder, RoundedCornerShape(20.dp))
                .padding(20.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                ShimmerBox(width = 110.dp, height = 12.dp, shimmerOffset = shimmerOffset)
                repeat(5) {
                    ShimmerBox(
                        width = if (it == 4) 160.dp else Dp.Infinity,
                        height = 13.dp,
                        shimmerOffset = shimmerOffset,
                        modifier = if (it != 4) Modifier.fillMaxWidth() else Modifier
                    )
                }
            }
        }

        Spacer(Modifier.height(28.dp))

        // Energy grid skeleton
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ShimmerBox(width = 100.dp, height = 12.dp, shimmerOffset = shimmerOffset)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ShimmerBox(height = 90.dp, shimmerOffset = shimmerOffset,
                    modifier = Modifier.weight(1f), shape = RoundedCornerShape(20.dp))
                ShimmerBox(height = 90.dp, shimmerOffset = shimmerOffset,
                    modifier = Modifier.weight(1f), shape = RoundedCornerShape(20.dp))
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ShimmerBox(height = 90.dp, shimmerOffset = shimmerOffset,
                    modifier = Modifier.weight(1f), shape = RoundedCornerShape(20.dp))
                ShimmerBox(height = 90.dp, shimmerOffset = shimmerOffset,
                    modifier = Modifier.weight(1f), shape = RoundedCornerShape(20.dp))
            }
        }

        Spacer(Modifier.height(96.dp))
    }
}

@Composable
private fun ShimmerBox(
    shimmerOffset: Float,
    height: Dp,
    modifier: Modifier = Modifier,
    width: Dp = Dp.Unspecified,
    shape: RoundedCornerShape = RoundedCornerShape(6.dp),
) {
    val baseColor    = YzSurface
    val highlightColor = Color(0xFF1E2448)   // slightly brighter than YzSurface

    val brush = Brush.linearGradient(
        colors = listOf(baseColor, highlightColor, baseColor),
        start = Offset(shimmerOffset * 400f, 0f),
        end   = Offset(shimmerOffset * 400f + 400f, 0f)
    )

    Box(
        modifier = modifier
            .then(if (width != Dp.Infinity && width != Dp.Unspecified) Modifier.width(width) else Modifier)
            .height(height)
            .clip(shape)
            .background(brush)
    )
}
