package com.kg.yildizname.feature.share.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kg.yildizname.core.ui.theme.ChipShape
import com.kg.yildizname.core.ui.theme.SheetShape
import com.kg.yildizname.core.ui.theme.YzBorder
import com.kg.yildizname.core.ui.theme.YzError
import com.kg.yildizname.core.ui.theme.YzGold
import com.kg.yildizname.core.ui.theme.YzInk
import com.kg.yildizname.core.ui.theme.YzSurface
import com.kg.yildizname.core.ui.theme.YzSurfaceAlt
import com.kg.yildizname.core.ui.utils.yzNavigationBarsPadding
import com.kg.yildizname.feature.share.ui.domain.ShareOption
import com.kg.yildizname.feature.share.ui.platform.promoteShareActionsIntoGrid
import com.kg.yildizname.platform.ShareTarget
import compose.icons.FeatherIcons
import compose.icons.feathericons.Download
import compose.icons.feathericons.FileText
import horoscope.shared.generated.resources.Res
import horoscope.shared.generated.resources.share_bottom_sheet_title
import horoscope.shared.generated.resources.share_close
import horoscope.shared.generated.resources.share_option_save_image
import horoscope.shared.generated.resources.share_option_share_text
import org.jetbrains.compose.resources.stringResource

// Platform brand colors — not app design tokens, hardcoded per each platform's own brand guidelines.
/*private val InstagramGradient = listOf(Color(0xFFFEDA75), Color(0xFFD62976), Color(0xFF962FBF), Color(0xFF4F5BD5))
private val WhatsAppGreen = Color(0xFF25D366)
private val FacebookBlue = Color(0xFF1877F2) */

/**
 * @param preview Already-scaled preview content shown at the top of the sheet — pass
 * `ShareCardPreview(...)`, not the raw `ShareCard` export composable. `ShareCard`'s fixed
 * dp/sp children clip instead of shrinking if squeezed directly into the sheet's preview box.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShareBottomSheet(
    preview: @Composable () -> Unit,
    isWorking: Boolean,
    errorMessage: String?,
    errorActionLabel: String? = null,
    errorAction: (() -> Unit)? = null,
    options: List<ShareOption>,
    onOptionClick: (ShareTarget) -> Unit,
    onShareTextClick: () -> Unit,
    onSaveImageClick: () -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = SheetShape,
        containerColor = YzSurface,
        tonalElevation = 0.dp,
    ) {
        ShareBottomSheetContent(
            preview = preview,
            isWorking = isWorking,
            errorMessage = errorMessage,
            errorAction = errorAction,
            errorActionLabel = errorActionLabel,
            options = options,
            onOptionClick = onOptionClick,
            onShareTextClick = onShareTextClick,
            onSaveImageClick = onSaveImageClick,
            onDismiss = onDismiss,
        )
    }
}

/**
 * The sheet's scrollable body, split out from [ShareBottomSheet] so it can be previewed on its
 * own: `ModalBottomSheet` renders its content through a Popup, which Android Studio's static
 * `@Preview` surface never captures, so previewing [ShareBottomSheet] directly always renders
 * blank. Compose this instead (e.g. inside a plain `Surface`) to preview the sheet's contents.
 */
@Composable
fun ShareBottomSheetContent(
    preview: @Composable () -> Unit,
    isWorking: Boolean,
    errorMessage: String?,
    options: List<ShareOption>,
    errorActionLabel: String? = null,
    errorAction: (() -> Unit)? = null,
    onOptionClick: (ShareTarget) -> Unit,
    onShareTextClick: () -> Unit,
    onSaveImageClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier.fillMaxWidth()
            .verticalScroll(
                rememberScrollState()
            )
            .yzNavigationBarsPadding()
    ) {
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

        val tiles = if (promoteShareActionsIntoGrid) {
            buildList {
                options.forEachIndexed { index, option ->
                    add(GridTile(option.icon, option.label, option.background, option.iconTint) { onOptionClick(option.target) })
                    // iOS only ever offers [InstagramStories, SystemSheet] — insert the
                    // promoted actions right after the first tile so the grid reads
                    // [Instagram Stories] [Save image] [Share as text] [More].
                    if (index == 0) {
                        add(
                            GridTile(
                                icon = FeatherIcons.Download,
                                label = stringResource(Res.string.share_option_save_image),
                                background = SolidColor(YzSurfaceAlt),
                                iconTint = YzGold,
                                onClick = onSaveImageClick,
                            )
                        )
                        add(
                            GridTile(
                                icon = FeatherIcons.FileText,
                                label = stringResource(Res.string.share_option_share_text),
                                background = SolidColor(YzSurfaceAlt),
                                iconTint = YzGold,
                                onClick = onShareTextClick,
                            )
                        )
                    }
                }
            }
        } else {
            options.map { option -> GridTile(option.icon, option.label, option.background, option.iconTint) { onOptionClick(option.target) } }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement =
                if (tiles.size <= 2) Arrangement.spacedBy(32.dp, Alignment.CenterHorizontally)
                else Arrangement.SpaceBetween,
        ) {
            tiles.forEach { tile ->
                SharePlatformOption(
                    icon = tile.icon,
                    label = tile.label,
                    background = tile.background,
                    iconTint = tile.iconTint,
                    onClick = tile.onClick,
                    enabled = !isWorking,
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        if (!promoteShareActionsIntoGrid) {
            HorizontalDivider(color = YzBorder, thickness = 1.dp)

            ShareTextRow(
                icon = FeatherIcons.FileText,
                label = stringResource(Res.string.share_option_share_text),
                onClick = onShareTextClick,
                enabled = !isWorking,
            )
            ShareTextRow(
                icon = FeatherIcons.Download,
                label = stringResource(Res.string.share_option_save_image),
                onClick = onSaveImageClick,
                enabled = !isWorking,
            )
        }

        if (errorMessage != null) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = errorMessage,
                    color = YzError,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                )
                if (errorActionLabel != null && errorAction != null) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = errorActionLabel,
                        color = YzGold,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.clickable(onClick = errorAction),
                    )
                }
            }
        }

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

/** Unifies a [ShareOption] tile and a promoted action (save/share-text) so the icon row can render both the same way. */
private data class GridTile(
    val icon: ImageVector,
    val label: String,
    val background: Brush,
    val iconTint: Color = Color.White,
    val onClick: () -> Unit,
)

@Composable
private fun SharePlatformOption(
    icon: ImageVector,
    label: String,
    background: Brush,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconTint: Color = Color.White,
    enabled: Boolean = true,
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
                // Visually signal the disabled state — we have no drop shadows or overlays
                // per the design system, so alpha on the whole tile is the available lever.
                .alpha(if (enabled) 1f else 0.4f)
                .clickable(enabled = enabled, onClick = onClick),
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
    enabled: Boolean = true,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .alpha(if (enabled) 1f else 0.4f)
            .clickable(enabled = enabled, onClick = onClick)
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
