package com.kg.yildizname.feature.compatability.ui

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kg.yildizname.core.data.model.ZodiacSign
import com.kg.yildizname.core.data.repository.CompatibilityRepository
import com.kg.yildizname.core.ui.components.StarFieldBackground
import com.kg.yildizname.core.ui.theme.CosmicPurple
import com.kg.yildizname.core.ui.theme.DarkGray
import com.kg.yildizname.core.ui.theme.Gray
import com.kg.yildizname.core.ui.theme.SquareShape
import com.kg.yildizname.core.ui.theme.YzBg
import com.kg.yildizname.core.ui.theme.YzGold
import com.kg.yildizname.core.ui.theme.YzInk
import com.kg.yildizname.core.ui.theme.YzOnSurface
import com.kg.yildizname.core.ui.theme.YzSurface
import com.kg.yildizname.core.ui.utils.yzStatusBarsPadding
import com.kg.yildizname.core.util.pairId
import com.kg.yildizname.feature.compatability.ui.components.AnalyzeButton
import com.kg.yildizname.feature.compatability.ui.components.InfoCard
import com.kg.yildizname.feature.compatability.ui.components.SelectSignBottomSheet
import com.kg.yildizname.feature.compatability.ui.components.SelectSignButton
import horoscope.shared.generated.resources.Res
import horoscope.shared.generated.resources.angle_relationship
import horoscope.shared.generated.resources.compat_person_one_label
import horoscope.shared.generated.resources.compat_person_two_label
import horoscope.shared.generated.resources.compat_select_sign
import horoscope.shared.generated.resources.compat_select_two_signs_hint
import horoscope.shared.generated.resources.compat_subtitle
import horoscope.shared.generated.resources.compat_title
import horoscope.shared.generated.resources.desc_angular_relationship
import horoscope.shared.generated.resources.desc_element_balance
import horoscope.shared.generated.resources.element_icon
import horoscope.shared.generated.resources.infinity
import horoscope.shared.generated.resources.title_angular_relationship
import horoscope.shared.generated.resources.title_element_balance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import kotlin.math.sign

/**
 * TEMPORARY TEST SCREEN — not the real Compatibility screen.
 *
 * Calls CompatibilityRepository directly from the composable via koinInject(), which
 * breaks the project's "screens are stateless, no ViewModel in composables" rule on
 * purpose, just for this smoke test. Delete or replace with the real ViewModel-backed
 * screen once the repository is confirmed working against real Firestore/Room.
 *
 * Hardcoded to Aries + Leo — change the two ZodiacSign values below to test other pairs.
 */
private enum class SignSlot { ONE, TWO }

@Composable
fun CompatibilityScreen() {
    StarFieldBackground(Modifier.fillMaxSize())
    var personOneSign by remember { mutableStateOf<ZodiacSign?>(null) }
    var personTwoSign by remember { mutableStateOf<ZodiacSign?>(null) }
    var pickingSlot by remember { mutableStateOf<SignSlot?>(null) }
    Box(
        modifier = Modifier.fillMaxSize().yzStatusBarsPadding(),
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(16.dp))
            Text(
                text = stringResource(Res.string.compat_title),
                color = YzGold,
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = stringResource(Res.string.compat_subtitle),
                color = YzInk,
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(Modifier.height(64.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            )
            {
                Column(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    SelectSignButton(
                        modifier = Modifier.size(96.dp),
                        textBelow = stringResource(Res.string.compat_person_one_label),
                        canShine = true,
                        selectedSign = personOneSign,
                        selectSign = { pickingSlot = SignSlot.ONE }
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = stringResource(Res.string.compat_person_one_label),
                        color = YzInk,
                        fontSize = 12.sp
                    )
                }
                Icon(
                    painter = painterResource(Res.drawable.infinity),
                    contentDescription = null,
                    tint = YzGold,
                    modifier = Modifier
                        .size(64.dp)
                        .offset(y = (-16).dp) // move left
                )
                Column(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    SelectSignButton(
                        modifier = Modifier.size(96.dp),
                        textBelow = stringResource(Res.string.compat_person_two_label),
                        canShine = true,
                        selectedSign = personTwoSign,
                        selectSign = { pickingSlot = SignSlot.TWO }
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = stringResource(Res.string.compat_person_two_label),
                        color = YzInk,
                        fontSize = 12.sp
                    )
                }

            }
            Spacer(Modifier.height(28.dp))
            AnalyzeButton(
                isEnabled = personOneSign != null && personTwoSign != null,
                modifier = Modifier.padding(horizontal = 28.dp),
                onClick = {}
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = stringResource(Res.string.compat_select_two_signs_hint),
                color = DarkGray.copy(0.8f),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(64.dp))
            InfoCard(
                headline = Res.string.title_element_balance,
                text = Res.string.desc_element_balance,
                icon = Res.drawable.element_icon,
                headlineAndIconColor = YzGold
            )
            Spacer(Modifier.height(16.dp))
            InfoCard(
                headline = Res.string.title_angular_relationship,
                text = Res.string.desc_angular_relationship,
                icon = Res.drawable.angle_relationship,
                headlineAndIconColor = CosmicPurple
            )
        }

        pickingSlot?.let { slot ->
            SelectSignBottomSheet(
                onSignConfirmed = { sign ->
                    when (slot) {
                        SignSlot.ONE -> personOneSign = sign
                        SignSlot.TWO -> personTwoSign = sign
                    }
                    pickingSlot = null
                },
                onDismiss = { pickingSlot = null },
            )
        }
    }
}
    /*
    *   Headline - Uyum Analizi
    *   Headline two : Kozmik Rezonans Testi
    *   One backgroudn image if appliacble
    *
    *   BURÇ SEÇ  -- -- - - BURÇ SEÇ
    *
    *   ANALİZ ET BUTTON
    *   Small hint
    *
    *   Two cards: element dengesi
    *   Açısal ilişki
    * */

//    val repository = koinInject<CompatibilityRepository>()
//    val scope = rememberCoroutineScope()
//
//    var status by remember { mutableStateOf("Not fetched yet") }
//    var resultText by remember { mutableStateOf("") }
//
//    Box(
//        modifier = Modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(24.dp)
//                .verticalScroll(rememberScrollState()),
//            verticalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//            Text(text = "Compatibility — Repository Test")
//
//            Button(onClick = {
//                status = "Fetching..."
//                resultText = ""
//                scope.launch {
//                    // .first() takes just the first emission — fine for a manual test,
//                    // since we're eyeballing one result at a time, not observing live updates.
//                    val result = repository
//                        .getCompatibilityResult(ZodiacSign.ARIES, ZodiacSign.LEO)
//                        .first()
//
//                    if (result == null) {
//                        status = "Got NULL — check println logs (Logcat) for which tier missed"
//                    } else {
//                        status = "Got a result"
//                        resultText = buildString {
//                            appendLine("id: ${result.id}")
//                            appendLine("signs: ${result.signs.map { it.firestoreKey }}")
//                            appendLine("matchPercent: ${result.matchPercent}")
//                            appendLine("scores: ${result.scores}")
//                            appendLine()
//                            appendLine("EN summary: ${result.content.en.summary}")
//                            appendLine()
//                            appendLine("TR summary: ${result.content.tr.summary}")
//                        }
//                    }
//                }
//            }) {
//                Text(text = "Fetch Aries + Leo")
//            }
//
//            Text(text = status, color = YzGold)
//            Text(text = resultText, color = YzGold)
//        }
//    }