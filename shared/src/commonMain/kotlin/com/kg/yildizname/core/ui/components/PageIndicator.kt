package com.kg.yildizname.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.kg.yildizname.core.ui.theme.YzGold
import com.kg.yildizname.core.ui.theme.YzMuted

@Composable
fun PageIndicator(currentStep: Int, totalSteps: Int = 3) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment     = Alignment.CenterVertically,
    ) {
        repeat(totalSteps) { idx ->
            val selected = idx == currentStep
            Box(
                modifier = Modifier
                    .size(if (selected) 10.dp else 8.dp)
                    .clip(RoundedCornerShape(50))
                    .background(if (selected) YzGold else YzMuted.copy(alpha = 0.4f))
            )
        }
    }
}
