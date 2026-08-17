package com.kg.yildizname.feature.share.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.kg.yildizname.core.data.model.localizedName
import com.kg.yildizname.core.domain.model.CompatibilityBand
import com.kg.yildizname.core.domain.model.localizedDesc
import com.kg.yildizname.core.ui.utils.DateFormatter
import com.kg.yildizname.core.ui.utils.yzNavigationBarsPadding
import com.kg.yildizname.core.util.AppLinks
import com.kg.yildizname.core.util.yzUppercase
import com.kg.yildizname.feature.share.ui.components.CompatibilityDetailedShareCard
import com.kg.yildizname.feature.share.ui.components.CompatibilityShareCard
import com.kg.yildizname.feature.share.ui.components.ScaledShareCard
import com.kg.yildizname.feature.share.ui.components.ShareCard
import com.kg.yildizname.feature.share.ui.components.ShareRequestCard
import com.kg.yildizname.feature.share.ui.components.ShareScore
import com.kg.yildizname.feature.share.ui.domain.ShareRequest
import com.kg.yildizname.feature.share.ui.platform.rememberGallerySaveGate
import com.kg.yildizname.feature.share.ui.platform.rememberShareOptions
import com.kg.yildizname.feature.share.ui.util.ShareCardExportDensity
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
import horoscope.shared.generated.resources.share_error_permission_denied_action
import horoscope.shared.generated.resources.share_save_success
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

/**
 * Renders the ShareBottomSheet (with the ShareCard/CompatibilityShareCard/
 * CompatibilityDetailedShareCard preview embedded inside it) for [state]'s current request, if
 * any. Any screen that wants to open the share flow just calls `state.open(ShareCardRequest(...))`,
 * `state.open(CompatibilityShareCardRequest(...))`, or `state.open(CompatibilityDetailedShareCardRequest(...))`
 * — this host owns the card/sheet composition and dismiss wiring so screens/nav graph don't have to.
 */
@Composable
fun ShareFlowHost(state: ShareFlowState) {
    // Declared here, outside the `request != null` block below, so the coroutine that shows
    // the snackbar survives state.dismiss() — that call unmounts the block below on the next
    // recomposition, which would otherwise cancel a scope remembered inside it mid-animation.
    val snackbarScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val saveSuccessMessage = stringResource(Res.string.share_save_success)
    var errorAction by remember { mutableStateOf<(() -> Unit)?>(null) }
    val request = state.request
    if (request != null) {
        val shareText = shareTextFor(request)
        // 1- dependencies
        // 1. dependencies
        val layer = rememberGraphicsLayer()
        val shareManager = koinInject<ShareManager>()
        // Unlike snackbarScope above, this one is scoped to the request block and dies with
        // it on dismiss — fine for the in-flight encode/share work, but never launch the
        // snackbar on this one, or it'll get cancelled mid-animation by the same dismiss() call.
        val shareScope = rememberCoroutineScope()

        // 2. state — just two vars, no sealed class
        var isWorking by remember { mutableStateOf(false) }
        var errorMessage by remember { mutableStateOf<String?>(null) }
        // 3. strings (stringResource must be called in composable scope, not inside perform)
        val genericError = stringResource(Res.string.share_error_generic)
        val permissionDenied = stringResource(Res.string.share_error_permission_denied)
        val openSettingsLabel = stringResource(Res.string.share_error_permission_denied_action)
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
        fun perform(successMessage: String? = null, action: suspend (ByteArray) -> ShareResult)
        {
            if(isWorking) return // guards the double-tap → double-encode race
            shareScope.launch {
                isWorking = true
                errorMessage = null
                errorAction = null
                try {
                    if(layer.size.width == 0 || layer.size.height == 0)
                    {
                        errorMessage = genericError
                        return@launch
                    }
                    val png = layer.toImageBitmap().toPngBytes()
                    when (val result = action(png)) {
                        ShareResult.Success -> {
                            state.dismiss()
                            if (successMessage != null) {
                                snackbarScope.launch { snackbarHostState.showSnackbar(successMessage) }
                            }
                        }
                        // App isn't installed — the user asked to share, so give them a way to
                        // rather than an error. Reuses the same bytes, no re-encode.
                        ShareResult.TargetUnavailable -> {
                            // Fall through to the system sheet. On iOS this is also the recovery path for a denied
                            // Photos permission — the sheet's built-in Save Image works without our grant.
                            when (shareManager.share(png, ShareTarget.SystemSheet)) {
                                ShareResult.Success -> state.dismiss()
                                ShareResult.Cancelled -> Unit          // backed out on purpose; sheet stays open
                                else -> errorMessage = genericError
                            }
                        }
                        is ShareResult.Failed -> errorMessage = result.cause?.message ?: genericError
                        // this dont close the sheet. user can choose another option too.
                        is ShareResult.Cancelled -> Unit
                    }
                } finally {
                    isWorking = false
                }
            }
        }
        val options = rememberShareOptions()
        val onSave = rememberGallerySaveGate(
            onGranted = {
                perform(successMessage = saveSuccessMessage) {
                    shareManager.saveToGallery(
                        it
                    )
                }
            },
            onDenied = {
                errorMessage = permissionDenied
                errorAction = { shareManager.openAppSettings() }
            }
        )
        ShareBottomSheet(
            preview = {
                ScaledShareCard(modifier = Modifier.fillMaxWidth()) { ShareRequestCard(request) }
            },
            isWorking = isWorking,
            errorMessage = errorMessage,
            options = options,
            errorActionLabel = if (errorAction != null) openSettingsLabel else null,
            errorAction = errorAction,
            onOptionClick = { target -> perform { shareManager.share(it, target) }},
            onSaveImageClick = onSave,
            onShareTextClick = {
                if (!isWorking) shareScope.launch { shareManager.shareText(shareText, ShareTarget.SystemSheet) }
            },
            onDismiss = state::dismiss,
        )
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        SnackbarHost(hostState = snackbarHostState, modifier = Modifier.yzNavigationBarsPadding())
    }
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
    is ShareRequest.CompatibilityDetailed -> buildString {
        val r = request.request
        appendLine("${r.signA.localizedName()} ♥ ${r.signB.localizedName()} — %${r.matchPercent}")
        appendLine(CompatibilityBand.fromScore(r.matchPercent).localizedDesc())
        if (r.summary.isNotBlank()) {
            appendLine()
            appendLine(r.summary)
        }
        if (r.strengths.isNotBlank()) {
            appendLine()
            appendLine(r.strengths)
        }
        if (r.challenges.isNotBlank()) {
            appendLine()
            appendLine(r.challenges)
        }
        if (r.pros.isNotEmpty()) {
            appendLine()
            r.pros.forEach { appendLine("+ $it") }
        }
        if (r.cons.isNotEmpty()) {
            appendLine()
            r.cons.forEach { appendLine("- $it") }
        }
        appendLine()
        appendLine(r.verdictText)
        appendLine()
        appendLine(stringResource(Res.string.share_card_app_name))
        append(AppLinks.LANDING_PAGE)
    }
}