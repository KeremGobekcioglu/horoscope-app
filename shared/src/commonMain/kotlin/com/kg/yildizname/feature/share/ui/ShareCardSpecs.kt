package com.kg.yildizname.feature.share.ui

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp

/**
 * Single source of truth for share card geometry. Both [ShareCard] and [CompatibilityShareCard]
 * render at exactly this size; [ScaledShareCard] divides by [ShareCardHeight] for preview scale;
 * the offscreen capture path composes at [ShareCardExportDensity].
 */
internal val ShareCardWidth = 675.dp
internal val ShareCardHeight = 1200.dp

/** Instagram Stories' recommended background asset size (9:16). */
internal const val ShareCardExportWidthPx = 1080

/**
 * Density used for offscreen capture: 675dp * 1.6 = 1080px, 1200dp * 1.6 = 1920px.
 *
 * Pinned so the exported PNG is the same size on every device — otherwise a 3x phone exports
 * 2025x3600 and a 1.5x tablet exports 1013x1800. fontScale is pinned to 1f so a user's
 * accessibility text-size setting cannot reflow the export.
 */
internal val ShareCardExportDensity = Density(
    density = ShareCardExportWidthPx / ShareCardWidth.value,
    fontScale = 1f,
)