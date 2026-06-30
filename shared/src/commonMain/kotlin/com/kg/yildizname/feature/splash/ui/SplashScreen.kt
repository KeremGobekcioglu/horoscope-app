package com.kg.yildizname.feature.splash.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kg.yildizname.core.ui.theme.YzBg
import com.kg.yildizname.core.ui.theme.YzGold
import com.kg.yildizname.core.ui.theme.YzMuted
import com.kg.yildizname.core.ui.theme.YzStarWhite
import horoscope.shared.generated.resources.Res
import horoscope.shared.generated.resources.app_name
import horoscope.shared.generated.resources.scorpio_constellation_asset_2x
import horoscope.shared.generated.resources.splash_tagline
import kotlinx.coroutines.delay
import kotlin.random.Random
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

private data class StarParticle(
    val x: Float,
    val y: Float,
    val radius: Float,
    val baseAlpha: Float,
    val isGlow: Boolean,
)

private fun generateStars(seed: Long = 42L): List<StarParticle> {
    val rng = Random(seed)
    return buildList {
        repeat(90) {
            add(StarParticle(
                x         = rng.nextFloat(),
                y         = rng.nextFloat(),
                radius    = rng.nextFloat() * 0.5f + 0.5f,
                baseAlpha = rng.nextFloat() * 0.4f + 0.2f,
                isGlow    = false,
            ))
        }
        repeat(7) {
            add(StarParticle(
                x         = rng.nextFloat(),
                y         = rng.nextFloat(),
                radius    = 1.5f,
                baseAlpha = 0.85f,
                isGlow    = true,
            ))
        }
    }
}

@Composable
fun SplashScreen(
    onAnimationDone: () -> Unit,
) {
    val stars = remember { generateStars() }

    var phase by remember { mutableStateOf(0) }
    LaunchedEffect(Unit) {
        delay(100)  ; phase = 1
        delay(200)  ; phase = 2
        delay(900)  ; phase = 3
        delay(200)  ; phase = 4
        delay(800)  ; phase = 5
    }

    val starFieldAlpha by animateFloatAsState(
        targetValue   = if (phase >= 1) 1f else 0f,
        animationSpec = tween(500, easing = LinearEasing),
        label         = "starFieldAlpha",
    )

    val constellationAlpha by animateFloatAsState(
        targetValue   = if (phase >= 2) 1f else 0f,
        animationSpec = tween(900, easing = FastOutSlowInEasing),
        label         = "constellationAlpha",
    )
    val constellationScale by animateFloatAsState(
        targetValue   = if (phase >= 2) 1f else 0.88f,
        animationSpec = tween(900, easing = FastOutSlowInEasing),
        label         = "constellationScale",
    )

    val infiniteTransition = rememberInfiniteTransition(label = "glowPulse")
    val glowPulse by infiniteTransition.animateFloat(
        initialValue  = 0.55f,
        targetValue   = 1f,
        animationSpec = infiniteRepeatable(
            animation  = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "glowPulse",
    )
    val effectiveGlow = if (phase >= 3) glowPulse else 0.55f

    val textAlpha by animateFloatAsState(
        targetValue   = if (phase >= 4) 1f else 0f,
        animationSpec = tween(600, easing = FastOutSlowInEasing),
        label         = "textAlpha",
    )
    val textOffsetY by animateFloatAsState(
        targetValue   = if (phase >= 4) 0f else 14f,
        animationSpec = tween(600, easing = FastOutSlowInEasing),
        label         = "textOffsetY",
    )

    LaunchedEffect(phase) {
        if (phase >= 5) {
            delay(100)
            onAnimationDone()
        }
    }

    Box(
        modifier         = Modifier.fillMaxSize().background(YzBg),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize().alpha(starFieldAlpha),
        ) {
            stars.forEach { star ->
                val px = star.x * size.width
                val py = star.y * size.height
                val r  = star.radius.dp.toPx()
                if (star.isGlow) {
                    drawCircle(
                        brush  = Brush.radialGradient(
                            colors = listOf(
                                YzStarWhite.copy(alpha = star.baseAlpha * 0.35f),
                                Color.Transparent,
                            ),
                            center = Offset(px, py),
                            radius = r * 6f,
                        ),
                        radius = r * 6f,
                        center = Offset(px, py),
                    )
                }
                drawCircle(
                    color  = YzStarWhite.copy(alpha = star.baseAlpha),
                    radius = r,
                    center = Offset(px, py),
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier         = Modifier
                    .size(180.dp)
                    .alpha(constellationAlpha)
                    .scale(constellationScale),
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                YzGold.copy(alpha = 0.18f * effectiveGlow),
                                Color.Transparent,
                            ),
                        ),
                    )
                }

                Image(
                    painter            = painterResource(Res.drawable.scorpio_constellation_asset_2x),
                    contentDescription = "Scorpio constellation",
                    contentScale       = ContentScale.Fit,
                    modifier           = Modifier.fillMaxSize(),
                )
            }

            Spacer(Modifier.height(28.dp))

            Text(
                text          = stringResource(Res.string.app_name),
                color         = YzGold,
                fontSize      = 28.sp,
                fontWeight    = FontWeight.SemiBold,
                letterSpacing = 4.sp,
                textAlign     = TextAlign.Center,
                modifier      = Modifier.alpha(textAlpha).offset(y = textOffsetY.dp),
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text          = stringResource( Res.string.splash_tagline),
                color         = YzMuted,
                fontSize      = 13.sp,
                fontWeight    = FontWeight.Medium,
                fontStyle     = FontStyle.Italic,
                letterSpacing = 1.sp,
                textAlign     = TextAlign.Center,
                modifier      = Modifier.alpha(textAlpha * 0.8f).offset(y = textOffsetY.dp),
            )
        }
    }
}
