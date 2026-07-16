package com.kg.yildizname.feature.compatability.ui.CompatibilityDetailedResult

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kg.yildizname.core.data.model.compatGridIcon
import com.kg.yildizname.core.data.model.localizedElementName
import com.kg.yildizname.core.data.model.localizedName
import com.kg.yildizname.core.ui.components.StarFieldBackground
import com.kg.yildizname.core.ui.theme.AppIcons
import com.kg.yildizname.core.ui.theme.YzBgLight
import com.kg.yildizname.core.ui.theme.YzGold
import com.kg.yildizname.core.ui.theme.YzInk
import com.kg.yildizname.core.ui.theme.YzMuted
import com.kg.yildizname.core.ui.theme.YzOnSurface
import com.kg.yildizname.core.ui.utils.yzStatusBarsPadding
import com.kg.yildizname.feature.compatability.ui.CompatibilityResult.CompatibilityResultErrorScreen
import com.kg.yildizname.feature.compatability.ui.CompatibilityResult.CompatibilityResultLoadingScreen
import com.kg.yildizname.feature.compatability.ui.CompatibilityResult.CompatibilityResultSuccessScreen
import com.kg.yildizname.feature.compatability.ui.CompatibilityResult.CompatibilityResultUIState
import com.kg.yildizname.feature.compatability.ui.components.SignBox
import com.kg.yildizname.feature.compatibility.ui.components.CompatibilityScoreRing
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowLeft
import horoscope.shared.generated.resources.Res
import horoscope.shared.generated.resources.advices
import horoscope.shared.generated.resources.btn_share_analysis
import horoscope.shared.generated.resources.cd_back
import horoscope.shared.generated.resources.challenges
import horoscope.shared.generated.resources.compat_score_communication
import horoscope.shared.generated.resources.compat_score_friendship
import horoscope.shared.generated.resources.compat_score_long_term
import horoscope.shared.generated.resources.compat_title
import horoscope.shared.generated.resources.detailed_compat_title
import horoscope.shared.generated.resources.loveandintimacy
import horoscope.shared.generated.resources.strengths
import org.jetbrains.compose.resources.stringResource

@Composable
fun CompatibilityDetailedResultScreen(
    uiState: CompatibilityResultUIState,
    onShare: () -> Unit = {},
    onBack: () -> Unit
)
{
    when (uiState) {
        is CompatibilityResultUIState.Loading -> CompatibilityResultLoadingScreen(
            onBackClick = {  }
        )
        is CompatibilityResultUIState.Error -> CompatibilityResultErrorScreen(
            message = uiState.message,
            onBackClick = {  }
        )
        is CompatibilityResultUIState.Success -> CompatibilityDetailedResultSuccessScreen(
            uiState = uiState,
            onShare = {},
            onBack = onBack
        )
    }
}

@Composable
fun CompatibilityDetailedResultSuccessScreen(
    uiState: CompatibilityResultUIState.Success,
    onShare: () -> Unit = {},
    onBack: () -> Unit
) {
    val bgCard = YzBgLight.copy(0.65f)
    val signA = uiState.result.signs[0]
    val signB = uiState.result.signs[1]
    StarFieldBackground()
    Box(modifier = Modifier.yzStatusBarsPadding().fillMaxSize())
    {
        Column(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .padding(bottom = 32.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Row(modifier = Modifier.fillMaxWidth()) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = FeatherIcons.ArrowLeft,
                        contentDescription = stringResource(Res.string.cd_back),
                        tint = YzInk
                    )
                }
            }
            Text(
                text = stringResource(Res.string.detailed_compat_title),
                color = YzGold,
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    SignBox(
                        sign = signA.localizedName(),
                        icon = signA.compatGridIcon,
                        element = signA.localizedElementName(),
                    )
                }
                Box(modifier = Modifier.weight(0.7f), contentAlignment = Alignment.Center) {
                    CompatibilityScoreRing(
                        matchPercent = uiState.result.matchPercent,
                    )
                }
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    SignBox(
                        sign = signB.localizedName(),
                        icon = signB.compatGridIcon,
                        element = signB.localizedElementName(),
                    )
                }
            }
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
            Spacer(Modifier.height(32.dp))
            ScoreCard(
                uiState.result.scores.love,
                uiState.result.scores.communication,
                uiState.result.scores.friendship,
                uiState.result.scores.longTerm
            )
            Spacer(Modifier.height(32.dp))
            InfoCards(
                headlineIcon = AppIcons.Recommendations,
                contentLineIcon = AppIcons.Bullet,
                contentLineIconTint = YzGold.copy(0.75f),
                textList = uiState.result.content.advice.split(".").map { it.trim() }.filter { it.isNotBlank() },
                backgroundColor = bgCard,
                headlineText = stringResource(Res.string.advices),
                headLineIconTint = YzGold,
                iconOffset = (7).dp
            )
            Spacer(Modifier.height(16.dp))
            InfoCards(
                contentLineIcon = AppIcons.Compatible,
                contentLineIconTint = YzGold,
                textList = uiState.result.content.strengths.split(".").map { it.trim() }.filter { it.isNotBlank() },
                backgroundColor = bgCard,
                headlineText = stringResource(Res.string.strengths),
                iconOffset = (7).dp,
                iconSize = 12.dp
            )
            Spacer(Modifier.height(16.dp))
            InfoCards(
                contentLineIcon = AppIcons.Incompatible,
                contentLineIconTint = Color.Red,
                textList = uiState.result.content.challenges.split(".").map { it.trim() }.filter { it.isNotBlank() },
                backgroundColor = bgCard,
                headlineText = stringResource(Res.string.challenges),
                iconOffset = (7).dp,
                iconSize = 12.dp
            )
            Spacer(Modifier.height(16.dp))
            InfoCards(
                headlineIcon = AppIcons.Love,
                contentLineIcon = null,
                contentLineIconTint = null,
                headLineIconTint = AppIcons.LoveTint,
                textList = uiState.result.content.loveAndIntimacy.split(".").map { it.trim() }.filter { it.isNotBlank() },
                backgroundColor = bgCard,
                headlineText = stringResource(Res.string.loveandintimacy),
                iconOffset = null
            )
            Spacer(Modifier.height(16.dp))
            InfoCards(
                headlineIcon = AppIcons.Communication,
                contentLineIcon = null,
                contentLineIconTint = null,
                headLineIconTint = AppIcons.CommunicationTint,
                textList = uiState.result.content.communication.split(".").map { it.trim() }.filter { it.isNotBlank() },
                backgroundColor = bgCard,
                headlineText = stringResource(Res.string.compat_score_communication),
                iconOffset = null
            )
            Spacer(Modifier.height(16.dp))
            InfoCards(
                headlineIcon = AppIcons.Friendship,
                contentLineIcon = null,
                contentLineIconTint = null,
                headLineIconTint = AppIcons.FriendshipTint,
                textList = uiState.result.content.friendship.split(".").map { it.trim() }.filter { it.isNotBlank() },
                backgroundColor = bgCard,
                headlineText = stringResource(Res.string.compat_score_friendship),
                iconOffset = null
            )
            Spacer(Modifier.height(16.dp))
            InfoCards(
                headlineIcon = AppIcons.LongTerm,
                contentLineIcon = null,
                contentLineIconTint = null,
                headLineIconTint = AppIcons.LongTermTint,
                textList = uiState.result.content.longTerm.split(".").map { it.trim() }.filter { it.isNotBlank() },
                backgroundColor = bgCard,
                headlineText = stringResource(Res.string.compat_score_long_term),
                iconOffset = null
            )
            Spacer(Modifier.height(16.dp))
            ProsConsCard(
                uiState.result.content.pros,
                uiState.result.content.cons
            )
            Spacer(Modifier.height(32.dp))
            FinalVerdictCard(
                text = uiState.result.content.finalVerdict,
                onClick = onShare,
                headLine = "Göklerin Kararı"
            )
        }
    }
}

@Composable
fun FinalVerdictCard(
    text: String,
    onClick: () -> Unit,
    headLine: String,
    enabled: Boolean = true
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFF1A1920),
                        Color(0xFF17171F),
                        Color(0xFF14141E)
                    )
                )
            )
            .border(1.dp, YzGold, RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(vertical = 32.dp, horizontal = 32.dp)
        ) {
            Text(
                text = headLine,
                color = YzGold,
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = text,
                color = YzOnSurface,
                fontSize = 19.sp,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(32.dp))
            Box(
                modifier = Modifier
                    .height(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (enabled) YzGold else YzGold.copy(alpha = 0.35f))
                    .clickable(enabled = enabled, onClick = onClick)
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.btn_share_analysis),
                        color = if (enabled) Color.Black else YzMuted,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Icon(
                        imageVector = Icons.Rounded.Share,
                        contentDescription = null,
                        tint = if (enabled) Color.Black else YzMuted,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}