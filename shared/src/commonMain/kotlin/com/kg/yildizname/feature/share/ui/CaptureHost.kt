package com.kg.yildizname.feature.share.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Constraints

/**
 * Composes [content] at its own intrinsic size — ignoring the parent's constraints entirely —
 * records its draw output into [layer], and reports zero size to the parent so nothing is
 * ever visible on screen. Once this has composed at least one frame, layer.toImageBitmap()
 * returns the captured pixels at the layer's recorded size.
 *
 * Caller is responsible for pinning density (see ShareCardExportDensity) before composing
 * this — capture happens at whatever density is in scope, same as any other composable.
 */

@Composable
fun CaptureHost(
    layer: GraphicsLayer,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
)
{
    Layout(
        modifier = modifier.drawWithContent {
            layer.record {
                this@drawWithContent.drawContent()
            }
            // Deliberately not calling drawContent() here — the content goes into the layer,
            // never onto the screen.
        },
        content = content,
    )
    {
        measurables, _ ->
        val placeable = measurables.first().measure(Constraints())
        // Report the real size so the draw phase actually runs for this node.
        layout(placeable.width, placeable.height) {
            placeable.place(0, 0)
        }
    }
}