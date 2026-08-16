package com.kg.yildizname.preview

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kg.yildizname.core.data.model.ZodiacSign
import com.kg.yildizname.core.ui.theme.YzTheme
import com.kg.yildizname.feature.share.ui.CompatibilityDetailedShareCard
import com.kg.yildizname.feature.share.ui.CompatibilityDetailedShareCardPreview

private val previewPros = listOf(
    "Ortak enerji ve heyecan",
    "Birbirini motive etme",
    "Güçlü fiziksel çekim",
)
private val previewCons = listOf(
    "Liderlik konusunda çekişme",
    "Sabırsızlık anları",
)
private const val previewSummary =
    "Ateşli ve tutkulu bir eşleşme — ikisi de hayattan keyif almayı bilir, ama liderlik " +
        "konusunda uzlaşmayı öğrenmeleri gerekir."
private const val previewStrengths =
    "İkisi de özgürlüğüne düşkün, birbirinin bağımsızlığına saygı duyar ve birlikte yeni " +
        "deneyimler keşfetmekten büyük keyif alır."
private const val previewChallenges =
    "İkisi de baskın karakterlere sahip olduğundan zaman zaman liderlik konusunda çekişme " +
        "yaşanabilir, sabır gerektirir."

/**
 * Full-size export asset for the detailed-result screen's share button — summary +
 * strengths/challenges + pros/cons, no score grid. The card's height is no longer a fixed
 * budget — it wraps whatever [CompatibilityShareCardFrame]'s content actually measures to, so
 * none of this text gets ellipsized to fit a guessed-at number. heightDp here is just a
 * generously oversized IDE preview canvas (real content lands well under it); it does not
 * constrain the composable or the real export.
 */
@Preview(name = "CompatibilityDetailedShareCard — Full export size", showBackground = true, heightDp = 1800, widthDp = 675)
@Composable
private fun CompatibilityDetailedShareCardExportPreview() {
    YzTheme {
        CompatibilityDetailedShareCard(
            signA = ZodiacSign.ARIES,
            signB = ZodiacSign.LEO,
            matchPercent = 87,
            bandLabel = "Güçlü Uyum",
            verdictText = previewVerdictText,
            summary = previewSummary,
            strengths = previewStrengths,
            challenges = previewChallenges,
            pros = previewPros,
            cons = previewCons,
        )
    }
}

/** Scaled-down on-screen version, as embedded in a share bottom sheet. */
@Preview(name = "CompatibilityDetailedShareCardPreview — Scaled", showBackground = true, widthDp = 360, heightDp = 750)
@Composable
private fun CompatibilityDetailedShareCardScaledPreview() {
    YzTheme {
        CompatibilityDetailedShareCardPreview(
            signA = ZodiacSign.ARIES,
            signB = ZodiacSign.LEO,
            matchPercent = 87,
            bandLabel = "Güçlü Uyum",
            verdictText = previewVerdictText,
            summary = previewSummary,
            strengths = previewStrengths,
            challenges = previewChallenges,
            pros = previewPros,
            cons = previewCons,
            modifier = Modifier.fillMaxWidth().padding(24.dp),
        )
    }
}
