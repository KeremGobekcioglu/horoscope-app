package com.kg.yildizname.core.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val AquariusConstellation: ImageVector
    get() = ImageVector.Builder(
        name           = "AquariusConstellation",
        defaultWidth   = 100.dp,
        defaultHeight  = 100.dp,
        viewportWidth  = 100f,
        viewportHeight = 100f
    ).apply {
        path(
            stroke          = SolidColor(Color(0xFFC9A84C)),
            strokeLineWidth = 1.5f,
            strokeLineCap   = StrokeCap.Round,
            strokeLineJoin  = StrokeJoin.Round
        ) {
            moveTo(20f, 20f)
            lineTo(40f, 30f)
            lineTo(45f, 55f)
            lineTo(70f, 60f)
            lineTo(85f, 85f)
        }

        val starColor = SolidColor(Color(0xFFC9A84C))
        val radius = 3f

        path(fill = starColor) {
            moveTo(20f, 20f); moveToRelative(-radius, 0f)
            arcTo(radius, radius, 0f, true, true, radius * 2f, 0f)
            arcTo(radius, radius, 0f, true, true, -radius * 2f, 0f)
        }
        path(fill = starColor) {
            moveTo(40f, 30f); moveToRelative(-radius, 0f)
            arcTo(radius, radius, 0f, true, true, radius * 2f, 0f)
            arcTo(radius, radius, 0f, true, true, -radius * 2f, 0f)
        }
        path(fill = starColor) {
            moveTo(45f, 55f); moveToRelative(-radius, 0f)
            arcTo(radius, radius, 0f, true, true, radius * 2f, 0f)
            arcTo(radius, radius, 0f, true, true, -radius * 2f, 0f)
        }
        path(fill = starColor) {
            moveTo(70f, 60f); moveToRelative(-radius, 0f)
            arcTo(radius, radius, 0f, true, true, radius * 2f, 0f)
            arcTo(radius, radius, 0f, true, true, -radius * 2f, 0f)
        }
        path(fill = starColor) {
            moveTo(85f, 85f); moveToRelative(-radius, 0f)
            arcTo(radius, radius, 0f, true, true, radius * 2f, 0f)
            arcTo(radius, radius, 0f, true, true, -radius * 2f, 0f)
        }
    }.build()
