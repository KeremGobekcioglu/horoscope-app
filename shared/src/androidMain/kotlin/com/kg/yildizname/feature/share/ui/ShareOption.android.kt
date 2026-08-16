package com.kg.yildizname.feature.share.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import com.kg.yildizname.core.ui.theme.YzGold
import com.kg.yildizname.core.ui.theme.YzSurfaceAlt
import com.kg.yildizname.platform.ShareTarget
import compose.icons.FeatherIcons
import compose.icons.FontAwesomeIcons
import compose.icons.feathericons.Share2
import compose.icons.fontawesomeicons.Brands
import compose.icons.fontawesomeicons.brands.Facebook
import compose.icons.fontawesomeicons.brands.Instagram
import compose.icons.fontawesomeicons.brands.Whatsapp
import horoscope.shared.generated.resources.Res
import horoscope.shared.generated.resources.Res.string
import horoscope.shared.generated.resources.share_option_facebook
import horoscope.shared.generated.resources.share_option_general
import horoscope.shared.generated.resources.share_option_instagram_stories
import horoscope.shared.generated.resources.share_option_whatsapp
import org.jetbrains.compose.resources.stringResource

@Composable
actual fun rememberShareOptions(): List<ShareOption> = listOf(
    ShareOption(
        target = ShareTarget.InstagramStories,
        label = stringResource(string.share_option_instagram_stories),
        icon = FontAwesomeIcons.Brands.Instagram,
        background = InstagramGradient,
    ),
    ShareOption(
        target = ShareTarget.WhatsApp,
        label = stringResource(string.share_option_whatsapp),
        icon = FontAwesomeIcons.Brands.Whatsapp,
        background = WhatsAppGreen,
    ),
    ShareOption(
        target = ShareTarget.Facebook,
        label = stringResource(string.share_option_facebook),
        icon = FontAwesomeIcons.Brands.Facebook,
        background = FacebookBlue,
    ),
    ShareOption(
        target = ShareTarget.SystemSheet,
        label = stringResource(string.share_option_general),
        icon = FeatherIcons.Share2,
        background = SolidColor(YzSurfaceAlt),
        iconTint = YzGold,
    ),
)