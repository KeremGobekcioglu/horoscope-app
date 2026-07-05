package com.kg.yildizname.feature.calendar.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kg.yildizname.core.ui.theme.YzBg
import com.kg.yildizname.core.ui.theme.YzBorder
import com.kg.yildizname.core.ui.theme.YzGold
import com.kg.yildizname.core.ui.theme.YzMuted
import com.kg.yildizname.core.ui.theme.YzSurface
import horoscope.shared.generated.resources.Res
import horoscope.shared.generated.resources.home_error_retry
import org.jetbrains.compose.resources.stringResource


@Composable
internal fun CalendarLoadingContent(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize().background(YzBg), contentAlignment = Alignment.Center) {
        Text(text = "✦", color = YzGold.copy(alpha = 0.5f), fontSize = 28.sp)
    }
}