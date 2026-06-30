package com.kg.yildizname.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.kg.yildizname.core.data.model.PeriodType
import com.kg.yildizname.core.data.model.Reading
import com.kg.yildizname.core.data.model.ScoreSet
import com.kg.yildizname.core.data.model.ZodiacSign
import com.kg.yildizname.core.ui.theme.YzTheme
import com.kg.yildizname.feature.home.ui.HomeScreen
import com.kg.yildizname.feature.home.ui.HomeUiState

private val previewReading = Reading(
    sign = ZodiacSign.SCORPIO,
    period = PeriodType.DAILY,
    date = "2026-07-01",
    text = "Bugün gezegenler senin lehine hizalanıyor. Sezgilerine güven, " +
        "özellikle ilişkilerinde sabırlı olman gereken bir konu gündeme gelebilir. " +
        "Akşam saatlerinde beklenmedik bir haber moralini yükseltecek.",
    scores = ScoreSet(love = 8, work = 6, health = 7, luck = 9),
)

@Preview(name = "Home — Success", showBackground = true)
@Composable
private fun HomeScreenSuccessPreview() {
    YzTheme {
        HomeScreen(
            uiState = HomeUiState.Success(
                reading = previewReading,
                todayLabel = "1 Temmuz, Çarşamba",
            ),
            onReadMoreClick = { _, _ -> },
            onShareClick = {},
            onShareCardClick = {},
            onNotificationClick = {},
            onRetryClick = {},
        )
    }
}

@Preview(name = "Home — Loading", showBackground = true)
@Composable
private fun HomeScreenLoadingPreview() {
    YzTheme {
        HomeScreen(
            uiState = HomeUiState.Loading,
            onReadMoreClick = { _, _ -> },
            onShareClick = {},
            onShareCardClick = {},
            onNotificationClick = {},
            onRetryClick = {},
        )
    }
}

@Preview(name = "Home — Error", showBackground = true)
@Composable
private fun HomeScreenErrorPreview() {
    YzTheme {
        HomeScreen(
            uiState = HomeUiState.Error(message = "Yorum yüklenemedi. Lütfen tekrar deneyin."),
            onReadMoreClick = { _, _ -> },
            onShareClick = {},
            onShareCardClick = {},
            onNotificationClick = {},
            onRetryClick = {},
        )
    }
}
