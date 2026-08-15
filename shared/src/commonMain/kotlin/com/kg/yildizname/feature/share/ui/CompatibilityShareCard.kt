package com.kg.yildizname.feature.share.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kg.yildizname.core.data.model.ZodiacSign
import com.kg.yildizname.core.data.model.compatGridIcon
import com.kg.yildizname.core.data.model.localizedName
import com.kg.yildizname.core.util.yzUppercase
import com.kg.yildizname.core.domain.model.tintColor
import com.kg.yildizname.core.ui.components.StaticStarField
import com.kg.yildizname.core.ui.theme.AppIcons
import com.kg.yildizname.core.ui.theme.CardShape
import com.kg.yildizname.core.ui.theme.YzBg
import com.kg.yildizname.core.ui.theme.YzBorder
import com.kg.yildizname.core.ui.theme.YzGold
import com.kg.yildizname.core.ui.theme.YzInk
import com.kg.yildizname.core.ui.theme.YzMuted
import horoscope.shared.generated.resources.Res
import horoscope.shared.generated.resources.challenges
import horoscope.shared.generated.resources.cons
import horoscope.shared.generated.resources.pros
import horoscope.shared.generated.resources.share_card_app_name
import horoscope.shared.generated.resources.share_card_app_url
import horoscope.shared.generated.resources.share_card_compat_header
import horoscope.shared.generated.resources.strengths
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


private val MedallionSize = 176.dp
private val MedallionColumnWidth = 220.dp

/** Fixed footer gap — was `Modifier.weight(1f)`, which stranded the URL far below the content
 * on a card taller than its actual content (esp. the compact card, or a detailed card with few
 * sections filled in). */
private val FooterGap = 40.dp

/** Extra card height budgeted per optional detail section, added only when that section renders. */
private val SummarySectionHeight = 120.dp
private val ParagraphSectionHeight = 110.dp
private val ProsConsSectionHeight = 230.dp

/**
 * A single labelled score row for the 2x2 tile grid. [label] is already-localized display
 * text; [value] is 0..100.
 */
data class ShareScore(val label: String, val value: Int)

/**
 * Self-contained Instagram Stories share card (9:16, 675x1200dp) for a compatibility result.
 * Rendered offscreen to a Bitmap via BitmapRender — must not depend on any parent
 * Scaffold/theme surface, and must contain NO enter/scroll animations (the bitmap is captured
 * in a single frame). Mirrors [ShareCard]'s export pattern: fixed dp/sp values, no constraint
 * shrinking. Use [CompatibilityShareCardPreview] for on-screen previews.
 *
 * Layout: wordmark → dual medallions bridged by interlocking element-tinted rings (sign name
 * sits centered under each medallion) → big match % + band label → 2x2 score tiles → verdict
 * quote → footer url.
 *
 * @param bandLabel localized CompatibilityBand headline (e.g. "Tutkulu Uyum"). Pass resolved
 *   text — this composable stays dumb.
 * @param scores exactly four rows, already localized & ordered
 *   (İletişim, Arkadaşlık, Aşk, Uzun Vadeli).
 * @param verdictText localized final-verdict sentence(s). Keep it short enough to fit ~3 lines;
 *   caller is responsible for trimming to the first sentence if the source runs long.
 * @param summary optional overview paragraph; leave blank (with [strengths]/[challenges]/[pros]/
 *   [cons] also empty) to keep the compact card. Any section passed in renders above the verdict
 *   quote and grows the card by just that section's own height.
 * @param strengths optional "what works" paragraph (localized `strengths` label as its header).
 * @param challenges optional "what to watch for" paragraph (localized `challenges` label).
 * @param pros optional short bullet list, rendered alongside [cons] as a two-column mini list
 *   (top 3 items each).
 * @param cons optional short bullet list, see [pros].
 */
@Composable
fun CompatibilityShareCard(
    signA: ZodiacSign,
    signB: ZodiacSign,
    matchPercent: Int,
    bandLabel: String,
    scores: List<ShareScore>,
    verdictText: String,
    modifier: Modifier = Modifier,
    summary: String = "",
    strengths: String = "",
    challenges: String = "",
    pros: List<String> = emptyList(),
    cons: List<String> = emptyList(),
) {
    val hasSummary = summary.isNotBlank()
    val hasStrengths = strengths.isNotBlank()
    val hasChallenges = challenges.isNotBlank()
    val hasProsCons = pros.isNotEmpty() || cons.isNotEmpty()
    val hasDetails = hasSummary || hasStrengths || hasChallenges || hasProsCons

    val extraHeight = (if (hasSummary) SummarySectionHeight else 0.dp) +
        (if (hasStrengths) ParagraphSectionHeight else 0.dp) +
        (if (hasChallenges) ParagraphSectionHeight else 0.dp) +
        (if (hasProsCons) ProsConsSectionHeight else 0.dp)
    val cardHeight = ShareCardHeight + extraHeight

    Box(
        modifier = modifier
            .width(ShareCardWidth)
            .height(cardHeight)
            .background(YzBg),
    ) {
        StaticStarField(Modifier.fillMaxSize())

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 52.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(60.dp))

            Text(
                text = stringResource(Res.string.share_card_app_name),
                color = YzGold,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(64.dp))

            CompatibilityHero(signA = signA, signB = signB)

            Spacer(Modifier.height(48.dp))

            Text(
                text = stringResource(Res.string.share_card_compat_header),
                color = YzMuted,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 6.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "$matchPercent%",
                color = YzGold,
                style = MaterialTheme.typography.displayLarge.copy(fontSize = 88.sp),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )

            Text(
                text = bandLabel.yzUppercase(),
                color = YzInk,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 3.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(44.dp))

            ScoreGrid(scores = scores)

            if (hasDetails) {
                Spacer(Modifier.height(32.dp))

                if (summary.isNotBlank()) {
                    Text(
                        text = summary,
                        maxLines = 4,
                        overflow = TextOverflow.Ellipsis,
                        color = YzInk.copy(alpha = 0.9f),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontFamily = FontFamily.Serif,
                            lineHeight = 22.sp,
                        ),
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(Modifier.height(28.dp))
                }

                if (strengths.isNotBlank()) {
                    DetailSection(
                        title = stringResource(Res.string.strengths),
                        body = strengths,
                        accentColor = YzGold,
                    )
                    Spacer(Modifier.height(20.dp))
                }

                if (challenges.isNotBlank()) {
                    DetailSection(
                        title = stringResource(Res.string.challenges),
                        body = challenges,
                        accentColor = YzMuted,
                    )
                    Spacer(Modifier.height(20.dp))
                }

                if (hasProsCons) {
                    ProsConsSection(pros = pros.take(3), cons = cons.take(3))
                    Spacer(Modifier.height(8.dp))
                }
            }

            Spacer(Modifier.height(36.dp))

            VerdictQuote(text = verdictText)

            Spacer(Modifier.height(FooterGap))

            Text(
                text = stringResource(Res.string.share_card_app_url),
                color = YzMuted,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 4.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(56.dp))
        }
    }
}

/**
 * Dual-medallion hero. Each sign is a column: an element-tinted glyph medallion with a soft
 * radial glow, and the sign name centered directly beneath it (so name and icon stay aligned
 * regardless of card width). Between the two columns sit two small interlocking rings, each in
 * one sign's element color — the "two becoming one" motif, replacing the earlier connector line.
 */
@Composable
private fun CompatibilityHero(
    signA: ZodiacSign,
    signB: ZodiacSign,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top,
    ) {
        SignColumn(sign = signA)

        // Rings vertically centered against the medallion (not the whole column with its name).
        Box(
            modifier = Modifier.height(MedallionSize),
            contentAlignment = Alignment.Center,
        ) {
            InterlockingRings(tintA = signA.element.tintColor, tintB = signB.element.tintColor)
        }

        SignColumn(sign = signB)
    }
}

@Composable
private fun SignColumn(
    sign: ZodiacSign,
    modifier: Modifier = Modifier,
) {
    val tint = sign.element.tintColor

    Column(
        modifier = modifier.width(MedallionColumnWidth),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier.size(MedallionSize),
            contentAlignment = Alignment.Center,
        ) {
            // Soft radial glow in the sign's element color.
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(tint.copy(alpha = 0.30f), Color.Transparent),
                        ),
                        shape = CircleShape,
                    ),
            )

            Box(
                modifier = Modifier
                    .size(MedallionSize - 24.dp)
                    .clip(CircleShape)
                    .background(YzBg.copy(alpha = 0.55f))
                    .border(1.dp, tint.copy(alpha = 0.55f), CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(sign.compatGridIcon),
                    contentDescription = null,
                    tint = tint,
                    modifier = Modifier.size(80.dp),
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        Text(
            text = sign.localizedName().yzUppercase(),
            color = tint,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 2.sp,
            textAlign = TextAlign.Center,
        )
    }
}

/**
 * Two overlapping ring outlines, each stroked in one sign's element color.
 */
@Composable
private fun InterlockingRings(
    tintA: Color,
    tintB: Color,
    modifier: Modifier = Modifier,
) {
    Canvas(
        modifier = modifier.size(width = 64.dp, height = 40.dp),
    ) {
        val stroke = 2.dp.toPx()
        val d = size.height - stroke
        val r = d / 2f
        val cy = size.height / 2f
        // Overlap the two rings horizontally.
        val overlap = r * 0.7f
        val cxA = size.width / 2f - overlap
        val cxB = size.width / 2f + overlap
        drawCircle(
            color = tintA.copy(alpha = 0.8f),
            radius = r,
            center = Offset(cxA, cy),
            style = Stroke(width = stroke),
        )
        drawCircle(
            color = tintB.copy(alpha = 0.8f),
            radius = r,
            center = Offset(cxB, cy),
            style = Stroke(width = stroke),
        )
    }
}

/**
 * 2x2 grid of score tiles. Each tile is just the field label + the number — no bars.
 */
@Composable
private fun ScoreGrid(
    scores: List<ShareScore>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        scores.chunked(2).forEach { rowScores ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                rowScores.forEach { s ->
                    ScoreTile(
                        label = s.label,
                        value = s.value,
                        modifier = Modifier.weight(1f),
                    )
                }
                // Keep grid aligned if a row has a single tile (shouldn't happen with 4).
                if (rowScores.size == 1) Spacer(Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun ScoreTile(
    label: String,
    value: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .clip(CardShape)
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF171B38), Color(0xFF0E1226)),
                ),
            )
            .border(0.5.dp, YzGold.copy(alpha = 0.22f), CardShape)
            .padding(horizontal = 20.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label.yzUppercase(),
            color = YzMuted,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.5.sp,
        )
        Text(
            text = "$value",
            color = YzGold,
            style = MaterialTheme.typography.headlineSmall,
        )
    }
}

/**
 * Labelled paragraph for the detailed card's extra sections (strengths/challenges): a small
 * all-caps header in [accentColor] over a body paragraph capped to 3 lines.
 */
@Composable
private fun DetailSection(
    title: String,
    body: String,
    accentColor: Color,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = title.yzUppercase(),
            color = accentColor,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 2.sp,
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = body,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            color = YzInk,
            style = MaterialTheme.typography.bodyMedium,
            lineHeight = 20.sp,
        )
    }
}

/**
 * Two-column pros/cons mini list for the detailed card — mirrors the on-screen `ProsConsCard`'s
 * icon/color language (check/positive, cross/negative) at share-card scale. Callers cap each
 * list to a handful of items; this just renders whatever it's given.
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
        ProsConsColumn(
            title = stringResource(Res.string.pros),
            items = pros,
            icon = AppIcons.Positive,
            tint = AppIcons.PositiveTint,
            modifier = Modifier.weight(1f),
        )
        ProsConsColumn(
            title = stringResource(Res.string.cons),
            items = cons,
            icon = AppIcons.Negative,
            tint = AppIcons.NegativeTint,
            modifier = Modifier.weight(1f),
        )
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
        verticalArrangement = Arrangement.spacedBy(10.dp),
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
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = YzInk,
                    style = MaterialTheme.typography.bodySmall,
                    lineHeight = 16.sp,
                )
            }
        }
    }
}

/**
 * Final-verdict quote: gold rail + serif italic text, matching the daily [ShareCard]'s quote
 * block treatment so the two cards read as a family.
 */
@Composable
private fun VerdictQuote(
    text: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .clip(CardShape)
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF1C2142), Color(0xFF0F1428)),
                ),
            )
            .border(0.5.dp, YzBorder, CardShape),
    ) {
        Box(
            modifier = Modifier
                .width(3.dp)
                .fillMaxHeight()
                .background(YzGold),
        )
        Text(
            text = "\u201C$text\u201D",
            maxLines = 6,
            overflow = TextOverflow.Ellipsis,
            color = YzInk,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontFamily = FontFamily.Serif,
                fontStyle = FontStyle.Italic,
            ),
            modifier = Modifier.padding(start = 20.dp, top = 20.dp, end = 20.dp, bottom = 20.dp),
        )
    }
}

/**
 * On-screen preview of [CompatibilityShareCard]: lays out the real 675x1200dp card at its true
 * fixed size and scales it down with a `graphicsLayer` transform, matching [ShareCardPreview].
 */

@Composable
fun CompatibilityShareCardPreview(
    signA: ZodiacSign,
    signB: ZodiacSign,
    matchPercent: Int,
    bandLabel: String,
    scores: List<ShareScore>,
    verdictText: String,
    modifier: Modifier = Modifier,
    previewHeight: Dp = 480.dp,
    summary: String = "",
    strengths: String = "",
    challenges: String = "",
    pros: List<String> = emptyList(),
    cons: List<String> = emptyList(),
) = ScaledShareCard(modifier = modifier, previewHeight = previewHeight) {
    CompatibilityShareCard(
        signA = signA,
        signB = signB,
        matchPercent = matchPercent,
        bandLabel = bandLabel,
        scores = scores,
        verdictText = verdictText,
        summary = summary,
        strengths = strengths,
        challenges = challenges,
        pros = pros,
        cons = cons,
    )
}