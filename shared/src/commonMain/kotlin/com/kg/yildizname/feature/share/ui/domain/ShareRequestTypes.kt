package com.kg.yildizname.feature.share.ui.domain

import com.kg.yildizname.core.data.model.CompatibilityScores
import com.kg.yildizname.core.data.model.ZodiacSign
import kotlinx.datetime.LocalDate

/** Localized, all-caps sign name — matches the format ShareCard expects. */
//@Composable
//fun shareCardSignName(sign: ZodiacSign): String =
//    sign.localizedName().uppercase()

/** Everything ShareCard/ShareBottomSheet need to render for one share request. */
data class ShareCardRequest(
    val signDisplayName: String,
    val sign: ZodiacSign,
    val quoteText: String,
    val date: LocalDate,
)

/** Everything [com.kg.yildizname.feature.share.ui.components.CompatibilityShareCard]/ShareBottomSheet need to render for one share request —
 * used from the quick-result screen. */
data class CompatibilityShareCardRequest(
    val signA: ZodiacSign,
    val signB: ZodiacSign,
    val matchPercent: Int,
    val scores: CompatibilityScores,
    val verdictText: String,
)

/** Everything [com.kg.yildizname.feature.share.ui.components.CompatibilityDetailedShareCard]/ShareBottomSheet need to render for one share
 * request — used from the detailed-result screen. No [CompatibilityScores]: the detailed card
 * never renders the score grid, so there's nothing here for it to feed. */
data class CompatibilityDetailedShareCardRequest(
    val signA: ZodiacSign,
    val signB: ZodiacSign,
    val matchPercent: Int,
    val verdictText: String,
    val summary: String,
    val strengths: String,
    val challenges: String,
    val pros: List<String>,
    val cons: List<String>,
)

/** The card [com.kg.yildizname.feature.share.ui.ShareFlowHost] is currently asked to preview/share — a reading, a quick-result
 * compatibility card, or a detailed-result compatibility card. */
sealed interface ShareRequest {
    data class Horoscope(val request: ShareCardRequest) : ShareRequest
    data class Compatibility(val request: CompatibilityShareCardRequest) : ShareRequest
    data class CompatibilityDetailed(val request: CompatibilityDetailedShareCardRequest) : ShareRequest
}

/** First sentence or two of a reading's body text, used as the share card's pull-quote. */
fun shareQuoteFrom(text: String, sentenceCount: Int = 2): String =
    text.split(". ", ".\n")/*.take(sentenceCount)*/.joinToString(". ").trimEnd('.', ' ') + "."