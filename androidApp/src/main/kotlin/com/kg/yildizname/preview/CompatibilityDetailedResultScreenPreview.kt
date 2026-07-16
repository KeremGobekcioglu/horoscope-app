package com.kg.yildizname.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kg.yildizname.core.data.model.CompatibilityContent
import com.kg.yildizname.core.data.model.CompatibilityResult
import com.kg.yildizname.core.data.model.CompatibilityScores
import com.kg.yildizname.core.data.model.ZodiacSign
import com.kg.yildizname.core.ui.theme.AppIcons
import com.kg.yildizname.core.ui.theme.YzBg
import com.kg.yildizname.core.ui.theme.YzBgLight
import com.kg.yildizname.core.ui.theme.YzGold
import com.kg.yildizname.core.ui.theme.YzTheme
import com.kg.yildizname.feature.compatability.ui.CompatibilityDetailedResult.CompatibilityDetailedResultScreen
import com.kg.yildizname.feature.compatability.ui.CompatibilityDetailedResult.FinalVerdictCard
import com.kg.yildizname.feature.compatability.ui.CompatibilityDetailedResult.InfoCards
import com.kg.yildizname.feature.compatability.ui.CompatibilityDetailedResult.ProsConsCard
import com.kg.yildizname.feature.compatability.ui.CompatibilityResult.CompatibilityResultUIState
import horoscope.shared.generated.resources.Res
import horoscope.shared.generated.resources.advices
import horoscope.shared.generated.resources.challenges
import horoscope.shared.generated.resources.compat_score_communication
import horoscope.shared.generated.resources.loveandintimacy
import horoscope.shared.generated.resources.strengths
import org.jetbrains.compose.resources.stringResource

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
        summary = "Aries and Leo share a fiery, passionate bond built on mutual admiration and shared energy. Both are ruled by a need to be seen and respected, which creates an electric dynamic from the very first spark. When they align, they push each other toward bigger goals and bolder adventures. This is a pairing that rarely feels dull, even in its quieter moments.",

        strengths = "Both signs bring confidence and enthusiasm to the relationship, making even ordinary days feel like an adventure. They admire each other's courage and are rarely threatened by the other's ambition. Aries brings spontaneity while Leo brings warmth and generosity, balancing the partnership nicely. Together they create a home base that feels exciting rather than routine.",

        challenges = "Two strong personalities can clash over who leads, especially when both want the final say in decisions. Aries can move too fast for Leo's need for appreciation, leaving Leo feeling overlooked. Leo's pride can come across as stubbornness to Aries, who values quick resolutions over long discussions. Without conscious effort, small disagreements can escalate into full-blown power struggles.",

        communication = "Direct and honest, though sometimes too blunt, this pairing rarely leaves things unsaid. Both prefer confrontation over passive-aggressive silence, which can be refreshing but also exhausting. Leo needs reassurance in how things are said, while Aries focuses more on getting the point across quickly. Learning to soften delivery without losing honesty is the key growth area here.",

        loveAndIntimacy = "Intense chemistry with a strong physical connection keeps this relationship charged from the start. Both signs are passionate and affectionate, often expressing love through grand gestures and playful teasing. Their romance thrives on excitement, spontaneity, and a bit of healthy competition. Keeping that initial spark alive requires continued effort as the relationship matures.",

        advice = "Take turns leading and celebrate each other's wins instead of quietly keeping score. Make space for Leo's need for recognition and Aries' need for independence without feeling threatened by either. Regular check-ins can help resolve ego clashes before they build into resentment. Most importantly, remember that this pairing thrives when both partners feel like teammates, not rivals.",

        pros = listOf(
            "Shared enthusiasm",
            "Strong loyalty",
            "Exciting, adventurous energy",
            "Mutual respect for ambition"
        ),
        cons = listOf(
            "Competing egos",
            "Impatience with each other",
            "Power struggles over control",
            "Difficulty compromising"
        ),
        friendship = "naber",
        longTerm = "naber",
        finalVerdict = "naber"
    )
)

@Preview
@Composable
fun ProConCards() {
    YzTheme {
        Box(modifier = Modifier.fillMaxSize().background(YzBg)) {
            ProsConsCard(
                pros = listOf(
                    "Shared enthusiasm",
                    "Strong loyalty",
                    "Exciting, adventurous energy",
                    "Mutual respect for ambition"
                ),
                cons = listOf(
                    "Competing egos",
                    "Impatience with each other",
                    "Power struggles over control",
                    "Difficulty compromising"
                )
            )
        }
    }
}
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

//@Preview
//@Composable
//fun CompatibilityDetailedResultScreenLoadingPreview() {
//    YzTheme {
//        Box(modifier = Modifier.fillMaxSize().background(YzBg)) {
//            CompatibilityDetailedResultScreen(
//                uiState = CompatibilityResultUIState.Loading,
//                onBack = {}
//            )
//        }
//    }
//}
//
//@Preview
//@Composable
//fun CompatibilityDetailedResultScreenErrorPreview() {
//    YzTheme {
//        Box(modifier = Modifier.fillMaxSize().background(YzBg)) {
//            CompatibilityDetailedResultScreen(
//                uiState = CompatibilityResultUIState.Error("No compatibility data is found"),
//                onBack = {}
//            )
//        }
//    }
//}

@Preview
@Composable
fun AdviceInfoCardPrew() {
    val advice = "Take turns leading and celebrate each other's wins instead of quietly keeping score. " +
            "Make space for Leo's need for recognition and Aries' need for independence " +
            "without feeling threatened by either. Regular check-ins can help resolve ego " +
            "clashes before they build into resentment. Most importantly, remember that this" +
            " pairing thrives when both partners feel like teammates, not rivals."
    val list = advice.split(".").map { it.trim() }.filter { it.isNotBlank() }
    val bgCard = YzBgLight.copy(0.65f)
    YzTheme {
        Box(modifier = Modifier.fillMaxSize().background(YzBg)) {
            InfoCards(
                headlineIcon = AppIcons.Recommendations,
                contentLineIcon = AppIcons.Bullet,
                contentLineIconTint = YzGold.copy(0.75f),
                textList = list,
                backgroundColor = bgCard,
                headlineText = stringResource(Res.string.advices),
                headLineIconTint = YzGold,
                iconOffset = (7).dp
            )
        }
    }
}

@Preview
@Composable
fun StrengthsInfoCardPrew() {
    val advice = "Both signs bring confidence and enthusiasm to the relationship, making even ordinary days feel like an adventure. They admire each other's courage and are rarely threatened by the other's ambition. Aries brings spontaneity while Leo brings warmth and generosity, balancing the partnership nicely. Together they create a home base that feels exciting rather than routine."

    val list = advice.split(".").map { it.trim() }.filter { it.isNotBlank() }
    val bgCard = YzBgLight.copy(0.65f)
    YzTheme {
        Box(modifier = Modifier.fillMaxSize().background(YzBg)) {
            InfoCards(
                contentLineIcon = AppIcons.Compatible,
                contentLineIconTint = YzGold,
                textList = list,
                backgroundColor = bgCard,
                headlineText = stringResource(Res.string.strengths),
                iconOffset = 7.dp,
                iconSize = 12.dp
            )
        }
    }
}

@Preview
@Composable
fun ChallengesInfoCardPrew() {
    val advice = "Two strong personalities can clash over who leads, especially when both want the final say in decisions. Aries can move too fast for Leo's need for appreciation, leaving Leo feeling overlooked. Leo's pride can come across as stubbornness to Aries, who values quick resolutions over long discussions. Without conscious effort, small disagreements can escalate into full-blown power struggles."

    val list = advice.split(".").map { it.trim() }.filter { it.isNotBlank() }
    val bgCard = YzBgLight.copy(0.65f)
    YzTheme {
        Box(modifier = Modifier.fillMaxSize().background(YzBg)) {
            InfoCards(
                headlineIcon = null,
                headLineIconTint = null,
                contentLineIcon = AppIcons.Incompatible,
                contentLineIconTint = Color.Red,
                textList = list,
                backgroundColor = bgCard,
                headlineText = stringResource(Res.string.challenges),
                iconOffset = 7.dp,
                iconSize = 12.dp
            )
        }
    }
}

@Preview
@Composable
fun LovePreview() {
    val advice = "Intense chemistry with a strong physical connection keeps this relationship charged from the start. Both signs are passionate and affectionate, often expressing love through grand gestures and playful teasing. Their romance thrives on excitement, spontaneity, and a bit of healthy competition. Keeping that initial spark alive requires continued effort as the relationship matures."

    val list = advice.split(".").map { it.trim() }.filter { it.isNotBlank() }
    val bgCard = YzBgLight.copy(0.65f)
    YzTheme {
        Box(modifier = Modifier.fillMaxSize().background(YzBg)) {
            InfoCards(
                headlineIcon = AppIcons.Love,
                headLineIconTint = YzGold,
                contentLineIcon = null,
                contentLineIconTint = null,
                textList = list,
                backgroundColor = bgCard,
                headlineText = stringResource(Res.string.loveandintimacy),
                iconOffset = null
            )
        }
    }
}

@Preview
@Composable
fun FinalVerdictCardEnabledPreview() {
    YzTheme {
        Box(modifier = Modifier.fillMaxSize().background(YzBg).padding(16.dp)) {
            FinalVerdictCard(
                headLine = "Güçlü Bir Uyum",
                text = "Aries ve Leo, karşılıklı hayranlık ve paylaşılan enerji üzerine kurulu, ateşli ve tutkulu bir bağ paylaşır.",
                enabled = true,
                onClick = {}
            )
        }
    }
}

@Preview
@Composable
fun FinalVerdictCardDisabledPreview() {
    YzTheme {
        Box(modifier = Modifier.fillMaxSize().background(YzBg).padding(16.dp)) {
            FinalVerdictCard(
                headLine = "Güçlü Bir Uyum",
                text = "Aries ve Leo, karşılıklı hayranlık ve paylaşılan enerji üzerine kurulu, ateşli ve tutkulu bir bağ paylaşır.",
                enabled = false,
                onClick = {}
            )
        }
    }
}

@Preview
@Composable
fun CommunicationPreview() {
    val advice = "Direct and honest, though sometimes too blunt, this pairing rarely leaves things unsaid. Both prefer confrontation over passive-aggressive silence, which can be refreshing but also exhausting. Leo needs reassurance in how things are said, while Aries focuses more on getting the point across quickly. Learning to soften delivery without losing honesty is the key growth area here."

    val list = advice.split(".").map { it.trim() }.filter { it.isNotBlank() }
    val bgCard = YzBgLight.copy(0.65f)
    YzTheme {
        Box(modifier = Modifier.fillMaxSize().background(YzBg)) {
            InfoCards(
                headlineIcon = AppIcons.Communication,
                headLineIconTint = Color.Blue,
                contentLineIcon = null,
                contentLineIconTint = null,
                textList = list,
                backgroundColor = bgCard,
                headlineText = stringResource(Res.string.compat_score_communication),
                iconOffset = null
            )
        }
    }
}