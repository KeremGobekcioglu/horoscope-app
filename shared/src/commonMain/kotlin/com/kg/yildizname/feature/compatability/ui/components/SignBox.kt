package com.kg.yildizname.feature.compatability.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import com.kg.yildizname.core.data.model.ZodiacSign
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.koin.core.component.getScopeName
import com.kg.yildizname.core.data.model.localizedName
import com.kg.yildizname.core.ui.theme.LightGray
import com.kg.yildizname.core.ui.theme.SquareShape
import com.kg.yildizname.core.ui.theme.YzBg
import com.kg.yildizname.core.ui.theme.YzGold
import com.kg.yildizname.core.ui.theme.YzInk

@Composable
fun SignBox(
    sign: String,
    icon: DrawableResource,
    element: String
)
{
    /*
    * Icon in a box. needs animation up and down and glow.
    * sign name
    * sign element "grubu"
    *
    * */
    val shape = SquareShape
    val infiniteTransition = rememberInfiniteTransition("floating")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.8f,
        infiniteRepeatable(
            animation = tween(
                durationMillis = 1200
            ),
            repeatMode = RepeatMode.Reverse
        )
    )
    val moveY by infiniteTransition.animateFloat(
        initialValue = -3f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1200,
                easing = LinearEasing
            ),
            // BACK AND forth
            repeatMode = RepeatMode.Reverse
        ),
        label = "moveY"
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier.padding(8.dp)
                .size(64.dp)
                .graphicsLayer{
                    translationY = moveY
                }
                .dropShadow(
                    shape = shape,
                    shadow = Shadow(
                        radius = 24.dp,
                        spread = 2.dp,
                        color = YzGold.copy(alpha = glowAlpha)
                    )
                )
                .clip(shape)
//                .dropShadow(shape = shape, color = YzGold.copy(glowAlpha), blur = 24.dp, spread = 2.dp)
                .background(YzBg.copy(0.4f))
                .border(1.dp, LightGray, shape),
            contentAlignment = Alignment.Center
        )
        {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = YzGold,
                modifier = Modifier.size(48.dp)
            )
        }
        Spacer(Modifier.height(16.dp))
        Text(
            text = sign,
            style = MaterialTheme.typography.headlineLarge,
            color = YzGold
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = element,
            style = MaterialTheme.typography.headlineSmall,
            color = YzInk
        )
    }
}