package com.kg.yildizname.feature.compatability.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kg.yildizname.core.ui.theme.DarkGray
import com.kg.yildizname.core.ui.theme.YzGold
import com.kg.yildizname.core.ui.theme.YzInk

@Composable
fun ResultColumn(
    score: Int,
    scoreDesc: String = "Mükemmel Uyum",
    elementA: String,
    elementB: String,
    genericElementExp: String = "Zıtlıkların Dansı",
    iconA: ImageVector,
    iconB: ImageVector,
    iconATint: Color,
    iconBTint: Color
) {
    Box() {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Text(
                text = "$score %",
                color = YzGold,
                style = MaterialTheme.typography.headlineLarge
            )
            Text(
                text = scoreDesc,
                color = YzInk,
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(Modifier.height(8.dp))
            Box(
                modifier = Modifier.padding(horizontal = 8.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(DarkGray.copy(0.4f)),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp)
                ) {
                    Icon(
                        imageVector = iconA,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = iconATint
                    )
                    Spacer(Modifier.width(4.dp))
                    Icon(
                        imageVector = iconB,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = iconBTint
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = "$elementA + $elementB",
                        color = YzGold.copy(0.8f),
                        fontSize = 12.sp
                    )
                }
            }
            Text(
                text = "$genericElementExp",
                color = YzInk,
                fontSize = 10.sp
            )
        }
    }
}