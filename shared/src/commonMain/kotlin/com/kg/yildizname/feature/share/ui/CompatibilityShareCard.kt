package com.kg.yildizname.feature.share.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import com.kg.yildizname.core.ui.theme.CardShape
import com.kg.yildizname.core.ui.theme.YzBg
import com.kg.yildizname.core.ui.theme.YzBorder
import com.kg.yildizname.core.ui.theme.YzGold
import com.kg.yildizname.core.ui.theme.YzInk
import com.kg.yildizname.core.ui.theme.YzMuted
import horoscope.shared.generated.resources.Res
import horoscope.shared.generated.resources.share_card_app_name
import horoscope.shared.generated.resources.share_card_app_url
import horoscope.shared.generated.resources.share_card_compat_header
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


private val MedallionSize = 176.dp
private val MedallionColumnWidth = 220.dp

/** Fixed footer gap — was `Modifier.weight(1f)`, which stranded the URL far below the content
 * on a card taller than its actual content (esp. the compact card, or a detailed card with few
 * sections filled in). */
private val FooterGap = 40.dp

/** Extra top breathing room so the wordmark clears Instagram Stories' own status-bar/camera
 * overlay when this card is posted there — added to the top spacer, so it must also be added to
 * each card's own height budget or the footer gets squeezed off the bottom. Shared by
 * [CompatibilityShareCard] and [CompatibilityDetailedShareCard] via [CompatibilityShareCardFrame]. */
internal val TopSafeAreaExtra = 40.dp

/**
 * A single labelled score row for the 2x2 tile grid. [label] is already-localized display
 * text; [value] is 0..100.
 */
data class ShareScore(val label: String, val value: Int)

/**
 * Self-contained Instagram Stories share card (9:16, 675x1200dp) for a compatibility result's
 * quick/compact result screen. Rendered offscreen to a Bitmap via BitmapRender — must not
 * depend on any parent Scaffold/theme surface, and must contain NO enter/scroll animations (the
 * bitmap is captured in a single frame). Mirrors [ShareCard]'s export pattern: fixed dp/sp
 * values, no constraint shrinking. Use [CompatibilityShareCardPreview] for on-screen previews.
 *
 * Layout: wordmark → dual medallions bridged by interlocking element-tinted rings (sign name
 * sits centered under each medallion) → big match % + band label → 2x2 score tiles → verdict
 * quote → footer url. This is the number — the reasoning behind it lives on the separate
 * [CompatibilityDetailedShareCard], used from the detailed result screen instead of overloading
 * this one with optional paragraphs.
 *
 * @param bandLabel localized CompatibilityBand headline (e.g. "Tutkulu Uyum"). Pass resolved
 *   text — this composable stays dumb.
 * @param scores exactly four rows, already localized & ordered
 *   (İletişim, Arkadaşlık, Aşk, Uzun Vadeli).
 * @param verdictText localized final-verdict sentence(s). Keep it short enough to fit ~3 lines;
 *   caller is responsible for trimming to the first sentence if the source runs long.
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
) {
    CompatibilityShareCardFrame(
        signA = signA,
        signB = signB,
        matchPercent = matchPercent,
        bandLabel = bandLabel,
        verdictText = verdictText,
        cardHeight = ShareCardHeight + TopSafeAreaExtra,
        modifier = modifier,
    ) {
        ScoreGrid(scores = scores)
    }
}

/**
 * Shared chrome for both [CompatibilityShareCard] (compact) and [CompatibilityDetailedShareCard]
 * (detailed): wordmark → dual-medallion hero → big match % + band label → [content] → verdict
 * quote → footer url, on the shared starfield background. The two cards differ only in what they
 * pass as [content] and [cardHeight] — everything above/below that slot is identical, so it lives
 * here once instead of being duplicated or branched on optional params.
 */
@Composable
internal fun CompatibilityShareCardFrame(
    signA: ZodiacSign,
    signB: ZodiacSign,
    matchPercent: Int,
    bandLabel: String,
    verdictText: String,
    cardHeight: Dp,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
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
                .padding(horizontal = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(48.dp + TopSafeAreaExtra))

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

            content()

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
 * Final-verdict quote: gold rail + serif italic text, matching the daily [ShareCard]'s quote
 * block treatment so the two cards read as a family. Shared by both cards via
 * [CompatibilityShareCardFrame].
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
) = ScaledShareCard(modifier = modifier, previewHeight = previewHeight) {
    CompatibilityShareCard(
        signA = signA,
        signB = signB,
        matchPercent = matchPercent,
        bandLabel = bandLabel,
        scores = scores,
        verdictText = verdictText,
    )
}

