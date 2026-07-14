package com.kg.yildizname.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kg.yildizname.core.data.model.CompatibilityContent
import com.kg.yildizname.core.data.model.CompatibilityResult
import com.kg.yildizname.core.data.model.CompatibilityScores
import com.kg.yildizname.core.data.model.ZodiacSign
import com.kg.yildizname.core.ui.theme.YzBg
import com.kg.yildizname.core.ui.theme.YzTheme
import com.kg.yildizname.feature.compatability.ui.CompatibilityDetailedResult.CompatibilityDetailedResultScreen
import com.kg.yildizname.feature.compatability.ui.CompatibilityResult.CompatibilityResultUIState

private val mockDetailedCompatibilityResult = CompatibilityResult(
    id = "aries_leo",
    signs = listOf(ZodiacSign.ARIES, ZodiacSign.LEO),
    matchPercent = 87,
    scores = CompatibilityScores(
        love = 90,
        communication = 70,
        friendship = 85,
        longTerm = 60
    ),
    content = CompatibilityContent(
        summary = "Aries and Leo share a fiery, passionate bond built on mutual admiration and shared energy.",
        strengths = "Both signs bring confidence and enthusiasm to the relationship.",
        challenges = "Two strong personalities can clash over who leads.",
        communication = "Direct and honest, though sometimes too blunt.",
        loveAndIntimacy = "Intense chemistry with a strong physical connection.",
        advice = "Take turns leading and celebrate each other's wins.",
        pros = listOf("Shared enthusiasm", "Strong loyalty"),
        cons = listOf("Competing egos", "Impatience with each other")
    )
)

@Preview
@Composable
fun CompatibilityDetailedResultScreenSuccessPreview() {
    YzTheme {
        Box(modifier = Modifier.fillMaxSize().background(YzBg)) {
            CompatibilityDetailedResultScreen(
                uiState = CompatibilityResultUIState.Success(mockDetailedCompatibilityResult),
                onBack = {}
            )
        }
    }
}

@Preview
@Composable
fun CompatibilityDetailedResultScreenLoadingPreview() {
    YzTheme {
        Box(modifier = Modifier.fillMaxSize().background(YzBg)) {
            CompatibilityDetailedResultScreen(
                uiState = CompatibilityResultUIState.Loading,
                onBack = {}
            )
        }
    }
}

@Preview
@Composable
fun CompatibilityDetailedResultScreenErrorPreview() {
    YzTheme {
        Box(modifier = Modifier.fillMaxSize().background(YzBg)) {
            CompatibilityDetailedResultScreen(
                uiState = CompatibilityResultUIState.Error("No compatibility data is found"),
                onBack = {}
            )
        }
    }
}