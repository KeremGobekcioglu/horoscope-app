package com.kg.yildizname.feature.home.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kg.yildizname.core.data.model.ZodiacSign
import com.kg.yildizname.core.ui.theme.YzGold
import com.kg.yildizname.core.ui.theme.YzMuted
import com.kg.yildizname.core.util.yzUppercase
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun SignHeader(
    sign: ZodiacSign,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = stringResource(sign.nameRes).yzUppercase(),
            color = YzGold,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 5.sp,
            // Uses YzTypography display/serif family — ensure it's applied at theme level
        )
        Text(
            text = stringResource(sign.dateRangeRes).yzUppercase(),
            color = YzMuted,
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            letterSpacing = 1.5.sp
        )
    }
}
