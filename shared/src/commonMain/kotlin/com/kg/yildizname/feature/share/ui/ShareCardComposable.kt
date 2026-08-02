package com.kg.yildizname.feature.share.ui

import com.kg.yildizname.core.ui.utils.DateFormatter
import com.kg.yildizname.core.ui.utils.Language
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
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
import com.kg.yildizname.core.data.model.localizedName
import com.kg.yildizname.core.ui.components.StaticStarField
import com.kg.yildizname.core.util.currentLanguageCode
import com.kg.yildizname.core.util.yzUppercase
import kotlin.math.roundToInt
import com.kg.yildizname.core.ui.theme.CardShape
import com.kg.yildizname.core.ui.theme.YzBg
import com.kg.yildizname.core.ui.theme.YzBorder
import com.kg.yildizname.core.ui.theme.YzGold
import com.kg.yildizname.core.ui.theme.YzInk
import com.kg.yildizname.core.ui.theme.YzMuted
import com.kg.yildizname.feature.home.ui.components.ConstellationHero
import compose.icons.FeatherIcons
import compose.icons.feathericons.Star
import horoscope.shared.generated.resources.Res
import horoscope.shared.generated.resources.share_card_app_name
import horoscope.shared.generated.resources.share_card_app_url
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.stringResource



/**
 * Self-contained Instagram Stories share card (9:16, 675x1200dp). Rendered offscreen to a
 * Bitmap via BitmapRender — must not depend on any parent Scaffold/theme surface.
 *
 * This is the literal export asset: its children use fixed dp/sp values and do not participate
 * in Compose constraint shrinking, so squeezing it into a smaller box clips content instead of
 * scaling it. Never compose this directly in visible UI — use [ShareCardPreview] for on-screen
 * previews, and reserve this composable for the offscreen bitmap render path.
 */
@Composable
fun ShareCard(
    sign: ZodiacSign,
    date: LocalDate,
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

            Spacer(Modifier.height(64.dp))

            ConstellationHero(
                sign = sign,
                modifier = Modifier
                    .fillMaxWidth(0.65f)
                    .aspectRatio(1f),
            )

            Spacer(Modifier.height(80.dp))

            Text(
                text = sign.localizedName().yzUppercase(),
                color = YzGold,
                style = MaterialTheme.typography.displayLarge.copy(fontSize = 64.sp),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = DateFormatter.formatDate(
                    date.toString(),
                    if (currentLanguageCode() == "tr") Language.TURKISH else Language.ENGLISH,
                ),
                color = YzMuted,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(60.dp))

            ShareCardQuoteBlock(quoteText = quoteText)

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
 * On-screen preview of [ShareCard]: lays out the real 675x1200dp card at its true fixed size
 * (so none of its dp/sp children get squeezed) and then visually scales the whole result down
 * with a `graphicsLayer` transform to fit [modifier]'s available width. This is a scale, not a
 * shrink — nothing needs to be clipped because the card's own layout never sees tighter
 * constraints than 675x1200dp.
 */

@Composable
fun ShareCardPreview(
    sign: ZodiacSign,
    date: LocalDate,
    quoteText: String,
    modifier: Modifier = Modifier,
    previewHeight: Dp = 480.dp,          // tune on-device
) {
    val scale = previewHeight / ShareCardHeight   // ≈ 0.283

    Layout(
        modifier = modifier.clip(CardShape),
        content = {
            ShareCard(
                sign = sign,
                date = date,
                quoteText = quoteText,
            )
        },
    ) { measurables, _ ->
        // Measure the card at its own fixed 675x1200 size, ignoring the sheet's tiny constraints.
        val placeable = measurables.first().measure(Constraints())
        val scaledW = (placeable.width * scale).roundToInt()
        val scaledH = (placeable.height * scale).roundToInt()

        // Report the SCALED size so the sheet lays out / centers a real 191x340 element.
        layout(scaledW, scaledH) {
            placeable.placeWithLayer(0, 0) {
                scaleX = scale
                scaleY = scale
                transformOrigin = TransformOrigin(0f, 0f)   // scale from top-left into reported bounds
            }
        }
    }
}
@Composable
private fun ShareCardQuoteBlock(
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
