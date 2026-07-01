package com.kg.yildizname.core.ui.utils

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Constraints
import kotlin.math.roundToInt

/**
 * Measures the child at its own intrinsic size (ignoring incoming constraints) then reports a
 * scaled-down footprint to the parent — for previewing fixed-size composables (e.g. ShareCard)
 * at a smaller on-screen size without changing their internal layout.
 */
fun Modifier.scaledToFit(scale: Float): Modifier = this.layout { measurable, _ ->
    val placeable = measurable.measure(Constraints())
    val width = (placeable.width * scale).roundToInt()
    val height = (placeable.height * scale).roundToInt()
    layout(width, height) {
        placeable.placeRelativeWithLayer(0, 0) {
            scaleX = scale
            scaleY = scale
            transformOrigin = TransformOrigin(0f, 0f)
        }
    }
}
