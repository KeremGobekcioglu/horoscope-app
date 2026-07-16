package com.kg.yildizname.feature.share.ui

import com.kg.yildizname.core.domain.model.localizedName
import com.kg.yildizname.core.domain.model.tintColor


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
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kg.yildizname.core.data.model.ZodiacSign
import com.kg.yildizname.core.data.model.compatGridIcon
import com.kg.yildizname.core.data.model.localizedName
import com.kg.yildizname.core.ui.components.StaticStarField
import com.kg.yildizname.core.ui.theme.CardShape
import com.kg.yildizname.core.ui.theme.YzBg
import com.kg.yildizname.core.ui.theme.YzBorder
import com.kg.yildizname.core.ui.theme.YzGold
import com.kg.yildizname.core.ui.theme.YzInk
import com.kg.yildizname.core.ui.theme.YzMuted
import compose.icons.FeatherIcons
import compose.icons.feathericons.Star
import horoscope.shared.generated.resources.Res
import horoscope.shared.generated.resources.share_card_app_name
import horoscope.shared.generated.resources.share_card_app_url
import kotlin.math.roundToInt
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

private val ShareCardWidth = 675.dp
private val ShareCardHeight = 1200.dp
private val MedallionSize = 190.dp

/**
 * Self-contained Instagram Stories share card (9:16, 675x1200dp) for a compatibility result.
 * Rendered offscreen to a Bitmap via BitmapRender — must not depend on any parent
 * Scaffold/theme surface. Mirrors [ShareCard]'s export pattern exactly: fixed dp/sp values,
 * no constraint shrinking. Never compose directly in visible UI — use
 * [CompatibilityShareCardPreview] for on-screen previews.
 */
@Composable
fun CompatibilityShareCard(
    signA: ZodiacSign,
    signB: ZodiacSign,
    matchPercent: Int,
    quoteText: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .width(ShareCardWidth)
            .height(ShareCardHeight)
            .background(YzBg),
    ) {
        StaticStarField(Modifier.fillMaxSize())

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(48.dp))

            Text(
                text = stringResource(Res.string.share_card_app_name),
                color = YzGold,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(56.dp))

            CompatibilityHero(signA = signA, signB = signB)

            Spacer(Modifier.height(28.dp))

            SignNameRow(signA = signA, signB = signB)

            Spacer(Modifier.height(48.dp))

            Text(
                text = "UYUM ANALİZİ",
                color = YzMuted,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 6.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = "$matchPercent% UYUM",
                color = YzGold,
                style = MaterialTheme.typography.displayLarge.copy(fontSize = 68.sp),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(20.dp))

            ElementPairRow(signA = signA, signB = signB)

            Spacer(Modifier.height(52.dp))

            CompatibilityQuoteBlock(quoteText = quoteText)

            Spacer(Modifier.height(40.dp))

            Text(
                text = stringResource(Res.string.share_card_app_url),
                color = YzMuted,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 4.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(48.dp))
        }
    }
}

/**
 * Dual-medallion hero: two zodiac glyphs, each ringed by a soft radial glow tinted to that
 * sign's element color, bridged by a thin gold arc with a star glyph at its apex. Reads as
 * "two orbits crossing" rather than the in-app CompatibilityScoreRing — this is a deliberately
 * different, more decorative treatment reserved for the export asset.
 */
@Composable
private fun CompatibilityHero(
    signA: ZodiacSign,
    signB: ZodiacSign,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(260.dp),
        contentAlignment = Alignment.TopCenter,
    ) {
        // Bridging arc, drawn behind both medallions, spanning the full width.
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(MedallionSize / 2 + 40.dp)
                .padding(top = 40.dp),
        ) {
            val strokeWidth = 1.5.dp.toPx()
            val margin = MedallionSize.toPx() / 2f + 8.dp.toPx()
            val start = Offset(margin, size.height * 0.15f)
            val end = Offset(size.width - margin, size.height * 0.15f)
            val controlY = size.height * 1.35f

            val path = androidx.compose.ui.graphics.Path().apply {
                moveTo(start.x, start.y)
                quadraticBezierTo(size.width / 2f, controlY, end.x, end.y)
            }

            drawPath(
                path = path,
                color = YzGold.copy(alpha = 0.45f),
                style = Stroke(width = strokeWidth),
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Top,
        ) {
            SignMedallion(sign = signA)
            SignMedallion(sign = signB)
        }

        Icon(
            imageVector = FeatherIcons.Star,
            contentDescription = null,
            tint = YzGold,
            modifier = Modifier
                .padding(top = 4.dp)
                .size(18.dp),
        )
    }
}

@Composable
private fun SignMedallion(
    sign: ZodiacSign,
    modifier: Modifier = Modifier,
) {
    val tint = sign.element.tintColor

    Box(
        modifier = modifier.size(MedallionSize),
        contentAlignment = Alignment.Center,
    ) {
        // Soft radial glow in the sign's element color.
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(tint.copy(alpha = 0.35f), Color.Transparent),
                    ),
                    shape = CircleShape,
                ),
        )

        Box(
            modifier = Modifier
                .size(MedallionSize - 24.dp)
                .clip(CircleShape)
                .background(YzBg.copy(alpha = 0.5f))
                .border(1.dp, tint.copy(alpha = 0.6f), CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(sign.compatGridIcon),
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(84.dp),
            )
        }
    }
}

@Composable
private fun SignNameRow(
    signA: ZodiacSign,
    signB: ZodiacSign,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        Text(
            text = signA.localizedName().uppercase(),
            color = signA.element.tintColor,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 2.sp,
        )
        Text(
            text = signB.localizedName().uppercase(),
            color = signB.element.tintColor,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 2.sp,
        )
    }
}

@Composable
private fun ElementPairRow(
    signA: ZodiacSign,
    signB: ZodiacSign,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        ElementDot(color = signA.element.tintColor)
        Text(
            text = "${signA.element.localizedName()} + ${signB.element.localizedName()}",
            color = YzMuted,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            letterSpacing = 1.sp,
        )
        ElementDot(color = signB.element.tintColor)
    }
}

@Composable
private fun ElementDot(color: Color) {
    Box(
        modifier = Modifier
            .size(8.dp)
            .clip(CircleShape)
            .background(color),
    )
}

/**
 * On-screen preview of [CompatibilityShareCard]: lays out the real 675x1200dp card at its
 * true fixed size and scales it down with a `graphicsLayer` transform, matching
 * [ShareCardPreview]'s approach exactly.
 */
private val ShareCardTrueHeight = 1200.dp

@Composable
fun CompatibilityShareCardPreview(
    signA: ZodiacSign,
    signB: ZodiacSign,
    matchPercent: Int,
    quoteText: String,
    modifier: Modifier = Modifier,
    previewHeight: Dp = 480.dp,
) {
    val scale = previewHeight / ShareCardTrueHeight

    Layout(
        modifier = modifier.clip(CardShape),
        content = {
            CompatibilityShareCard(
                signA = signA,
                signB = signB,
                matchPercent = matchPercent,
                quoteText = quoteText,
            )
        },
    ) { measurables, _ ->
        val placeable = measurables.first().measure(Constraints())
        val scaledW = (placeable.width * scale).roundToInt()
        val scaledH = (placeable.height * scale).roundToInt()

        layout(scaledW, scaledH) {
            placeable.placeWithLayer(0, 0) {
                scaleX = scale
                scaleY = scale
                transformOrigin = TransformOrigin(0f, 0f)
            }
        }
    }
}

@Composable
private fun CompatibilityQuoteBlock(
    quoteText: String,
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
        Column(
            modifier = Modifier.padding(start = 20.dp, top = 20.dp, end = 20.dp, bottom = 16.dp),
        ) {
            Text(
                text = "\u201C$quoteText\u201D",
                color = YzInk,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontFamily = FontFamily.Serif,
                    fontStyle = FontStyle.Italic,
                ),
            )
            Spacer(Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = FeatherIcons.Star,
                    contentDescription = null,
                    tint = YzGold,
                    modifier = Modifier.size(14.dp),
                )
                Spacer(Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .width(24.dp)
                        .height(1.dp)
                        .background(YzGold),
                )
            }
        }
    }
}