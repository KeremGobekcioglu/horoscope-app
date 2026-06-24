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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kg.yildizname.core.domain.model.ZodiacSign
import com.kg.yildizname.core.domain.model.ZodiacSigns
import com.kg.yildizname.core.ui.components.PageIndicator
import com.kg.yildizname.core.ui.components.StarFieldBackground
import com.kg.yildizname.core.ui.theme.YzBg
import com.kg.yildizname.core.ui.theme.YzBorder
import com.kg.yildizname.core.ui.theme.YzCardBg
import com.kg.yildizname.core.ui.theme.YzCardBgSel
import com.kg.yildizname.core.ui.theme.YzGold
import com.kg.yildizname.core.ui.theme.YzMuted
import com.kg.yildizname.core.ui.theme.YzOnSurface
import org.jetbrains.compose.resources.painterResource

@Composable
private fun ZodiacCard(
    sign: ZodiacSign,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val borderColor = if (selected) YzGold else YzBorder
    val borderWidth = if (selected) 1.5.dp else 1.dp
    val bgColor     = if (selected) YzCardBgSel else YzCardBg
    val nameColor   = if (selected) YzGold else YzOnSurface

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
            modifier            = Modifier.padding(horizontal = 8.dp, vertical = 12.dp),
        ) {
            Image(
                painter            = painterResource(sign.drawable),
                contentDescription = sign.nameTr,
                contentScale       = ContentScale.Fit,
                modifier           = Modifier.size(56.dp),
            )

            Spacer(Modifier.height(10.dp))

            Text(
                text       = sign.nameTr,
                color      = nameColor,
                fontSize   = 13.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.SemiBold,
                textAlign  = TextAlign.Center,
                maxLines   = 1,
            )

            Spacer(Modifier.height(3.dp))

            Text(
                text      = sign.dateRange,
                color     = YzMuted,
                fontSize  = 10.sp,
                textAlign = TextAlign.Center,
                maxLines  = 1,
            )
        }
    }
}

@Composable
fun OnboardingStep1Screen(
    selectedSign: ZodiacSign?,
    onSignSelected: (ZodiacSign) -> Unit,
    onContinue: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(YzBg),
    ) {
        StarFieldBackground(modifier = Modifier.fillMaxSize())

        Column(
            modifier            = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(20.dp))
            PageIndicator(currentStep = 0)
            Spacer(Modifier.height(32.dp))

            Text(
                text       = "Burcunu seç",
                color      = YzGold,
                fontSize   = 32.sp,
                fontWeight = FontWeight.Bold,
                fontStyle  = FontStyle.Italic,
                textAlign  = TextAlign.Center,
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text      = "Sana özel günlük rehberin başlasın",
                color     = YzOnSurface.copy(alpha = 0.75f),
                fontSize  = 15.sp,
                textAlign = TextAlign.Center,
                modifier  = Modifier.padding(horizontal = 24.dp),
            )

            Spacer(Modifier.height(24.dp))

            LazyVerticalGrid(
                columns               = GridCells.Fixed(3),
                modifier              = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                contentPadding        = PaddingValues(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement   = Arrangement.spacedBy(10.dp),
            ) {
                items(ZodiacSigns) { sign ->
                    ZodiacCard(
                        sign     = sign,
                        selected = sign == selectedSign,
                        onClick  = { onSignSelected(sign) },
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(listOf(Color.Transparent, YzBg, YzBg))
                )
                .padding(horizontal = 20.dp, vertical = 20.dp),
        ) {
            val enabled = selectedSign != null
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .clip(RoundedCornerShape(50))
                    .background(if (enabled) YzGold else YzGold.copy(alpha = 0.35f))
                    .alpha(if (enabled) 1f else 0.6f)
                    .clickable(enabled = enabled) { onContinue() },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text       = "Devam Et",
                    color      = if (enabled) Color(0xFF1A1400) else YzMuted,
                    fontSize   = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}
