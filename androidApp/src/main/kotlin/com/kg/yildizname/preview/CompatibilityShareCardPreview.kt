package com.kg.yildizname.preview

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kg.yildizname.core.data.model.ZodiacSign
import com.kg.yildizname.core.ui.theme.YzTheme
import com.kg.yildizname.feature.share.ui.CompatibilityShareCard
import com.kg.yildizname.feature.share.ui.CompatibilityShareCardPreview
import com.kg.yildizname.feature.share.ui.ShareScore

private val previewCompatibilityScores = listOf(
    ShareScore("İletişim", 82),
    ShareScore("Arkadaşlık", 74),
    ShareScore("Aşk", 91),
    ShareScore("Uzun Vadeli", 68),
)

private const val previewVerdictText =
    "İki yıldız birbirine değdiğinde, gökyüzü bile nefesini tutar. Bu bağ, sabır ve " +
        "anlayışla beslendikçe daha da güçlenecek."

/**
 * Full-size 675x1200dp export asset, unscaled. Lets you eyeball the literal bitmap that gets
 * rendered offscreen for sharing — never composed at this size in real UI.
 */
@Preview(name = "CompatibilityShareCard — Full export size", showBackground = true, heightDp = 1200, widthDp = 675)
@Composable
private fun CompatibilityShareCardExportPreview() {
    YzTheme {
        CompatibilityShareCard(
            signA = ZodiacSign.ARIES,
            signB = ZodiacSign.LEO,
            matchPercent = 87,
            bandLabel = "Güçlü Uyum",
            scores = previewCompatibilityScores,
            verdictText = previewVerdictText,
        )
    }
}

/** Scaled-down on-screen version, as embedded in a share bottom sheet. */
@Preview(name = "CompatibilityShareCardPreview — Scaled", showBackground = true, widthDp = 360, heightDp = 520)
@Composable
private fun CompatibilityShareCardScaledPreview() {
    YzTheme {
        CompatibilityShareCardPreview(
            signA = ZodiacSign.ARIES,
            signB = ZodiacSign.LEO,
            matchPercent = 87,
            bandLabel = "Güçlü Uyum",
            scores = previewCompatibilityScores,
            verdictText = previewVerdictText,
            modifier = Modifier.fillMaxWidth().padding(24.dp),
        )
    }
}

/** Low scores, to check the score panel and bars render sensibly at the low end. */
@Preview(name = "CompatibilityShareCardPreview — Low scores", showBackground = true, widthDp = 360, heightDp = 560)
@Composable
private fun CompatibilityShareCardScaledLowScoresPreview() {
    YzTheme {
        CompatibilityShareCardPreview(
            signA = ZodiacSign.CANCER,
            signB = ZodiacSign.SCORPIO,
            matchPercent = 62,
            bandLabel = "Değişken Uyum",
            scores = listOf(
                ShareScore("İletişim", 48),
                ShareScore("Arkadaşlık", 55),
                ShareScore("Aşk", 62),
                ShareScore("Uzun Vadeli", 40),
            ),
            verdictText = "Zaman zaman anlaşmazlıklar yaşansa da, temeldeki sevgi ve saygı her şeyi aşacak güçtedir.",
            modifier = Modifier.fillMaxWidth().padding(24.dp),
        )
    }
}
