package com.kg.yildizname.feature.compatability.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kg.yildizname.core.data.model.ZodiacSign
import com.kg.yildizname.core.data.repository.CompatibilityRepository
import com.kg.yildizname.core.ui.theme.YzGold
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

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
fun CompatibilityScreen() {
    val repository = koinInject<CompatibilityRepository>()
    val scope = rememberCoroutineScope()

    var status by remember { mutableStateOf("Not fetched yet") }
    var resultText by remember { mutableStateOf("") }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = "Compatibility — Repository Test")

            Button(onClick = {
                status = "Fetching..."
                resultText = ""
                scope.launch {
                    // .first() takes just the first emission — fine for a manual test,
                    // since we're eyeballing one result at a time, not observing live updates.
                    val result = repository
                        .getCompatibilityResult(ZodiacSign.ARIES, ZodiacSign.LEO)
                        .first()

                    if (result == null) {
                        status = "Got NULL — check println logs (Logcat) for which tier missed"
                    } else {
                        status = "Got a result"
                        resultText = buildString {
                            appendLine("id: ${result.id}")
                            appendLine("signs: ${result.signs.map { it.firestoreKey }}")
                            appendLine("matchPercent: ${result.matchPercent}")
                            appendLine("scores: ${result.scores}")
                            appendLine()
                            appendLine("EN summary: ${result.content.en.summary}")
                            appendLine()
                            appendLine("TR summary: ${result.content.tr.summary}")
                        }
                    }
                }
            }) {
                Text(text = "Fetch Aries + Leo")
            }

            Text(text = status, color = YzGold)
            Text(text = resultText, color = YzGold)
        }
    }
}