package com.kg.yildizname.feature.share.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import com.kg.yildizname.platform.ShareTarget

data class ShareOption(
    val target: ShareTarget,
    val label: String,
    val icon: ImageVector,
    val background: Brush,
    val iconTint: Color = Color.White
)

// Not actually `remember`-ed: ShareOption.background is a Brush, which isn't @Stable, and the
// labels come from stringResource, so memoizing would need a locale key to stay correct.
// Rebuilding six objects per recomposition is cheap — the name is kept for call-site readability.
/** Platform-specific set of share targets, in display order. */
@Composable
expect fun rememberShareOptions(): List<ShareOption>

// Platform brand colors — not app design tokens, per each platform's own brand guidelines.
internal val InstagramGradient = Brush.linearGradient(
    listOf(Color(0xFFFEDA75), Color(0xFFD62976), Color(0xFF962FBF), Color(0xFF4F5BD5))
)
internal val WhatsAppGreen = SolidColor(Color(0xFF25D366))
internal val FacebookBlue = SolidColor(Color(0xFF1877F2))