package com.kg.yildizname.preview

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kg.yildizname.core.data.model.ZodiacSign
import com.kg.yildizname.core.ui.theme.YzSurface
import com.kg.yildizname.core.ui.theme.YzTheme
import com.kg.yildizname.feature.share.ui.ShareBottomSheetContent
import com.kg.yildizname.feature.share.ui.components.ShareCard
import com.kg.yildizname.feature.share.ui.components.ShareCardPreview
import kotlinx.datetime.LocalDate

private val previewShareDate = LocalDate(2026, 7, 1)
private const val previewShareQuote =
    "Bugün gezegenler senin lehine hizalanıyor. Sezgilerine güven, özellikle ilişkilerinde " +
        "sabırlı olman gereken bir konu gündeme gelebilir."

/**
 * Full-size 675x1200dp export asset, unscaled. Lets you eyeball the literal bitmap that
 * gets rendered offscreen for sharing — never composed at this size in real UI.
 */
//@Preview(name = "ShareCard — Full export size", showBackground = true, heightDp = 1200, widthDp = 675)
//@Composable
//private fun ShareCardExportPreview() {
//    YzTheme {
//        ShareCard(
//            signDisplayName = "AKREP",
//            sign = ZodiacSign.SCORPIO,
//            date = previewShareDate,
//            quoteText = previewShareQuote,
//        )
//    }
//}
//
///** Scaled-down on-screen version, as embedded in [ShareBottomSheet]. */
//@Preview(name = "ShareCardPreview — Scaled", showBackground = true, widthDp = 360, heightDp = 520)
//@Composable
//private fun ShareCardScaledPreview() {
//    YzTheme {
//        ShareCardPreview(
//            signDisplayName = "AKREP",
//            sign = ZodiacSign.SCORPIO,
//            date = previewShareDate,
//            quoteText = previewShareQuote,
//            modifier = Modifier.fillMaxWidth().padding(24.dp),
//        )
//    }
//}
//
///** Long quote text, to check the quote block and card layout don't overflow/clip. */
//@Preview(name = "ShareCardPreview — Long quote", showBackground = true, widthDp = 360, heightDp = 560)
//@Composable
//private fun ShareCardScaledLongQuotePreview() {
//    YzTheme {
//        ShareCardPreview(
//            signDisplayName = "YENGEÇ",
//            sign = ZodiacSign.CANCER,
//            date = previewShareDate,
//            quoteText = "Bugün gezegenler senin lehine hizalanıyor. Sezgilerine güven, özellikle " +
//                "ilişkilerinde sabırlı olman gereken bir konu gündeme gelebilir. Akşam saatlerinde " +
//                "beklenmedik bir haber moralini yükseltecek ve seni gülümsetecek.",
//            modifier = Modifier.fillMaxWidth().padding(24.dp),
//        )
//    }
//}

/**
 * Full bottom sheet body: platform grid, text rows, and the embedded card preview together.
 *
 * Previews [ShareBottomSheetContent] directly (inside a plain [Surface]) instead of the real
 * [ShareBottomSheet] — `ModalBottomSheet` renders through a Popup, which Android Studio's static
 * preview surface never captures, so a preview wrapping the actual sheet always renders blank.
 */
//@Preview(name = "ShareBottomSheet", showBackground = true, heightDp = 700)
//@Composable
//private fun ShareBottomSheetPreview() {
//    YzTheme {
//        Surface(color = YzSurface) {
//            ShareBottomSheetContent(
//                preview = {
//                    ShareCardPreview(
//                        sign = ZodiacSign.SCORPIO,
//                        date = previewShareDate,
//                        quoteText = previewShareQuote,
//                        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
//                    )
//                },
//                onInstagramStoriesClick = {},
//                onWhatsAppClick = {},
//                onFacebookClick = {},
//                onGeneralShareClick = {},
//                onCopyLinkClick = {},
//                onSaveImageClick = {},
//                onDismiss = {},
//            )
//        }
//    }
//}
