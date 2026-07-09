package com.kg.yildizname.feature.compatability.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kg.yildizname.core.data.model.ZodiacSign
import com.kg.yildizname.core.data.repository.CompatibilityRepository
import com.kg.yildizname.core.ui.components.StarFieldBackground
import com.kg.yildizname.core.ui.theme.SquareShape
import com.kg.yildizname.core.ui.theme.YzBg
import com.kg.yildizname.core.ui.theme.YzGold
import com.kg.yildizname.core.ui.theme.YzInk
import com.kg.yildizname.core.ui.theme.YzOnSurface
import com.kg.yildizname.core.ui.theme.YzSurface
import com.kg.yildizname.core.ui.utils.yzStatusBarsPadding
import horoscope.shared.generated.resources.Res
import horoscope.shared.generated.resources.compat_person_one_label
import horoscope.shared.generated.resources.compat_person_two_label
import horoscope.shared.generated.resources.compat_select_sign
import horoscope.shared.generated.resources.compat_subtitle
import horoscope.shared.generated.resources.compat_title
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
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
@Composable
fun selectSignComposable(modifier: Modifier = Modifier, selectSign: () -> Unit, canShine: Boolean, textBelow: String) {
    val shape = SquareShape
    Box(
        modifier = modifier
            .clickable(true, onClick = selectSign)
            .then(
                if (canShine) {
                    Modifier.shadow(
                        elevation = 1.dp,
                        shape = shape,
                        ambientColor = YzGold,
                        spotColor = YzGold
                    )
                } else Modifier
            )
            .border(width = 2.dp , shape = shape , color = YzGold)
            .clip(shape)
            .background(YzOnSurface.copy(0.15f)),
        contentAlignment = Alignment.Center
    )
    {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
            )

        {
            // plus icon
            // text
            Box {
                Icon(Icons.Default.Add, contentDescription = null, tint = YzGold, modifier = Modifier.size(24.dp).offset(0.5.dp, 0.dp))
                Icon(Icons.Default.Add, contentDescription = null, tint = YzGold, modifier = Modifier.size(24.dp))
            }
            Text(
                text = stringResource(Res.string.compat_select_sign),
                color = YzGold,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
        }
    }
}

/***
 *     Box(
 *         modifier = modifier.aspectRatio(1f)
 *             .clickable(isAvailable, onClick = onClick)
 * //            .size(42.dp)
 *             .then(
 *                 if(canShine) {
 *                     Modifier.shadow(elevation = 0.dp, shape = shape, ambientColor = YzGold, spotColor = YzGold)
 *                 } else Modifier
 *             )
 *             .clip(shape)
 *             .background(if(canShine) YzBg.copy(0.1f) else Color.Transparent)
 *             .then(
 *                 if(canShine) {
 *                     Modifier.border(width = 2.dp, color = YzGold, shape = shape)
 *                 } else Modifier
 *             ),
 *         contentAlignment = Alignment.Center
 *     )
 */
@Composable
fun CompatibilityScreen() {
    StarFieldBackground(Modifier.fillMaxSize())

    Box(
        modifier = Modifier.fillMaxSize().yzStatusBarsPadding(),
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
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
            Spacer(Modifier.height(32.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            )
            {
                Column(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    selectSignComposable(
                        modifier = Modifier.size(96.dp),
                        textBelow = stringResource(Res.string.compat_person_one_label),
                        canShine = true, selectSign = {}
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = stringResource(Res.string.compat_person_one_label),
                        color = YzInk,
                        fontSize = 12.sp
                    )
                }
                Column(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    selectSignComposable(
                        modifier = Modifier.size(96.dp),
                        textBelow = stringResource(Res.string.compat_person_one_label),
                        canShine = true, selectSign = {}
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = stringResource(Res.string.compat_person_two_label),
                        color = YzInk,
                        fontSize = 12.sp
                    )
                }

            }
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