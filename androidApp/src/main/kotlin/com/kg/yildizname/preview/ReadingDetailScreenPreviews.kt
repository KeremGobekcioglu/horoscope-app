package com.kg.yildizname.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.kg.yildizname.core.data.model.ZodiacSign
import com.kg.yildizname.core.ui.theme.YzTheme
import com.kg.yildizname.feature.readingDetail.ui.ReadingDetailScreen
import com.kg.yildizname.feature.readingDetail.ui.ReadingDetailUiState

private val previewReadingDetail = ReadingDetailUiState(
    sign = ZodiacSign.SCORPIO,
    signDisplayName = "AKREP",
    periodLabel = "GÜNLÜK YORUM",
    luckyNumber = 7,
    luckyColorName = "Kırmızı",
    generalText = "Bugün gezegenler senin lehine hizalanıyor. Sezgilerine güven, " +
        "özellikle ilişkilerinde sabırlı olman gereken bir konu gündeme gelebilir.",
    loveText = "Duygusal bağların güçleneceği bir gün. Karşındaki kişiyle açık " +
        "iletişim kurman beklenmedik bir yakınlaşma sağlayabilir.",
    careerText = "İş hayatında attığın adımlar karşılığını bulmaya başlıyor. " +
        "Yeni bir teklif ya da işbirliği fırsatı gündeme gelebilir.",
    healthText = "Enerjin yüksek ama dinlenmeyi ihmal etme. Kısa bir yürüyüş " +
        "zihnini açacak.",
    luckText = "Şans bugün senden yana. Cesur adımlar atmak için iyi bir zaman.",
    isLoading = false
)

@Preview(name = "ReadingDetail — Success", showBackground = true)
@Composable
private fun ReadingDetailScreenSuccessPreview() {
    YzTheme {
        ReadingDetailScreen(
            uiState = previewReadingDetail,
            onBackClick = {},
            onShareClick = {},
        )
    }
}

@Preview(name = "ReadingDetail — Loading", showBackground = true)
@Composable
private fun ReadingDetailScreenLoadingPreview() {
    YzTheme {
        ReadingDetailScreen(
            uiState = ReadingDetailUiState(),
            onBackClick = {},
            onShareClick = {},
        )
    }
}

@Preview(name = "ReadingDetail — Error", showBackground = true)
@Composable
private fun ReadingDetailScreenErrorPreview() {
    YzTheme {
        ReadingDetailScreen(
            uiState = ReadingDetailUiState(err = "Yorum yüklenemedi. Lütfen tekrar deneyin.", isLoading = false),
            onBackClick = {},
            onShareClick = {},
        )
    }
}
