package com.kg.yildizname.feature.share.ui.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.SolidColor
import com.kg.yildizname.core.ui.theme.YzGold
import com.kg.yildizname.core.ui.theme.YzSurfaceAlt
import com.kg.yildizname.feature.share.ui.domain.ShareOption
import com.kg.yildizname.platform.ShareTarget
import compose.icons.FeatherIcons
import compose.icons.FontAwesomeIcons
import compose.icons.feathericons.Share2
import compose.icons.fontawesomeicons.Brands
import compose.icons.fontawesomeicons.brands.Instagram
import horoscope.shared.generated.resources.Res
import horoscope.shared.generated.resources.share_option_general
import horoscope.shared.generated.resources.share_option_instagram_stories
import org.jetbrains.compose.resources.stringResource
// iOS has no equivalent of ACTION_SEND + setPackage: URL schemes carry text only, so an image
// cannot be handed to a named app. Instagram Stories works solely because Meta built a
// pasteboard side channel for it. Everything else goes through UIActivityViewController, where
// the user picks the destination — so WhatsApp/Facebook aren't separate targets here.
@Composable
actual fun rememberShareOptions(): List<ShareOption> = listOf(
    ShareOption(
        target = ShareTarget.InstagramStories,
        label = stringResource(Res.string.share_option_instagram_stories),
        icon = FontAwesomeIcons.Brands.Instagram,
        background = InstagramGradient,
    ),
    ShareOption(
        target = ShareTarget.SystemSheet,
        label = stringResource(Res.string.share_option_general),
        icon = FeatherIcons.Share2,
        background = SolidColor(YzSurfaceAlt),
        iconTint = YzGold,
    ),
)

actual val promoteShareActionsIntoGrid: Boolean = true