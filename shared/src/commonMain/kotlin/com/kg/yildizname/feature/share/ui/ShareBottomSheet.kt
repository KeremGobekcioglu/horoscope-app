package com.kg.yildizname.feature.share.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kg.yildizname.core.ui.theme.CardShape
import com.kg.yildizname.core.ui.theme.ChipShape
import com.kg.yildizname.core.ui.theme.YzBorder
import com.kg.yildizname.core.ui.theme.YzGold
import com.kg.yildizname.core.ui.theme.YzInk
import com.kg.yildizname.core.ui.theme.YzSurface
import com.kg.yildizname.core.ui.theme.YzSurfaceAlt
import com.kg.yildizname.core.ui.utils.yzNavigationBarsPadding
import compose.icons.FeatherIcons
import compose.icons.FontAwesomeIcons
import compose.icons.feathericons.Download
import compose.icons.feathericons.Link
import compose.icons.feathericons.Share2
import compose.icons.fontawesomeicons.Brands
import compose.icons.fontawesomeicons.brands.Facebook
import compose.icons.fontawesomeicons.brands.Instagram
import compose.icons.fontawesomeicons.brands.Whatsapp
import horoscope.shared.generated.resources.Res
import horoscope.shared.generated.resources.share_bottom_sheet_title
import horoscope.shared.generated.resources.share_close
import horoscope.shared.generated.resources.share_option_copy_link
import horoscope.shared.generated.resources.share_option_facebook
import horoscope.shared.generated.resources.share_option_general
import horoscope.shared.generated.resources.share_option_instagram_stories
import horoscope.shared.generated.resources.share_option_save_image
import horoscope.shared.generated.resources.share_option_whatsapp
import org.jetbrains.compose.resources.stringResource

// Platform brand colors — not app design tokens, hardcoded per each platform's own brand guidelines.
private val InstagramGradient = listOf(Color(0xFFFEDA75), Color(0xFFD62976), Color(0xFF962FBF), Color(0xFF4F5BD5))
private val WhatsAppGreen = Color(0xFF25D366)
private val FacebookBlue = Color(0xFF1877F2)

/**
 * @param preview Already-scaled preview content shown at the top of the sheet — pass
 * `ShareCardPreview(...)`, not the raw `ShareCard` export composable. `ShareCard`'s fixed
 * dp/sp children clip instead of shrinking if squeezed directly into the sheet's preview box.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShareBottomSheet(
    preview: @Composable () -> Unit,
    onInstagramStoriesClick: () -> Unit,
    onWhatsAppClick: () -> Unit,
    onFacebookClick: () -> Unit,
    onGeneralShareClick: () -> Unit,
    onCopyLinkClick: () -> Unit,
    onSaveImageClick: () -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = YzSurface,
        tonalElevation = 0.dp,
    ) {
        Column(Modifier.fillMaxWidth().verticalScroll(rememberScrollState()).yzNavigationBarsPadding()) {
            Box(Modifier.fillMaxWidth().padding(vertical = 16.dp), contentAlignment = Alignment.Center) {
                preview()   // centered, letterboxed automatically
            }

            Text(
                text = stringResource(Res.string.share_bottom_sheet_title),
                color = YzGold,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                SharePlatformOption(
                    icon = FontAwesomeIcons.Brands.Instagram,
                    label = stringResource(Res.string.share_option_instagram_stories),
                    background = Brush.linearGradient(InstagramGradient),
                    onClick = onInstagramStoriesClick,
                )
                SharePlatformOption(
                    icon = FontAwesomeIcons.Brands.Whatsapp,
                    label = stringResource(Res.string.share_option_whatsapp),
                    background = SolidColor(WhatsAppGreen),
                    onClick = onWhatsAppClick,
                )
                SharePlatformOption(
                    icon = FontAwesomeIcons.Brands.Facebook,
                    label = stringResource(Res.string.share_option_facebook),
                    background = SolidColor(FacebookBlue),
                    onClick = onFacebookClick,
                )
                SharePlatformOption(
                    icon = FeatherIcons.Share2,
                    label = stringResource(Res.string.share_option_general),
                    background = SolidColor(YzSurfaceAlt),
                    iconTint = YzGold,
                    onClick = onGeneralShareClick,
                )
            }

            Spacer(Modifier.height(24.dp))
            HorizontalDivider(color = YzBorder, thickness = 1.dp)

            ShareTextRow(
                icon = FeatherIcons.Link,
                label = stringResource(Res.string.share_option_copy_link),
                onClick = onCopyLinkClick,
            )
            ShareTextRow(
                icon = FeatherIcons.Download,
                label = stringResource(Res.string.share_option_save_image),
                onClick = onSaveImageClick,
            )

            HorizontalDivider(color = YzBorder, thickness = 1.dp)

            Text(
                text = stringResource(Res.string.share_close),
                color = YzGold,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 3.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onDismiss)
                    .padding(vertical = 20.dp),
            )
        }
    }
}

@Composable
private fun SharePlatformOption(
    icon: ImageVector,
    label: String,
    background: Brush,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconTint: Color = Color.White,
) {
    Column(
        modifier = modifier.width(72.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(ChipShape)
                .background(background)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconTint,
                modifier = Modifier.size(24.dp),
            )
        }
        Spacer(Modifier.height(8.dp))
        Text(
            text = label,
            color = YzInk,
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun ShareTextRow(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = YzGold,
            modifier = Modifier.size(20.dp),
        )
        Spacer(Modifier.width(16.dp))
        Text(
            text = label,
            color = YzInk,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}
