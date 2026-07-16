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

private const val previewCompatibilityQuote =
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
            quoteText = previewCompatibilityQuote,
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
            quoteText = previewCompatibilityQuote,
            modifier = Modifier.fillMaxWidth().padding(24.dp),
        )
    }
}

/** Long quote text, to check the quote block and card layout don't overflow/clip. */
@Preview(name = "CompatibilityShareCardPreview — Long quote", showBackground = true, widthDp = 360, heightDp = 560)
@Composable
private fun CompatibilityShareCardScaledLongQuotePreview() {
    YzTheme {
        CompatibilityShareCardPreview(
            signA = ZodiacSign.CANCER,
            signB = ZodiacSign.SCORPIO,
            matchPercent = 62,
            quoteText = "İki yıldız birbirine değdiğinde, gökyüzü bile nefesini tutar. Bu bağ, sabır ve " +
                "anlayışla beslendikçe daha da güçlenecek. Zaman zaman anlaşmazlıklar yaşansa da, " +
                "temeldeki sevgi ve saygı her şeyi aşacak güçtedir.",
            modifier = Modifier.fillMaxWidth().padding(24.dp),
        )
    }
}