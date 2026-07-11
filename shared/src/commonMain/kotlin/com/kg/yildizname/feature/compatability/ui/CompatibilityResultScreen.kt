package com.kg.yildizname.feature.compatability.ui

import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kg.yildizname.core.ui.theme.YzGold
import com.kg.yildizname.core.ui.theme.YzInk
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Air
import androidx.compose.material.icons.rounded.Landscape
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.kg.yildizname.core.ui.components.StarFieldBackground
import com.kg.yildizname.core.ui.theme.DarkGray
import com.kg.yildizname.core.ui.theme.YzBg
import com.kg.yildizname.core.ui.theme.YzSurfaceAlt
import com.kg.yildizname.core.ui.utils.yzStatusBarsPadding
import com.kg.yildizname.feature.compatability.ui.components.CompatButton
import com.kg.yildizname.feature.compatability.ui.components.ResultColumn
import com.kg.yildizname.feature.compatability.ui.components.Scores
import com.kg.yildizname.feature.compatability.ui.components.SignBox
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowLeft
import horoscope.shared.generated.resources.Res
import horoscope.shared.generated.resources.capricorn_svgrepo_com
import horoscope.shared.generated.resources.cd_back
import horoscope.shared.generated.resources.compat_subtitle
import horoscope.shared.generated.resources.compat_title
import horoscope.shared.generated.resources.gemini_svgrepo_com
import org.jetbrains.compose.resources.stringResource

@Composable
fun CompatibilityResultScreen(
    onBackClick: () -> Unit = {}
)
{
    StarFieldBackground()
    Box(modifier = Modifier.yzStatusBarsPadding().fillMaxSize())
    {
        Column(modifier = Modifier
            .padding(horizontal = 8.dp)
            .padding(bottom = 32.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Row(modifier = Modifier.fillMaxWidth()) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = FeatherIcons.ArrowLeft,
                        contentDescription = stringResource(Res.string.cd_back),
                        tint = YzInk
                    )
                }
            }
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
            Spacer(Modifier.height(16.dp))
            SignBox(
                sign= "CAPRICORN",
                icon = Res.drawable.capricorn_svgrepo_com,
                element =  "Earth"
            )
            Spacer(Modifier.height(32.dp))
            ResultColumn(
                score = 68,
                elementA = "Earth",
                elementB = "Air",
                iconA = Icons.Rounded.Landscape,
                iconB = Icons.Rounded.Air
            )
            Spacer(Modifier.height(32.dp))
            SignBox(
                sign = "GEMINI",
                icon = Res.drawable.gemini_svgrepo_com,
                element = "Air"
            )
            Spacer(Modifier.height(32.dp))
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        SpanStyle(
                            // A smooth gradient fading from a rich ink to a slightly softer tone
                            brush = Brush.linearGradient(
                                colors = listOf(YzInk, YzInk.copy(alpha = 0.85f))
                            ),
                            // Premium typography settings
                            fontFamily = FontFamily.Serif,
                            fontSize = 14.sp,
                            letterSpacing = 0.5.sp,

                        )
                    ) {
                        append(
                            "Akrep ve Aslan arasındaki bu çekim, derin bir tutku ve sadakat temeline dayanır. " +
                                    "Su ve Ateşin birleşimi zorlayıcı olsa da, karşılıklı saygı ile sarsılmaz bir " +
                                    "bağa dönüşebilir. Her iki burç da sabit nitelikte olduğundan, " +
                                    "birbirlerine olan bağlılıkları zamanın ötesinde bir güç barındırır."
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                style = TextStyle(
                    lineHeight = 24.sp, // Adds elegant breathing room between lines
                    textAlign = TextAlign.Justify
                )
            )
            Spacer(Modifier.height(16.dp))
            Scores(90,"Aşk")
            Spacer(Modifier.height(8.dp))
            Scores(60,"İş")
            Spacer(Modifier.height(8.dp))
            Scores(70,"Para")
            Spacer(Modifier.height(8.dp))
            Scores(100,"Hayat")
            Spacer(Modifier.height(16.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                CompatButton(
                    text = "Detaylı Analiz",
                    filled = false,
                    onClick = { }
                )

                CompatButton(
                    text = "Paylaş",
                    filled = true,
                    onClick = { }
                )
            }
        }
    }
}
/**/