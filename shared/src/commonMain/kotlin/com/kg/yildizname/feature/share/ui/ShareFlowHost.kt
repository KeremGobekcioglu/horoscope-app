package com.kg.yildizname.feature.share.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.kg.yildizname.core.data.model.CompatibilityScores
import com.kg.yildizname.core.data.model.ZodiacSign
import com.kg.yildizname.core.data.model.localizedName
import com.kg.yildizname.core.domain.model.CompatibilityBand
import com.kg.yildizname.core.domain.model.localizedDesc
import com.kg.yildizname.core.ui.utils.DateFormatter
import com.kg.yildizname.core.util.AppLinks
import com.kg.yildizname.core.util.yzUppercase
import com.kg.yildizname.platform.ShareManager
import com.kg.yildizname.platform.ShareResult
import com.kg.yildizname.platform.ShareTarget
import com.kg.yildizname.platform.toPngBytes
import horoscope.shared.generated.resources.Res
import horoscope.shared.generated.resources.compat_score_communication
import horoscope.shared.generated.resources.compat_score_friendship
import horoscope.shared.generated.resources.compat_score_long_term
import horoscope.shared.generated.resources.compat_score_love
import horoscope.shared.generated.resources.share_card_app_name
import horoscope.shared.generated.resources.share_error_generic
import horoscope.shared.generated.resources.share_error_permission_denied
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

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

/** The card for [request], at full export size. Shared by the preview and the capture path. */
@Composable
private fun ShareRequestCard(request: ShareRequest) {
    when (request) {
        is ShareRequest.Horoscope -> ShareCard(
            sign = request.request.sign,
            date = request.request.date,
            quoteText = request.request.quoteText,
        )
        is ShareRequest.Compatibility -> CompatibilityShareCard(
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
        )
    }
}

/**
 * Renders the ShareBottomSheet (with the ShareCard/CompatibilityShareCard preview embedded
 * inside it) for [state]'s current request, if any. Any screen that wants to open the share
 * flow just calls `state.open(ShareCardRequest(...))` or `state.open(CompatibilityShareCardRequest(...))`
 * — this host owns the card/sheet composition and dismiss wiring so screens/nav graph don't have to.
 */
@Composable
fun ShareFlowHost(state: ShareFlowState) {
    val request = state.request ?: return
    val shareText = shareTextFor(request)
    // 1- dependencies
    // 1. dependencies
    val layer = rememberGraphicsLayer()
    val shareManager = koinInject<ShareManager>()
    val scope = rememberCoroutineScope()

    // 2. state — just two vars, no sealed class
    var isWorking by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    // 3. strings (stringResource must be called in composable scope, not inside perform)
    val genericError = stringResource(Res.string.share_error_generic)
    val permissionDenied = stringResource(Res.string.share_error_permission_denied)

    // Offscreen export copy. Reports its real size — a zero-sized node is skipped during the
    // draw phase, so nothing would ever be recorded into the layer — and is wrapped in a
    // 0dp Box with unbounded wrapping so the parent still reserves no space for it.
    Box(modifier = Modifier.size(0.dp).wrapContentSize(unbounded = true))
    {
        CompositionLocalProvider(LocalDensity provides ShareCardExportDensity)
        {
            CaptureHost(layer = layer) {
                ShareRequestCard(request)
            }
        }
    }
    /**/
    fun perform(action: suspend (ByteArray) -> ShareResult)
    {
        if(isWorking) return // guards the double-tap → double-encode race
        scope.launch {
            isWorking = true
            errorMessage = null
            try {
                if(layer.size.width == 0 || layer.size.height == 0)
                {
                    errorMessage = genericError
                    return@launch
                }
                val png = layer.toImageBitmap().toPngBytes()
                when (val result = action(png)) {
                    ShareResult.Success -> state.dismiss()
                    // App isn't installed — the user asked to share, so give them a way to
                    // rather than an error. Reuses the same bytes, no re-encode.
                    ShareResult.TargetUnavailable ->
                        if (shareManager.share(png, ShareTarget.SystemSheet) is ShareResult.Success) {
                            state.dismiss()
                        } else {
                            errorMessage = genericError
                        }
                    is ShareResult.Failed -> errorMessage = result.cause?.message ?: genericError
                }
            } finally {
                isWorking = false
            }
        }
    }

    val onSave = rememberGallerySaveGate(
        onGranted = { perform { shareManager.saveToGallery(it) }},
        onDenied = {errorMessage = permissionDenied}
    )
    ShareBottomSheet(
        preview = {
            ScaledShareCard(modifier = Modifier.fillMaxWidth()) { ShareRequestCard(request) }
        },
        isWorking = isWorking,
        errorMessage = errorMessage,
        onInstagramStoriesClick = { perform { shareManager.share(it, ShareTarget.InstagramStories) } },
        onWhatsAppClick = { perform { shareManager.share(it, ShareTarget.WhatsApp) } },
        onFacebookClick = { perform { shareManager.share(it, ShareTarget.Facebook) } },
        onGeneralShareClick = { perform { shareManager.share(it, ShareTarget.SystemSheet) } },
        onSaveImageClick = onSave,
        onShareTextClick = {
            if (!isWorking) scope.launch { shareManager.shareText(shareText, ShareTarget.SystemSheet) }
        },
        onDismiss = state::dismiss,
    )
}

@Composable
private fun shareTextFor(request: ShareRequest): String = when (request) {
    is ShareRequest.Horoscope -> buildString {
        appendLine(request.request.sign.localizedName().yzUppercase())
        appendLine(DateFormatter.formatDate(request.request.date.toString()))
        appendLine()
        appendLine(request.request.quoteText)
        appendLine()
        appendLine(stringResource(Res.string.share_card_app_name))
        append(AppLinks.LANDING_PAGE)
    }
    is ShareRequest.Compatibility -> buildString {
        val r = request.request
        appendLine("${r.signA.localizedName()} ♥ ${r.signB.localizedName()} — %${r.matchPercent}")
        appendLine(CompatibilityBand.fromScore(r.matchPercent).localizedDesc())
        appendLine()
        appendLine(r.verdictText)
        appendLine()
        appendLine(stringResource(Res.string.share_card_app_name))
        append(AppLinks.LANDING_PAGE)
    }
}