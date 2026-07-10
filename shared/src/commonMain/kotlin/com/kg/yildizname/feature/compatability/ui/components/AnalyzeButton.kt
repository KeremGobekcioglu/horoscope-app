package com.kg.yildizname.feature.compatability.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kg.yildizname.core.ui.theme.ButtonShape
import com.kg.yildizname.core.ui.theme.NeonPurple
import com.kg.yildizname.core.ui.theme.YzBg
import com.kg.yildizname.core.ui.theme.YzGold
import com.kg.yildizname.core.ui.theme.YzInk
import horoscope.shared.generated.resources.Res
import horoscope.shared.generated.resources.compat_analyze_button
import org.jetbrains.compose.resources.stringResource

@Composable
fun AnalyzeButton(isEnabled: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit)
{
    val shape = ButtonShape

    val contentColor by animateColorAsState(
        targetValue = if(isEnabled) YzGold else YzInk.copy(0.5f),
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "contentColor"
    )

    val backgroundColor by animateColorAsState(
        targetValue = if(isEnabled) NeonPurple/*YzGold.copy(0.6f) */else YzBg.copy(0.6f),
        label = "backgroundColor"
    )

    val borderAlpha by animateFloatAsState(
        targetValue = if(isEnabled) 1f else 0f,
        animationSpec = spring(Spring.DampingRatioMediumBouncy),
        label = "borderAlpha"
    )
    val scale by animateFloatAsState(
        targetValue = if(isEnabled) 0.95f else 1f,
        animationSpec = spring(Spring.DampingRatioMediumBouncy),
        label = ""
    )
    val shineElevation by animateFloatAsState(
        targetValue = if(isEnabled) 4f else 0f,
        animationSpec = spring(Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = ""
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer{
                scaleX = scale
                scaleY = scale
            }
            .clip(shape)
            .background(backgroundColor)
            .border(1.dp, color = YzGold.copy(borderAlpha),shape = shape)
//            .shadow(
//                elevation = shineElevation.dp,
//                shape = shape,
//                ambientColor = YzGold,
//                spotColor = YzGold
//            )
            .clickable(
                enabled = isEnabled,
                onClick = onClick
            )
            .padding(horizontal = 32.dp, vertical = 16.dp)
    )
    {
        /* now two content , one icon and one text in a row
        * */
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)

        )
        {
            Icon(
                imageVector = Icons.Default.Analytics,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = stringResource(Res.string.compat_analyze_button),
                color = contentColor,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}