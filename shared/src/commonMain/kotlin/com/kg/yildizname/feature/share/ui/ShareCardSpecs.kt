package com.kg.yildizname.feature.share.ui

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp

/**
 * Single source of truth for share card width — every share card (daily [ShareCard],
 * [CompatibilityShareCard], [CompatibilityDetailedShareCard]) is exactly this wide; height wraps
 * each card's actual content instead of being pinned, so text is never forced to ellipsize to
 * fit a guessed-at budget. [ShareCardHeight] is [ShareCard]/[CompatibilityShareCard]'s nominal,
 * roughly-accurate content height, kept around only as [ScaledShareCard]'s preview-scale
 * reference — it is not a layout constraint. The offscreen capture path composes at
 * [ShareCardExportDensity] and captures whatever size the content actually measures to.
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