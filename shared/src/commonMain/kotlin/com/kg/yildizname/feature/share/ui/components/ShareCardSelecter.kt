package com.kg.yildizname.feature.share.ui.components

import androidx.compose.runtime.Composable
import com.kg.yildizname.core.domain.model.CompatibilityBand
import com.kg.yildizname.core.domain.model.localizedDesc
import com.kg.yildizname.feature.share.ui.domain.ShareRequest
import horoscope.shared.generated.resources.Res
import horoscope.shared.generated.resources.compat_score_communication
import horoscope.shared.generated.resources.compat_score_friendship
import horoscope.shared.generated.resources.compat_score_long_term
import horoscope.shared.generated.resources.compat_score_love
import org.jetbrains.compose.resources.stringResource

/** The card for [request], at full export size. Shared by the preview and the capture path. */
@Composable
internal fun ShareRequestCard(request: ShareRequest) {
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
                ShareScore(
                    stringResource(Res.string.compat_score_communication),
                    request.request.scores.communication
                ),
                ShareScore(
                    stringResource(Res.string.compat_score_friendship),
                    request.request.scores.friendship
                ),
                ShareScore(
                    stringResource(Res.string.compat_score_love),
                    request.request.scores.love
                ),
                ShareScore(
                    stringResource(Res.string.compat_score_long_term),
                    request.request.scores.longTerm
                ),
            ),
            verdictText = request.request.verdictText,
        )
        is ShareRequest.CompatibilityDetailed -> CompatibilityDetailedShareCard(
            signA = request.request.signA,
            signB = request.request.signB,
            matchPercent = request.request.matchPercent,
            bandLabel = CompatibilityBand.fromScore(request.request.matchPercent).localizedDesc(),
            verdictText = request.request.verdictText,
            summary = request.request.summary,
            strengths = request.request.strengths,
            challenges = request.request.challenges,
            pros = request.request.pros,
            cons = request.request.cons,
        )
    }
}
