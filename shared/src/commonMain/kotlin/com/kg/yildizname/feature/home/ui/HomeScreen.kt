package com.kg.yildizname.feature.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kg.yildizname.core.ui.components.StarFieldBackground
import com.kg.yildizname.core.ui.theme.YzBg
import com.kg.yildizname.core.ui.theme.YzBorder
import com.kg.yildizname.core.ui.theme.YzCardBg
import com.kg.yildizname.core.ui.theme.YzGold
import com.kg.yildizname.core.ui.theme.YzMuted
import com.kg.yildizname.core.ui.theme.YzOnSurface
import org.jetbrains.compose.resources.painterResource

private fun monthName(month: Int): String = when (month) {
    1 -> "Ocak"; 2 -> "Şubat"; 3 -> "Mart"; 4 -> "Nisan"
    5 -> "Mayıs"; 6 -> "Haziran"; 7 -> "Temmuz"; 8 -> "Ağustos"
    9 -> "Eylül"; 10 -> "Ekim"; 11 -> "Kasım"; 12 -> "Aralık"
    else -> ""
}

private fun genderDisplay(key: String): String = when (key) {
    "MALE"       -> "Erkek"
    "FEMALE"     -> "Kadın"
    "OTHER"      -> "Diğer"
    "PREFER_NOT" -> "Belirtmek istemiyorum"
    else         -> key
}

@Composable
private fun DataRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically,
    ) {
        Text(label, color = YzMuted, fontSize = 13.sp, fontWeight = FontWeight.Medium)
        Text(value, color = YzOnSurface, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onClearOnboarding: () -> Unit,
    onReadingDetail: (sign: String, period: String) -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize().background(YzBg),
    ) {
        StarFieldBackground(Modifier.fillMaxSize())

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(32.dp))

            val sign = uiState.zodiacSign
            if (sign != null) {
                // Sign avatar
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(YzCardBg)
                        .border(1.5.dp, YzGold.copy(alpha = 0.6f), CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Image(
                        painter            = painterResource(sign.drawable),
                        contentDescription = sign.nameTr,
                        contentScale       = ContentScale.Fit,
                        modifier           = Modifier.size(72.dp),
                    )
                }

                Spacer(Modifier.height(16.dp))

                Text(
                    text       = sign.nameTr,
                    color      = YzGold,
                    fontSize   = 28.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle  = FontStyle.Italic,
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text     = sign.dateRange,
                    color    = YzMuted,
                    fontSize = 13.sp,
                )
            } else if (!uiState.isLoading) {
                Text(
                    text       = "Hoşgeldin!",
                    color      = YzGold,
                    fontSize   = 28.sp,
                    fontWeight = FontWeight.Bold,
                )
            }

            Spacer(Modifier.height(32.dp))

            // Profile card
            val hasAnyData = uiState.birthDay != null || uiState.birthTime != null ||
                             uiState.birthCity != null || uiState.gender != null

            if (!uiState.isLoading && hasAnyData) {
                Box(
                    modifier = Modifier
                        .widthIn(max = 480.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(YzCardBg)
                        .border(1.dp, YzBorder, RoundedCornerShape(16.dp)),
                ) {
                    Column {
                        Text(
                            text       = "Profil Bilgilerin",
                            color      = YzGold,
                            fontSize   = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier   = Modifier.padding(horizontal = 20.dp, vertical = 14.dp),
                        )

                        HorizontalDivider(color = YzBorder, thickness = 0.5.dp)

                        if (uiState.birthDay != null && uiState.birthMonth != null && uiState.birthYear != null) {
                            DataRow(
                                label = "Doğum Tarihi",
                                value = "${uiState.birthDay} ${monthName(uiState.birthMonth)} ${uiState.birthYear}",
                            )
                            HorizontalDivider(color = YzBorder.copy(alpha = 0.4f), thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 20.dp))
                        }

                        if (!uiState.birthTime.isNullOrBlank()) {
                            DataRow(label = "Doğum Saati", value = uiState.birthTime)
                            HorizontalDivider(color = YzBorder.copy(alpha = 0.4f), thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 20.dp))
                        }

                        if (!uiState.birthCity.isNullOrBlank()) {
                            DataRow(label = "Doğum Yeri", value = uiState.birthCity)
                            HorizontalDivider(color = YzBorder.copy(alpha = 0.4f), thickness = 0.5.dp, modifier = Modifier.padding(horizontal = 20.dp))
                        }

                        if (!uiState.gender.isNullOrBlank()) {
                            DataRow(label = "Cinsiyet", value = genderDisplay(uiState.gender))
                        }
                    }
                }
            }

            Spacer(Modifier.weight(1f))

            // Debug reset button
            Text(
                text      = "Onboardingi Sıfırla",
                color     = YzMuted.copy(alpha = 0.5f),
                fontSize  = 11.sp,
                textAlign = TextAlign.Center,
                modifier  = Modifier
                    .clickable(onClick = onClearOnboarding)
                    .padding(horizontal = 24.dp, vertical = 8.dp),
            )

            Spacer(Modifier.navigationBarsPadding().height(16.dp))
        }
    }
}
