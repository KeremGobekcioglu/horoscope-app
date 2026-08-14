package com.kg.yildizname.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kg.yildizname.core.data.model.ZodiacSign
import com.kg.yildizname.core.data.model.compatGridIcon
import com.kg.yildizname.core.data.model.localizedName
import com.kg.yildizname.core.ui.theme.SheetShape
import com.kg.yildizname.core.ui.theme.SquareShape
import com.kg.yildizname.core.ui.theme.YzBorder
import com.kg.yildizname.core.ui.theme.YzGold
import com.kg.yildizname.core.ui.theme.YzInk
import com.kg.yildizname.core.ui.theme.YzOnSurface
import com.kg.yildizname.core.ui.theme.YzSurface
import com.kg.yildizname.core.ui.utils.yzNavigationBarsPadding
import com.kg.yildizname.feature.compatability.ui.components.AnalyzeButton
import horoscope.shared.generated.resources.Res
import horoscope.shared.generated.resources.compat_pick_sign_placeholder
import horoscope.shared.generated.resources.compat_select_sign
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource



/**
 * Modal sheet with a 4x3 grid of the 12 zodiac signs.
 * Tapping a tile just highlights it so the user can change their mind. The pick is only
 * reported via [onSignSelected] when the sheet closes — whether by the button, tapping the
 * scrim, or the back gesture — so callers never see the selection update while it's still open.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectSignBottomSheet(
    onSignSelected: (ZodiacSign) -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedSign by remember { mutableStateOf<ZodiacSign?>(null) }

    val commitAndDismiss: () -> Unit = {
        selectedSign?.let(onSignSelected)
        onDismiss()
    }

    ModalBottomSheet(
        onDismissRequest = commitAndDismiss,
        sheetState = sheetState,
        shape = SheetShape,
        containerColor = YzSurface,
        tonalElevation = 0.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .yzNavigationBarsPadding(),
        ) {
            Text(
                text = stringResource(Res.string.compat_pick_sign_placeholder),
                color = YzGold,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                items(ZodiacSign.entries) { sign ->
                    SignGridItem(
                        sign = sign,
                        isSelected = sign == selectedSign,
                        onClick = { selectedSign = sign },
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            AnalyzeButton(
                isEnabled = selectedSign != null,
                text = stringResource(Res.string.compat_select_sign),
                onClick = commitAndDismiss,
                iconVisible = false
            )

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun SignGridItem(
    sign: ZodiacSign,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(SquareShape)
                .background(if (isSelected) YzGold.copy(0.15f) else YzOnSurface.copy(0.06f))
                .border(
                    width = if (isSelected) 2.dp else 1.dp,
                    color = if (isSelected) YzGold else YzBorder,
                    shape = SquareShape,
                )
                .clickable(onClick = onClick)
                .padding(14.dp),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(sign.compatGridIcon),
                contentDescription = sign.localizedName(),
                tint = YzGold.copy(0.75f),
                modifier = Modifier.fillMaxWidth(),
            )
        }
        Spacer(Modifier.height(6.dp))
        Text(
            text = sign.localizedName(),
            color = if (isSelected) YzGold else YzInk,
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.Center,
        )
    }
}