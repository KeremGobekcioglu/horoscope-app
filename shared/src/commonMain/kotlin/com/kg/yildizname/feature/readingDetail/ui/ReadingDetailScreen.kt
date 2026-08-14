package com.kg.yildizname.feature.readingDetail.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kg.yildizname.core.ui.components.yzTextSurfaceWash
import com.kg.yildizname.core.ui.theme.CardShape
import com.kg.yildizname.core.ui.theme.YzBorder
import com.kg.yildizname.core.ui.theme.YzGold
import com.kg.yildizname.core.ui.theme.YzMuted
import com.kg.yildizname.core.ui.theme.YzSurface
import com.kg.yildizname.core.ui.utils.DateFormatter
import com.kg.yildizname.core.ui.utils.YzWindowWidth
import com.kg.yildizname.core.ui.utils.rememberWindowWidth
import com.kg.yildizname.core.util.DateUtils
import com.kg.yildizname.feature.home.ui.components.ConstellationHero
import compose.icons.FeatherIcons
import compose.icons.feathericons.Activity
import compose.icons.feathericons.Briefcase
import compose.icons.feathericons.Heart
import compose.icons.feathericons.Star
import compose.icons.feathericons.Sun
import horoscope.shared.generated.resources.Res
import horoscope.shared.generated.resources.reading_detail_section_career
import horoscope.shared.generated.resources.reading_detail_section_general
import horoscope.shared.generated.resources.reading_detail_section_health
import horoscope.shared.generated.resources.reading_detail_section_love
import horoscope.shared.generated.resources.reading_detail_section_luck
import horoscope.shared.generated.resources.reading_detail_zodyakin_gizemi
import org.jetbrains.compose.resources.stringResource

@Composable
fun ReadingDetailScreen(
    uiState: ReadingDetailUiState,
    onBackClick: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
//    when (uiState) {
//        is ReadingDetailUiState.Loading -> ReadingDetailLoadingScreen(
//            onBackClick = onBackClick,
//            modifier = modifier,
//        )
//        is ReadingDetailUiState.Error -> ReadingDetailErrorScreen(
//            message = uiState.message,
//            onBackClick = onBackClick,
//            modifier = modifier,
//        )
//        is ReadingDetailUiState.Success -> ReadingDetailSuccessScreen(
//            uiState = uiState,
//            onBackClick = onBackClick,
//            onShareClick = onShareClick,
//            modifier = modifier,
//        )
//    }
    if(uiState.isLoading)
    {
        Box(modifier = modifier.fillMaxSize()) {
            ReadingDetailTopBar(
                signName = "",
                periodLabel = "",
                onBackClick = onBackClick,
                modifier = Modifier.align(Alignment.TopStart),
            )
            CircularProgressIndicator(
                color = YzGold,
                modifier = Modifier.align(Alignment.Center),
            )
        }
    }
    else if(!uiState.err.isNullOrEmpty())
    {
        Box(modifier = modifier.fillMaxSize()) {
            ReadingDetailTopBar(
                signName = "",
                periodLabel = "",
                onBackClick = onBackClick,
                modifier = Modifier.align(Alignment.TopStart),
            )
            Text(
                text = uiState.err,
                color = YzMuted,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 32.dp),
            )
        }
    }
    else {
        ReadingDetailSuccessScreen(
            uiState = uiState,
            onBackClick = onBackClick,
            onShareClick = onShareClick,
            modifier = modifier,
        )
}
}

@Composable
private fun ReadingDetailSuccessScreen(
    uiState: ReadingDetailUiState,
    onBackClick: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val windowWidth = rememberWindowWidth()
    val contentMaxWidth: Dp = when (windowWidth) {
        YzWindowWidth.Compact  -> Dp.Infinity
        YzWindowWidth.Medium   -> 560.dp
        YzWindowWidth.Expanded -> 520.dp
    }

    Scaffold(
        modifier = modifier,
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets(0),
        topBar = {
            ReadingDetailTopBar(
                signName = uiState.signDisplayName,
                periodLabel = uiState.periodLabel,
                onBackClick = onBackClick,
                date = uiState.formattedDate
            )
        },
        bottomBar = {
            SharePillButton(onClick = onShareClick)
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.TopCenter,
        ) {
            Column(
                modifier = Modifier
                    .let { if (contentMaxWidth != Dp.Infinity) it.widthIn(max = contentMaxWidth) else it }
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(Modifier.height(12.dp))

                ConstellationHero(
                    sign = uiState.sign,
                    modifier = Modifier
                        .fillMaxWidth(0.65f)
                        .aspectRatio(1f),
                )

                Spacer(Modifier.height(20.dp))

                Text(
                    text = stringResource(Res.string.reading_detail_zodyakin_gizemi),
                    color = YzGold,
                    style = MaterialTheme.typography.labelSmall,
                    letterSpacing = 2.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(Modifier.height(12.dp))

                LuckChipsRow(
                    luckyNumber = uiState.luckyNumber,
                    luckyColorName = uiState.luckyColorName,
                )

                Spacer(Modifier.height(28.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(CardShape)
                        .background(YzSurface)
                        .border(0.5.dp, YzBorder, CardShape)
                        .padding(horizontal = 20.dp),
                ) {
                    ReadingSection(
                        icon = FeatherIcons.Sun,
                        title = stringResource(Res.string.reading_detail_section_general),
                        body = uiState.generalText,
                    )
                    HorizontalDivider(color = YzBorder, thickness = 1.dp)
                    ReadingSection(
                        icon = FeatherIcons.Heart,
                        title = stringResource(Res.string.reading_detail_section_love),
                        body = uiState.loveText,
                    )
                    HorizontalDivider(color = YzBorder, thickness = 1.dp)
                    ReadingSection(
                        icon = FeatherIcons.Briefcase,
                        title = stringResource(Res.string.reading_detail_section_career),
                        body = uiState.careerText,
                    )
                    HorizontalDivider(color = YzBorder, thickness = 1.dp)
                    ReadingSection(
                        icon = FeatherIcons.Activity,
                        title = stringResource(Res.string.reading_detail_section_health),
                        body = uiState.healthText,
                    )
                    HorizontalDivider(color = YzBorder, thickness = 1.dp)
                    ReadingSection(
                        icon = FeatherIcons.Star,
                        title = stringResource(Res.string.reading_detail_section_luck),
                        body = uiState.luckText,
                    )
                }

                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun ReadingDetailLoadingScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        ReadingDetailTopBar(
            signName = "",
            periodLabel = "",
            onBackClick = onBackClick,
            modifier = Modifier.align(Alignment.TopStart),
        )
        CircularProgressIndicator(
            color = YzGold,
            modifier = Modifier.align(Alignment.Center),
        )
    }
}

@Composable
private fun ReadingDetailErrorScreen(
    message: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        ReadingDetailTopBar(
            signName = "",
            periodLabel = "",
            onBackClick = onBackClick,
            modifier = Modifier.align(Alignment.TopStart),
        )
        Text(
            text = message,
            color = YzMuted,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 32.dp),
        )
    }
}
