package com.kg.yildizname.feature.share.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.kg.yildizname.core.data.model.CompatibilityScores
import com.kg.yildizname.core.data.model.ZodiacSign
import com.kg.yildizname.core.data.model.localizedName
import com.kg.yildizname.core.domain.model.CompatibilityBand
import com.kg.yildizname.core.domain.model.localizedDesc
import horoscope.shared.generated.resources.Res
import horoscope.shared.generated.resources.compat_score_communication
import horoscope.shared.generated.resources.compat_score_friendship
import horoscope.shared.generated.resources.compat_score_long_term
import horoscope.shared.generated.resources.compat_score_love
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.stringResource

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

/** Everything CompatibilityShareCard/ShareBottomSheet need to render for one share request. */
data class CompatibilityShareCardRequest(
    val signA: ZodiacSign,
    val signB: ZodiacSign,
    val matchPercent: Int,
    val scores: CompatibilityScores,
    val verdictText: String,
)

/** The card [ShareFlowHost] is currently asked to preview/share — either a reading or a compatibility result. */
sealed interface ShareRequest {
    data class Horoscope(val request: ShareCardRequest) : ShareRequest
    data class Compatibility(val request: CompatibilityShareCardRequest) : ShareRequest
}

/** First sentence or two of a reading's body text, used as the share card's pull-quote. */
fun shareQuoteFrom(text: String, sentenceCount: Int = 2): String =
    text.split(". ", ".\n")/*.take(sentenceCount)*/.joinToString(". ").trimEnd('.', ' ') + "."

@Stable
class ShareFlowState internal constructor() {
    var request by mutableStateOf<ShareRequest?>(null)
        private set

    fun open(request: ShareCardRequest) {
        this.request = ShareRequest.Horoscope(request)
    }

    fun open(request: CompatibilityShareCardRequest) {
        this.request = ShareRequest.Compatibility(request)
    }

    fun dismiss() {
        request = null
    }
}

@Composable
fun rememberShareFlowState(): ShareFlowState = remember { ShareFlowState() }

/**
 * Renders the ShareBottomSheet (with the ShareCard/CompatibilityShareCard preview embedded
 * inside it) for [state]'s current request, if any. Any screen that wants to open the share
 * flow just calls `state.open(ShareCardRequest(...))` or `state.open(CompatibilityShareCardRequest(...))`
 * — this host owns the card/sheet composition and dismiss wiring so screens/nav graph don't have to.
 */
@Composable
fun ShareFlowHost(state: ShareFlowState) {
    val request = state.request ?: return

    ShareBottomSheet(
        preview = {
            when (request) {
                is ShareRequest.Horoscope -> ShareCardPreview(
                    sign = request.request.sign,
                    date = request.request.date,
                    quoteText = request.request.quoteText,
                    modifier = Modifier.fillMaxWidth(),
                )
                is ShareRequest.Compatibility -> CompatibilityShareCardPreview(
                    signA = request.request.signA,
                    signB = request.request.signB,
                    matchPercent = request.request.matchPercent,
                    bandLabel = CompatibilityBand.fromScore(request.request.matchPercent).localizedDesc(),
                    scores = listOf(
                        ShareScore(stringResource(Res.string.compat_score_communication), request.request.scores.communication),
                        ShareScore(stringResource(Res.string.compat_score_friendship), request.request.scores.friendship),
                        ShareScore(stringResource(Res.string.compat_score_love), request.request.scores.love),
                        ShareScore(stringResource(Res.string.compat_score_long_term), request.request.scores.longTerm),
                    ),
                    verdictText = request.request.verdictText,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
        onInstagramStoriesClick = state::dismiss,
        onWhatsAppClick = state::dismiss,
        onFacebookClick = state::dismiss,
        onGeneralShareClick = state::dismiss,
        onCopyLinkClick = state::dismiss,
        onSaveImageClick = state::dismiss,
        onDismiss = state::dismiss,
    )
}
