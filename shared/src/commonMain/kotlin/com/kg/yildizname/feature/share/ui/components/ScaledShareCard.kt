package com.kg.yildizname.feature.share.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kg.yildizname.core.ui.theme.CardShape
import com.kg.yildizname.feature.share.ui.util.ShareCardHeight
import kotlin.math.roundToInt

/**
 * Lays [content] out at its own intrinsic size (ignoring the parent's constraints entirely)
 * and then visually scales the result to [previewHeight] with a graphicsLayer transform.
 *
 * This is a *scale*, not a *shrink*: the content's layout pass never sees tighter constraints
 * than it asks for, so fixed dp/sp children can't clip. Required for previewing the share
 * cards, whose children use fixed sizes and do not participate in constraint shrinking.
 */

@Composable
fun ScaledShareCard(
    modifier: Modifier = Modifier,
    previewHeight: Dp = 480.dp,
    content: @Composable () -> Unit,
) {
    val scale = previewHeight / ShareCardHeight

    Layout(modifier = modifier.clip(CardShape), content = content) { measurables, _ ->
        val placeable = measurables.first().measure(Constraints())
        val scaledW = (placeable.width * scale).roundToInt()
        val scaledH = (placeable.height * scale).roundToInt()

        // Report the SCALED size so the parent centers a real ~270x480 element.
        layout(scaledW, scaledH) {
            placeable.placeWithLayer(0, 0) {
                scaleX = scale
                scaleY = scale
                transformOrigin = TransformOrigin(0f, 0f)   // scale into the reported bounds
            }
        }
    }
}