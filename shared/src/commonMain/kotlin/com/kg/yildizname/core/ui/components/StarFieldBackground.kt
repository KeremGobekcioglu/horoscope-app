package com.kg.yildizname.core.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.kg.yildizname.core.ui.theme.YzBg
import com.kg.yildizname.core.ui.theme.YzStarWhite
import com.kg.yildizname.core.ui.theme.YzViolet
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random

private data class Star(
    val x: Float,               // base position, 0..1 fraction of width
    val y: Float,               // base position, 0..1 fraction of height
    val radius: Float,
    val alpha: Float,
    val isGlow: Boolean,
    val twinklePhase: Float,
    val twinkleSpeed: Float,
    val isMobile: Boolean,      // only ~20% of background stars ever slide
    val cycleLength: Float,     // seconds between slides
    val phaseOffset: Float,     // staggers when each star's cycle starts
    val slideDuration: Float,   // seconds the slide itself takes
    val slideRadius: Float,     // how far it can wander from base spot, px
    val seed: Int
)

// Deterministic pseudo-random float in [0,1) from an int — lets each mobile star's
// "next resting spot" be computed purely from time, no per-star mutable state needed.
private fun hash(n: Int): Float {
    val x = sin(n * 12.9898f) * 43758.5453f
    return x - floor(x)
}

private fun smoothstep(t: Float): Float = t * t * (3f - 2f * t)

private fun buildStars(seed: Long = 99L): List<Star> {
    val rng = Random(seed)
    return buildList {
        repeat(90) { i ->
            val mobile = rng.nextFloat() < 0.45f
            add(
                Star(
                    x = rng.nextFloat(),
                    y = rng.nextFloat(),
                    radius = rng.nextFloat() * 0.5f + 0.5f,
                    alpha = rng.nextFloat() * 0.35f + 0.35f,
                    isGlow = false,
                    twinklePhase = rng.nextFloat() * 2f * PI.toFloat(),
                    twinkleSpeed = rng.nextFloat() * 0.1f + 0.06f,
                    isMobile = mobile,
                    cycleLength = rng.nextFloat() * 6f + 5f,      // holds still 5-11s
                    phaseOffset = rng.nextFloat() * 30f,          // stagger so they don't sync up
                    slideDuration = rng.nextFloat() * 1f + 1.5f,  // slide itself: 1.5-2.5s
                    slideRadius = rng.nextFloat() * 40f + 30f,    // wanders 30-70px
                    seed = i
                )
            )
        }
        repeat(6) { i ->
            add(
                Star(
                    x = rng.nextFloat(),
                    y = rng.nextFloat(),
                    radius = 1.5f,
                    alpha = 0.75f,
                    isGlow = true,
                    twinklePhase = rng.nextFloat() * 2f * PI.toFloat(),
                    twinkleSpeed = rng.nextFloat() * 0.06f + 0.04f,
                    isMobile = false, // glow stars are anchors — never slide
                    cycleLength = 0f,
                    phaseOffset = 0f,
                    slideDuration = 0f,
                    slideRadius = 0f,
                    seed = 1000 + i
                )
            )
        }
    }
}

@Composable
fun StarFieldBackground(modifier: Modifier = Modifier) {
    val stars = remember { buildStars() }
    val scope = rememberCoroutineScope()

    // Time in seconds — feeds both twinkle and the slide-cycle math below.
    val timeState = rememberInfiniteTransition(label = "starFieldTime").animateFloat(
        initialValue = 0f,
        targetValue = 100_000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 100_000_000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "time"
    )

    // Touch parallax — user-driven only, springs back on release. Not autonomous motion.
    val parallaxX = remember { Animatable(0f) }
    val parallaxY = remember { Animatable(0f) }

    // Rare shooting star — same "slide occasionally, then nothing" philosophy.
    var meteorVisible by remember { mutableStateOf(false) }
    var meteorStart by remember { mutableStateOf(Offset(0.2f, 0.05f)) }
    var meteorAngleDeg by remember { mutableStateOf(30f) }
    val meteorProgress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(Random.nextLong(18_000, 30_000))
            meteorStart = Offset(Random.nextFloat() * 0.6f, Random.nextFloat() * 0.25f)
            meteorAngleDeg = Random.nextFloat() * 20f + 20f
            meteorVisible = true
            meteorProgress.snapTo(0f)
            meteorProgress.animateTo(1f, tween(durationMillis = 900, easing = FastOutSlowInEasing))
            meteorVisible = false
        }
    }

    Canvas(
        modifier = modifier.pointerInput(Unit) {
            detectDragGestures(
                onDrag = { change, dragAmount ->
                    change.consume()
                    scope.launch {
                        parallaxX.animateTo(
                            (parallaxX.value + dragAmount.x * 0.1f).coerceIn(-20f, 20f),
                            spring(dampingRatio = 0.9f)
                        )
                    }
                    scope.launch {
                        parallaxY.animateTo(
                            (parallaxY.value + dragAmount.y * 0.1f).coerceIn(-20f, 20f),
                            spring(dampingRatio = 0.9f)
                        )
                    }
                },
                onDragEnd = {
                    scope.launch { parallaxX.animateTo(0f, spring(dampingRatio = 0.7f)) }
                    scope.launch { parallaxY.animateTo(0f, spring(dampingRatio = 0.7f)) }
                }
            )
        }
    ) {
        val time = timeState.value
        val pX = parallaxX.value
        val pY = parallaxY.value

        drawRect(color = YzBg)
        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(YzViolet.copy(alpha = 0.16f), YzBg),
                center = Offset(size.width * 0.5f, size.height * 0.18f),
                radius = size.width.coerceAtLeast(size.height) * 0.9f
            )
        )

        stars.forEach { s ->
            var offsetX = 0f
            var offsetY = 0f
            var isSliding = false
            var slideProgress = 0f
            var slideDirX = 0f
            var slideDirY = 0f
            var slideLen = 0f

            if (s.isMobile) {
                val localTime = time + s.phaseOffset
                val cycleIndex = floor(localTime / s.cycleLength).toInt()
                val localT = localTime - cycleIndex * s.cycleLength

                fun targetFor(cycle: Int): Offset {
                    val dx = (hash(s.seed * 7919 + cycle) * 2f - 1f) * s.slideRadius
                    val dy = (hash(s.seed * 7919 + cycle + 4242) * 2f - 1f) * s.slideRadius
                    return Offset(dx, dy)
                }

                val from = targetFor(cycleIndex - 1)
                val to = targetFor(cycleIndex)
                val rawT = (localT / s.slideDuration).coerceIn(0f, 1f)
                val t = smoothstep(rawT)

                isSliding = localT < s.slideDuration
                offsetX = if (isSliding) from.x + (to.x - from.x) * t else to.x
                offsetY = if (isSliding) from.y + (to.y - from.y) * t else to.y

                if (isSliding) {
                    slideProgress = rawT
                    val dx = to.x - from.x
                    val dy = to.y - from.y
                    slideLen = sqrt(dx * dx + dy * dy)
                    if (slideLen > 0.001f) {
                        slideDirX = dx / slideLen
                        slideDirY = dy / slideLen
                    }
                }
            }

            val px = s.x * size.width + offsetX + pX * (if (s.isGlow) 0.7f else 0.4f)
            val py = s.y * size.height + offsetY + pY * (if (s.isGlow) 0.7f else 0.4f)
            val r = s.radius.dp.toPx()

            val flicker = sin(time * s.twinkleSpeed + s.twinklePhase)
            val twinkleAlpha = (s.alpha * (0.75f + 0.25f * flicker)).coerceIn(0.3f, 1f)

            if (s.isGlow) {
                drawCircle(
                    brush = Brush.radialGradient(
                        listOf(YzStarWhite.copy(alpha = twinkleAlpha * 0.3f), Color.Transparent),
                        center = Offset(px, py), radius = r * 6f,
                    ),
                    radius = r * 6f, center = Offset(px, py),
                )
            }

            if (isSliding && slideLen > 0.001f) {
                // meteor-style fading trail behind the star while it's mid-slide
                val fade = when {
                    slideProgress < 0.2f -> slideProgress / 0.2f
                    slideProgress > 0.8f -> (1f - slideProgress) / 0.2f
                    else -> 1f
                }
                val tailLen = slideLen * 0.9f
                val tailX = px - slideDirX * tailLen
                val tailY = py - slideDirY * tailLen
                drawLine(
                    brush = Brush.linearGradient(
                        listOf(Color.Transparent, YzStarWhite.copy(alpha = twinkleAlpha * fade)),
                        start = Offset(tailX, tailY), end = Offset(px, py)
                    ),
                    start = Offset(tailX, tailY),
                    end = Offset(px, py),
                    strokeWidth = (r * 0.9f).coerceAtLeast(1f),
                    cap = StrokeCap.Round
                )
            }

            drawCircle(YzStarWhite.copy(alpha = twinkleAlpha), r, Offset(px, py))
        }

        if (meteorVisible) {
            val progress = meteorProgress.value
            val rad = meteorAngleDeg * PI.toFloat() / 180f
            val travelPx = size.width * 0.35f
            val headX = meteorStart.x * size.width + cos(rad) * travelPx * progress
            val headY = meteorStart.y * size.height + sin(rad) * travelPx * progress
            val tailLen = travelPx * 0.22f
            val tailX = headX - cos(rad) * tailLen
            val tailY = headY - sin(rad) * tailLen

            val fadeAlpha = when {
                progress < 0.15f -> progress / 0.15f
                progress > 0.75f -> (1f - progress) / 0.25f
                else -> 1f
            } * 0.85f

            drawLine(
                brush = Brush.linearGradient(
                    listOf(Color.Transparent, YzStarWhite.copy(alpha = fadeAlpha)),
                    start = Offset(tailX, tailY), end = Offset(headX, headY)
                ),
                start = Offset(tailX, tailY),
                end = Offset(headX, headY),
                strokeWidth = 2.dp.toPx(),
                cap = StrokeCap.Round
            )
            drawCircle(YzStarWhite.copy(alpha = fadeAlpha), radius = 2.dp.toPx(), center = Offset(headX, headY))
        }
    }
}