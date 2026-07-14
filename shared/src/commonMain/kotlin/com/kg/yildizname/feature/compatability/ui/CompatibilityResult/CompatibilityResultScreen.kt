package com.kg.yildizname.feature.compatability.ui.CompatibilityResult

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kg.yildizname.core.ui.theme.YzGold
import com.kg.yildizname.core.ui.theme.YzInk
import com.kg.yildizname.core.ui.theme.YzMuted
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Air
import androidx.compose.material.icons.rounded.Landscape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.kg.yildizname.core.data.model.compatGridIcon
import com.kg.yildizname.core.data.model.elementIcon
import com.kg.yildizname.core.data.model.localizedElementName
import com.kg.yildizname.core.data.model.localizedName
import com.kg.yildizname.core.domain.model.CompatibilityBand
import com.kg.yildizname.core.domain.model.ElementPairing
import com.kg.yildizname.core.domain.model.localizedDesc
import com.kg.yildizname.core.domain.model.tintColor
import com.kg.yildizname.core.ui.components.StarFieldBackground
import com.kg.yildizname.core.ui.utils.yzStatusBarsPadding
import com.kg.yildizname.feature.compatability.ui.components.CompatButton
import com.kg.yildizname.feature.compatability.ui.components.ResultColumn
import com.kg.yildizname.feature.compatability.ui.components.Scores
import com.kg.yildizname.feature.compatability.ui.components.SignBox
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowLeft
import horoscope.shared.generated.resources.Res
import horoscope.shared.generated.resources.btn_share
import horoscope.shared.generated.resources.capricorn_svgrepo_com
import horoscope.shared.generated.resources.cd_back
import horoscope.shared.generated.resources.compat_detailed_analysis_button
import horoscope.shared.generated.resources.compat_score_communication
import horoscope.shared.generated.resources.compat_score_friendship
import horoscope.shared.generated.resources.compat_score_long_term
import horoscope.shared.generated.resources.compat_score_love
import horoscope.shared.generated.resources.compat_subtitle
import horoscope.shared.generated.resources.compat_title
import horoscope.shared.generated.resources.gemini_svgrepo_com
import org.jetbrains.compose.resources.stringResource

@Composable
fun CompatibilityResultScreen(
    uiState: CompatibilityResultUIState,
    onBackClick: () -> Unit = {}
)
{
    when (uiState) {
        is CompatibilityResultUIState.Loading -> CompatibilityResultLoadingScreen(
            onBackClick = onBackClick
        )
        is CompatibilityResultUIState.Error -> CompatibilityResultErrorScreen(
            message = uiState.message,
            onBackClick = onBackClick
        )
        is CompatibilityResultUIState.Success -> CompatibilityResultSuccessScreen(
            uiState = uiState,
            onBackClick = onBackClick
        )
    }
}

@Composable
private fun CompatibilityResultLoadingScreen(
    onBackClick: () -> Unit = {}
) {
    StarFieldBackground()
    Box(modifier = Modifier.yzStatusBarsPadding().fillMaxSize()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = FeatherIcons.ArrowLeft,
                    contentDescription = stringResource(Res.string.cd_back),
                    tint = YzInk
                )
            }
        }
        CircularProgressIndicator(
            color = YzGold,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun CompatibilityResultErrorScreen(
    message: String,
    onBackClick: () -> Unit = {}
) {
    StarFieldBackground()
    Box(modifier = Modifier.yzStatusBarsPadding().fillMaxSize()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = FeatherIcons.ArrowLeft,
                    contentDescription = stringResource(Res.string.cd_back),
                    tint = YzInk
                )
            }
        }
        Text(
            text = message,
            color = YzMuted,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 32.dp)
        )
    }
}


@Composable
fun CompatibilityResultSuccessScreen(
    uiState: CompatibilityResultUIState.Success,
    onBackClick: () -> Unit = {}
)
{
    val signA = uiState.result.signs[0]
    val signB = uiState.result.signs[1]
    StarFieldBackground()
    Box(modifier = Modifier.yzStatusBarsPadding().fillMaxSize())
    {
        Column(modifier = Modifier
            .padding(horizontal = 8.dp)
            .padding(bottom = 32.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Row(modifier = Modifier.fillMaxWidth()) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = FeatherIcons.ArrowLeft,
                        contentDescription = stringResource(Res.string.cd_back),
                        tint = YzInk
                    )
                }
            }
            Text(
                text = stringResource(Res.string.compat_title),
                color = YzGold,
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = stringResource(Res.string.compat_subtitle),
                color = YzInk,
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(Modifier.height(16.dp))
            SignBox(
                sign= signA.localizedName(),
                icon = signA.compatGridIcon,
                element =  signA.localizedElementName()
            )
            Spacer(Modifier.height(32.dp))
            ResultColumn(
                score = uiState.result.matchPercent,
                elementA = signA.localizedElementName(),
                elementB = signB.localizedElementName(),
                iconA = signA.elementIcon,
                iconB = signB.elementIcon,
                scoreDesc = CompatibilityBand.fromScore(uiState.result.matchPercent).localizedDesc(),
                genericElementExp = ElementPairing.from(signA.element, signB.element).localizedDesc(),
                iconATint = signA.element.tintColor,
                iconBTint = signB.element.tintColor
            )
            Spacer(Modifier.height(32.dp))
            SignBox(
                sign = signB.localizedName(),
                icon = signB.compatGridIcon,
                element = signB.localizedElementName()
            )
            Spacer(Modifier.height(32.dp))
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        SpanStyle(
                            // A smooth gradient fading from a rich ink to a slightly softer tone
                            brush = Brush.linearGradient(
                                colors = listOf(YzInk, YzInk.copy(alpha = 0.85f))
                            ),
                            // Premium typography settings
                            fontFamily = FontFamily.Serif,
                            fontSize = 14.sp,
                            letterSpacing = 0.5.sp,

                        )
                    ) {
                        append(
                            uiState.result.content.summary
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                style = TextStyle(
                    lineHeight = 24.sp, // Adds elegant breathing room between lines
                    textAlign = TextAlign.Justify
                )
            )
            Spacer(Modifier.height(16.dp))
            Scores(uiState.result.scores.love, stringResource(Res.string.compat_score_love))
            Spacer(Modifier.height(8.dp))
            Scores(uiState.result.scores.communication, stringResource(Res.string.compat_score_communication))
            Spacer(Modifier.height(8.dp))
            Scores(uiState.result.scores.friendship, stringResource(Res.string.compat_score_friendship))
            Spacer(Modifier.height(8.dp))
            Scores(uiState.result.scores.longTerm, stringResource(Res.string.compat_score_long_term))
            Spacer(Modifier.height(16.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                CompatButton(
                    text = stringResource(Res.string.compat_detailed_analysis_button),
                    filled = false,
                    onClick = { }
                )

                CompatButton(
                    text = stringResource(Res.string.btn_share),
                    filled = true,
                    onClick = { }
                )
            }
        }
    }
}
/**/