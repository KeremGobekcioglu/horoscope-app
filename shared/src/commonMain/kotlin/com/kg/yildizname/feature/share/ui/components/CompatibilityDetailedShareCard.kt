package com.kg.yildizname.feature.share.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kg.yildizname.core.data.model.ZodiacSign
import com.kg.yildizname.core.util.yzUppercase
import com.kg.yildizname.core.ui.theme.AppIcons
import com.kg.yildizname.core.ui.theme.CardShape
import com.kg.yildizname.core.ui.theme.YzBorder
import com.kg.yildizname.core.ui.theme.YzGold
import com.kg.yildizname.core.ui.theme.YzInk
import com.kg.yildizname.core.ui.theme.YzMuted
import com.kg.yildizname.feature.share.ui.components.CompatibilityShareCardFrame
import horoscope.shared.generated.resources.Res
import horoscope.shared.generated.resources.challenges
import horoscope.shared.generated.resources.cons
import horoscope.shared.generated.resources.pros
import horoscope.shared.generated.resources.strengths
import org.jetbrains.compose.resources.stringResource

/**
 * Self-contained Instagram Stories share card (675dp wide, height grows to fit content) for a
 * compatibility result's *detailed* result screen — a separate composable from
 * [com.kg.yildizname.feature.share.ui.components.CompatibilityShareCard] rather than that card branching on optional params, because the two
 * genuinely carry different payloads: compact is the number, this one is the reasoning behind
 * it. Same offscreen-render constraints as [com.kg.yildizname.feature.share.ui.components.CompatibilityShareCard] (no parent Scaffold/theme
 * dependency, no enter/scroll animations). Use [CompatibilityDetailedShareCardPreview] for
 * on-screen previews.
 *
 * Layout: wordmark → dual medallions bridged by interlocking element-tinted rings → big match %
 * + band label → summary line → strengths/challenges → pros/cons → verdict quote → footer url.
 * [ScoreGrid] never renders here — repeating the four scores from the compact card is what made
 * the two exports look identical before this split.
 *
 * @param bandLabel localized CompatibilityBand headline (e.g. "Tutkulu Uyum"). Pass resolved
 *   text — this composable stays dumb.
 * @param verdictText localized final-verdict sentence(s). Keep it short enough to fit ~3 lines;
 *   caller is responsible for trimming to the first sentence if the source runs long.
 * @param summary one-line takeaway, rendered as the sole uncontained text block on the card.
 * @param strengths "what works" paragraph (localized `strengths` label as its header).
 * @param challenges "what to watch for" paragraph (localized `challenges` label as its header).
 * @param pros short bullet fragments, capped at three for display.
 * @param cons short bullet fragments, capped at three for display.
 */
@Composable
fun CompatibilityDetailedShareCard(
    signA: ZodiacSign,
    signB: ZodiacSign,
    matchPercent: Int,
    bandLabel: String,
    verdictText: String,
    summary: String,
    strengths: String,
    challenges: String,
    pros: List<String>,
    cons: List<String>,
    modifier: Modifier = Modifier,
) {
    CompatibilityShareCardFrame(
        signA = signA,
        signB = signB,
        matchPercent = matchPercent,
        bandLabel = bandLabel,
        verdictText = verdictText,
        modifier = modifier,
    ) {
        var needsSpacer = false

        if (summary.isNotBlank()) {
            SummaryLine(text = summary)
            needsSpacer = true
        }

        if (strengths.isNotBlank() || challenges.isNotBlank()) {
            if (needsSpacer) Spacer(Modifier.height(24.dp))
            StrengthsChallengesSection(strengths = strengths, challenges = challenges)
            needsSpacer = true
        }

        if (pros.isNotEmpty() || cons.isNotEmpty()) {
            if (needsSpacer) Spacer(Modifier.height(24.dp))
            ProsConsSection(pros = pros, cons = cons)
        }
    }
}

/**
 * Single centered takeaway line above strengths/challenges — the one uncontained text block on
 * the detailed card, giving the eye a rest between the hero/score-band and the boxed sections
 * below. No card/border, deliberately, unlike every other detail block. No `maxLines`/ellipsis —
 * the card's height wraps its content (see [CompatibilityShareCardFrame]), so there is no fixed
 * budget this text needs to fit inside; capping it here would silently drop real copy instead.
 */
@Composable
private fun SummaryLine(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        color = YzInk.copy(alpha = 0.85f),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodyLarge.copy(fontFamily = FontFamily.Serif),
        modifier = modifier.fillMaxWidth(),
    )
}

/**
 * Strengths/challenges as a single bordered two-column card (mirrors [ProsConsSection]'s
 * treatment) instead of two stacked bare paragraphs — gives the text a visible boundary so it
 * doesn't read as crammed against the summary above and pros/cons below. Renders only the
 * column(s) whose text is non-blank; a lone column still fills the row via `weight(1f)`.
 */
@Composable
private fun StrengthsChallengesSection(
    strengths: String,
    challenges: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(CardShape)
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF171B38), Color(0xFF0E1226)),
                ),
            )
            .border(0.5.dp, YzBorder, CardShape)
            .padding(horizontal = 20.dp, vertical = 18.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        if (strengths.isNotBlank()) {
            DetailColumn(
                title = stringResource(Res.string.strengths),
                body = strengths,
                accentColor = YzGold,
                modifier = Modifier.weight(1f),
            )
        }
        if (challenges.isNotBlank()) {
            DetailColumn(
                title = stringResource(Res.string.challenges),
                body = challenges,
                accentColor = YzMuted,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun DetailColumn(
    title: String,
    body: String,
    accentColor: Color,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = title.yzUppercase(),
            color = accentColor,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 1.sp,
        )
        Text(
            text = body,
            color = YzInk,
            style = MaterialTheme.typography.bodySmall,
            lineHeight = 17.sp,
        )
    }
}

/**
 * Pros/cons as a single bordered two-column card, mirroring [StrengthsChallengesSection]'s
 * treatment and the on-screen `ProsConsCard`. Each list is capped at three items — short bullet
 * fragments, so the cap keeps the card from growing unpredictably with content length.
 */
@Composable
private fun ProsConsSection(
    pros: List<String>,
    cons: List<String>,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(CardShape)
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF171B38), Color(0xFF0E1226)),
                ),
            )
            .border(0.5.dp, YzBorder, CardShape)
            .padding(horizontal = 20.dp, vertical = 18.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        if (pros.isNotEmpty()) {
            ProsConsColumn(
                title = stringResource(Res.string.pros),
                items = pros.take(3),
                icon = AppIcons.Positive,
                tint = AppIcons.PositiveTint,
                modifier = Modifier.weight(1f),
            )
        }
        if (cons.isNotEmpty()) {
            ProsConsColumn(
                title = stringResource(Res.string.cons),
                items = cons.take(3),
                icon = AppIcons.Negative,
                tint = AppIcons.NegativeTint,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun ProsConsColumn(
    title: String,
    items: List<String>,
    icon: ImageVector,
    tint: Color,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = title.yzUppercase(),
            color = tint,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 1.sp,
        )
        items.forEach { item ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.Top,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = tint,
                    modifier = Modifier
                        .padding(top = 2.dp)
                        .size(11.dp),
                )
                Text(
                    text = item,
                    color = YzInk,
                    style = MaterialTheme.typography.bodySmall,
                    lineHeight = 17.sp,
                )
            }
        }
    }
}

/**
 * On-screen preview of [CompatibilityDetailedShareCard]: lays out the real fixed-size card and
 * scales it down with a `graphicsLayer` transform, matching [com.kg.yildizname.feature.share.ui.components.CompatibilityShareCardPreview].
 */
@Composable
fun CompatibilityDetailedShareCardPreview(
    signA: ZodiacSign,
    signB: ZodiacSign,
    matchPercent: Int,
    bandLabel: String,
    verdictText: String,
    summary: String,
    strengths: String,
    challenges: String,
    pros: List<String>,
    cons: List<String>,
    modifier: Modifier = Modifier,
    previewHeight: Dp = 480.dp,
) = _root_ide_package_.com.kg.yildizname.feature.share.ui.ScaledShareCard(
    modifier = modifier,
    previewHeight = previewHeight
) {
    CompatibilityDetailedShareCard(
        signA = signA,
        signB = signB,
        matchPercent = matchPercent,
        bandLabel = bandLabel,
        verdictText = verdictText,
        summary = summary,
        strengths = strengths,
        challenges = challenges,
        pros = pros,
        cons = cons,
    )
}
