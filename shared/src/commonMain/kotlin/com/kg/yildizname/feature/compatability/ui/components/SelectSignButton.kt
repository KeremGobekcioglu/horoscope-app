package com.kg.yildizname.feature.compatability.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kg.yildizname.core.data.model.ZodiacSign
import com.kg.yildizname.core.ui.theme.SquareShape
import com.kg.yildizname.core.ui.theme.YzGold
import com.kg.yildizname.core.ui.theme.YzOnSurface
import horoscope.shared.generated.resources.Res
import horoscope.shared.generated.resources.compat_select_sign
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun SelectSignButton(
    modifier: Modifier = Modifier,
    selectSign: () -> Unit,
    canShine: Boolean,
    textBelow: String,
    selectedSign: ZodiacSign? = null,
) {
    val shape = SquareShape

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if(isPressed) 1.15f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium)
    )
    val rotationX by animateFloatAsState(
        targetValue = if(isPressed) 8f else 0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "rotationX"
    )
    val iconRotation by animateFloatAsState(
        targetValue = if(isPressed) 90f else 0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow),
        label = "iconRotation"
    )
    val borderAlpha by animateFloatAsState(
        targetValue = if(isPressed) 1f else 0.6f,
        label = "borderAlpha",
    )
    Box(
        modifier = modifier
            .graphicsLayer{
                scaleX = scale
                scaleY = scale
                this.rotationX = rotationX
                cameraDistance = 12f * density
            }
            .clickable(
//                enabled = true,
                interactionSource = interactionSource,
                indication = null,
                onClick = selectSign)
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
            .border(width = 2.dp , shape = shape , color = YzGold.copy(alpha = borderAlpha))
            .clip(shape)
            .background(YzOnSurface.copy(0.15f)),
        contentAlignment = Alignment.Center
    )
    {
        if (selectedSign != null) {
            Icon(
                painter = painterResource(selectedSign.compatGridIcon),
                contentDescription = null,
                tint = YzGold.copy(0.75f),
                modifier = Modifier.fillMaxSize().padding(20.dp),
            )
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            )

            {
                // plus icon
                // text
                Box(modifier = Modifier.graphicsLayer { rotationZ = iconRotation }) {
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
}