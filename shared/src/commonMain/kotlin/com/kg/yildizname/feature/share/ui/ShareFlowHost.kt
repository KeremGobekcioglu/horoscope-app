package com.kg.yildizname.feature.share.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.kg.yildizname.core.data.model.ZodiacSign
import com.kg.yildizname.core.data.model.localizedName
import kotlinx.datetime.LocalDate

/** Localized, all-caps sign name — matches the format ShareCard expects. */
@Composable
fun shareCardSignName(sign: ZodiacSign): String =
    sign.localizedName().uppercase()

/** Everything ShareCard/ShareBottomSheet need to render for one share request. */
data class ShareCardRequest(
    val signDisplayName: String,
    val sign: ZodiacSign,
    val quoteText: String,
    val date: LocalDate,
)

/** First sentence or two of a reading's body text, used as the share card's pull-quote. */
fun shareQuoteFrom(text: String, sentenceCount: Int = 2): String =
    text.split(". ", ".\n").take(sentenceCount).joinToString(". ").trimEnd('.', ' ') + "."

@Stable
class ShareFlowState internal constructor() {
    var request by mutableStateOf<ShareCardRequest?>(null)
        private set

    fun open(request: ShareCardRequest) {
        this.request = request
    }

    fun dismiss() {
        request = null
    }
}

@Composable
fun rememberShareFlowState(): ShareFlowState = remember { ShareFlowState() }

/**
 * Renders the ShareBottomSheet (with the ShareCard preview embedded inside it) for [state]'s
 * current request, if any. Any screen that wants to open the share flow just calls
 * `state.open(ShareCardRequest(...))` — this host owns the card/sheet composition and dismiss
 * wiring so screens/nav graph don't have to.
 */
@Composable
fun ShareFlowHost(state: ShareFlowState) {
    val request = state.request ?: return

    ShareBottomSheet(
        preview = {
            ShareCardPreview(
                signDisplayName = request.signDisplayName,
                sign = request.sign,
                date = request.date,
                quoteText = request.quoteText,
                modifier = Modifier.fillMaxWidth(),
            )
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
