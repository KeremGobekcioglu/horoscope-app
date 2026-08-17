package com.kg.yildizname.feature.onboarding.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import com.kg.yildizname.core.ui.utils.yzNavigationBarsPadding
import com.kg.yildizname.core.ui.utils.yzStatusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kg.yildizname.core.data.model.ZodiacSign
import com.kg.yildizname.core.data.model.localizedDateRange
import com.kg.yildizname.core.data.model.localizedName
import com.kg.yildizname.core.ui.components.PageIndicator
import com.kg.yildizname.core.ui.components.yzTextSurfaceWash
import com.kg.yildizname.core.ui.theme.YzBg
import com.kg.yildizname.core.ui.theme.YzBorder
import com.kg.yildizname.core.ui.theme.YzCardBg
import com.kg.yildizname.core.ui.theme.YzCardBgSel
import com.kg.yildizname.core.ui.theme.YzGold
import com.kg.yildizname.core.ui.theme.YzMuted
import com.kg.yildizname.core.ui.theme.YzOnSurface
import com.kg.yildizname.core.ui.utils.YzWindowWidth
import com.kg.yildizname.core.ui.utils.rememberWindowWidth
import horoscope.shared.generated.resources.Res
import horoscope.shared.generated.resources.onboarding_continue
import horoscope.shared.generated.resources.onboarding_pick_sign_subtitle
import horoscope.shared.generated.resources.onboarding_pick_sign_title
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
private fun ZodiacCard(
    sign: ZodiacSign,
    selected: Boolean,
    compact: Boolean,
    onClick: () -> Unit,
) {
    val borderColor = if (selected) YzGold else YzBorder
    val borderWidth = if (selected) 1.5.dp else 1.dp
    val bgColor     = if (selected) YzCardBgSel else YzCardBg
    val nameColor   = if (selected) YzGold else YzOnSurface
    val signName    = sign.localizedName()

    Box(
        modifier = Modifier
            .aspectRatio(0.78f)
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .border(borderWidth, borderColor, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier            = Modifier.padding(
                horizontal = 8.dp,
                vertical   = if (compact) 8.dp else 12.dp,
            ),
        ) {
            Image(
                painter            = painterResource(sign.drawable),
                contentDescription = signName,
                contentScale       = ContentScale.Fit,
                modifier           = Modifier.size(if (compact) 48.dp else 56.dp),
            )

            Spacer(Modifier.height(if (compact) 6.dp else 10.dp))

            Text(
                text       = signName,
                color      = nameColor,
                fontSize   = 13.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.SemiBold,
                textAlign  = TextAlign.Center,
                maxLines   = 1,
                overflow   = TextOverflow.Ellipsis,
                modifier   = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(3.dp))

            Text(
                text      = sign.localizedDateRange(),
                color     = YzMuted,
                fontSize  = 10.sp,
                textAlign = TextAlign.Center,
                maxLines  = 1,
                overflow  = TextOverflow.Ellipsis,
                modifier  = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
fun OnboardingStep1Screen(
    selectedSign: ZodiacSign?,
    onSignSelected: (ZodiacSign) -> Unit,
    onContinue: () -> Unit,
    error: String? = null,
    onErrorShown: () -> Unit = {},
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(error) {
        if (error != null) {
            snackbarHostState.showSnackbar(error)
            onErrorShown()
        }
    }

    val windowWidth = rememberWindowWidth()
    val compact     = windowWidth == YzWindowWidth.Compact
    val columns     = when (windowWidth) {
        YzWindowWidth.Compact  -> 3
        YzWindowWidth.Medium   -> 4
        YzWindowWidth.Expanded -> 6
    }

    val density = LocalDensity.current
    var buttonHeight by remember { mutableStateOf(0.dp) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier            = Modifier
                .fillMaxSize()
                .yzStatusBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(20.dp))
//            PageIndicator(currentStep = 0)
            Spacer(Modifier.height(32.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .yzTextSurfaceWash()
                    .padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text       = stringResource(Res.string.onboarding_pick_sign_title),
                    color      = YzGold,
                    fontSize   = if (compact) 28.sp else 32.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle  = FontStyle.Italic,
                    textAlign  = TextAlign.Center,
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text      = stringResource(Res.string.onboarding_pick_sign_subtitle),
                    color     = YzOnSurface.copy(alpha = 0.75f),
                    fontSize  = 15.sp,
                    textAlign = TextAlign.Center,
                    modifier  = Modifier.padding(horizontal = 24.dp),
                )
            }

            Spacer(Modifier.height(24.dp))

            LazyVerticalGrid(
                columns               = GridCells.Fixed(columns),
                modifier              = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                contentPadding        = PaddingValues(bottom = buttonHeight + 8.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement   = Arrangement.spacedBy(10.dp),
            ) {
                items(ZodiacSign.entries) { sign ->
                    ZodiacCard(
                        sign     = sign,
                        selected = sign == selectedSign,
                        compact  = compact,
                        onClick  = { onSignSelected(sign) },
                    )
                }
            }
        }

        val enabled = selectedSign != null
        Box(
            modifier         = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .onSizeChanged { buttonHeight = with(density) { it.height.toDp() } }
                .background(Brush.verticalGradient(listOf(Color.Transparent, YzBg, YzBg)))
                .yzNavigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 20.dp),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .widthIn(max = 480.dp)
                    .fillMaxWidth()
                    .height(52.dp)
                    .clip(RoundedCornerShape(50))
                    .background(if (enabled) YzGold else YzGold.copy(alpha = 0.35f))
                    .alpha(if (enabled) 1f else 0.6f)
                    .clickable(enabled = enabled) { onContinue() },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text       = stringResource(Res.string.onboarding_continue),
                    color      = if (enabled) Color(0xFF1A1400) else YzMuted,
                    fontSize   = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier  = Modifier.align(Alignment.BottomCenter).padding(16.dp),
        )
    }
}
