package com.kg.yildizname.feature.home.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import compose.icons.FeatherIcons
import compose.icons.feathericons.Activity
import compose.icons.feathericons.Bell
import compose.icons.feathericons.Briefcase
import compose.icons.feathericons.Heart
import compose.icons.feathericons.Image
import compose.icons.feathericons.Share2
import compose.icons.feathericons.Star
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kg.yildizname.core.data.model.Reading
import com.kg.yildizname.core.data.model.ScoreSet
import com.kg.yildizname.core.data.model.ZodiacSign
import com.kg.yildizname.core.ui.components.StarFieldBackground
import com.kg.yildizname.core.ui.theme.*
import com.kg.yildizname.core.ui.utils.YzWindowWidth
import com.kg.yildizname.core.ui.utils.rememberWindowWidth
import horoscope.shared.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

// ─────────────────────────────────────────────
// Entry point — stateless, all callbacks injected
// ─────────────────────────────────────────────

/**
 * HomeScreen — stateless.
 *
 * The nav graph owns the Scaffold + YzBottomNav.
 * This composable is the content body only.
 *
 * @param uiState            Current state from HomeViewModel.
 * @param onReadMoreClick    Navigate to ReadingDetail(sign, period).
 * @param onShareClick       Open native share sheet for the reading text.
 * @param onShareCardClick   Open share card flow (rendered bitmap).
 * @param onNotificationClick Open notification settings.
 * @param onRetryClick       Re-trigger the reading fetch on error.
 */
@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onReadMoreClick: (sign: String, period: String) -> Unit,
    onShareClick: (text: String) -> Unit,
    onShareCardClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val windowWidth = rememberWindowWidth()

    // Constrain content width on tablets / large screens
    val contentMaxWidth: Dp = when (windowWidth) {
        YzWindowWidth.Compact  -> Dp.Infinity
        YzWindowWidth.Medium   -> 520.dp
        YzWindowWidth.Expanded -> 480.dp
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(YzBg),           // #080B1A
        contentAlignment = Alignment.TopCenter
    ) {
        StarFieldBackground(Modifier.fillMaxSize())
        when (uiState) {
            is HomeUiState.Loading -> HomeLoadingContent(
                contentMaxWidth = contentMaxWidth,
                onShareCardClick = onShareCardClick,
                onNotificationClick = onNotificationClick,
            )

            is HomeUiState.Error -> HomeErrorContent(
                message = uiState.message,
                onRetry = onRetryClick,
            )

            is HomeUiState.Success -> HomeSuccessContent(
                reading = uiState.reading,
                todayLabel = uiState.todayLabel,
                contentMaxWidth = contentMaxWidth,
                onReadMoreClick = onReadMoreClick,
                onShareClick = onShareClick,
                onShareCardClick = onShareCardClick,
                onNotificationClick = onNotificationClick,
            )
        }
    }
}

// ─────────────────────────────────────────────
// Success state
// ─────────────────────────────────────────────

@Composable
private fun HomeSuccessContent(
    reading: Reading,
    todayLabel: String,
    contentMaxWidth: Dp,
    onReadMoreClick: (sign: String, period: String) -> Unit,
    onShareClick: (text: String) -> Unit,
    onShareCardClick: () -> Unit,
    onNotificationClick: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .let { if (contentMaxWidth != Dp.Infinity) it.widthIn(max = contentMaxWidth) else it }
                .align(Alignment.TopCenter)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // ── Top bar ──
            HomeTopBar(
                dateLabel = todayLabel,
                onNotificationClick = onNotificationClick,
                onShareCardClick = onShareCardClick,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(24.dp))

            // ── Constellation image ──
            ConstellationHero(
                sign = reading.sign,
                modifier = Modifier
                    .fillMaxWidth(0.65f)
                    .aspectRatio(1f)
            )

            Spacer(Modifier.height(20.dp))

            // ── Sign name + date range ──
            SignHeader(
                sign = reading.sign,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(28.dp))

            // ── Daily reading card ──
            DailyReadingCard(
                text = reading.text,
                onReadMoreClick = {
                    onReadMoreClick(reading.sign.apiKey, reading.period.apiKey)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            )

            Spacer(Modifier.height(28.dp))

            // ── Energy section ──
            EnergySection(
                scores = reading.scores,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            )

            // Bottom padding — leaves room for FAB + bottom nav
            Spacer(Modifier.height(96.dp))
        }

        // ── Share FAB — floats bottom-right ──
        HomeShareFab(
            onClick = { onShareClick(reading.text) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 20.dp)
        )
    }
}

// ─────────────────────────────────────────────
// Top bar
// ─────────────────────────────────────────────

@Composable
private fun HomeTopBar(
    dateLabel: String,
    onNotificationClick: () -> Unit,
    onShareCardClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Date pill
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(999.dp))
                .background(YzSurface)          // #0F1428
                .border(0.5.dp, YzBorder, RoundedCornerShape(999.dp))
                .padding(horizontal = 14.dp, vertical = 7.dp)
        ) {
            Text(
                text = dateLabel.uppercase(),
                color = YzInk,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 1.2.sp
            )
        }

        // Right icons
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TopBarIconButton(
                contentDescription = stringResource(Res.string.home_notification_cd),
                onClick = onNotificationClick,
                icon = FeatherIcons.Bell,
            )
            TopBarIconButton(
                contentDescription = stringResource(Res.string.home_share_card_cd),
                onClick = onShareCardClick,
                icon = FeatherIcons.Image,
            )
        }
    }
}

@Composable
private fun TopBarIconButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(38.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(YzSurface)
            .border(0.5.dp, YzBorder, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = YzInk,
            modifier = Modifier.size(18.dp)
        )
    }
}

// ─────────────────────────────────────────────
// Constellation hero image
// ─────────────────────────────────────────────

@Composable
private fun ConstellationHero(
    sign: ZodiacSign,
    modifier: Modifier = Modifier,
) {
    // Resolve PNG resource by sign — drop files as constellation_scorpio.png etc.
    // into composeResources/drawable/
    val painter: Painter = when (sign) {
        ZodiacSign.ARIES        -> painterResource(Res.drawable.aries_constellation_icon)
        ZodiacSign.TAURUS       -> painterResource(Res.drawable.taurus_constellation_icon)
        ZodiacSign.GEMINI       -> painterResource(Res.drawable.gemini_constellation_icon)
        ZodiacSign.CANCER       -> painterResource(Res.drawable.cancer_constellation_icon)
        ZodiacSign.LEO          -> painterResource(Res.drawable.leo_constellation_icon)
        ZodiacSign.VIRGO        -> painterResource(Res.drawable.virgo_constellation_icon)
        ZodiacSign.LIBRA        -> painterResource(Res.drawable.libra_constellation_icon)
        ZodiacSign.SCORPIO      -> painterResource(Res.drawable.scorpio_constellation_icon)
        ZodiacSign.SAGITTARIUS  -> painterResource(Res.drawable.sagittarius_constellation_icon)
        ZodiacSign.CAPRICORN    -> painterResource(Res.drawable.capricorn_constellation_icon)
        ZodiacSign.AQUARIUS     -> painterResource(Res.drawable.aquarius_constellation_icon)
        ZodiacSign.PISCES       -> painterResource(Res.drawable.pisces_constellation_icon)
    }

    Image(
        painter = painter,
        contentDescription = sign.apiKey,
        contentScale = ContentScale.Fit,
        modifier = modifier
    )
}

// ─────────────────────────────────────────────
// Sign name + date range
// ─────────────────────────────────────────────

@Composable
private fun SignHeader(
    sign: ZodiacSign,
    modifier: Modifier = Modifier,
) {
    val signNameRes = when (sign) {
        ZodiacSign.ARIES        -> Res.string.sign_name_aries
        ZodiacSign.TAURUS       -> Res.string.sign_name_taurus
        ZodiacSign.GEMINI       -> Res.string.sign_name_gemini
        ZodiacSign.CANCER       -> Res.string.sign_name_cancer
        ZodiacSign.LEO          -> Res.string.sign_name_leo
        ZodiacSign.VIRGO        -> Res.string.sign_name_virgo
        ZodiacSign.LIBRA        -> Res.string.sign_name_libra
        ZodiacSign.SCORPIO      -> Res.string.sign_name_scorpio
        ZodiacSign.SAGITTARIUS  -> Res.string.sign_name_sagittarius
        ZodiacSign.CAPRICORN    -> Res.string.sign_name_capricorn
        ZodiacSign.AQUARIUS     -> Res.string.sign_name_aquarius
        ZodiacSign.PISCES       -> Res.string.sign_name_pisces
    }

    val dateRangeRes = when (sign) {
        ZodiacSign.ARIES        -> Res.string.sign_dates_aries
        ZodiacSign.TAURUS       -> Res.string.sign_dates_taurus
        ZodiacSign.GEMINI       -> Res.string.sign_dates_gemini
        ZodiacSign.CANCER       -> Res.string.sign_dates_cancer
        ZodiacSign.LEO          -> Res.string.sign_dates_leo
        ZodiacSign.VIRGO        -> Res.string.sign_dates_virgo
        ZodiacSign.LIBRA        -> Res.string.sign_dates_libra
        ZodiacSign.SCORPIO      -> Res.string.sign_dates_scorpio
        ZodiacSign.SAGITTARIUS  -> Res.string.sign_dates_sagittarius
        ZodiacSign.CAPRICORN    -> Res.string.sign_dates_capricorn
        ZodiacSign.AQUARIUS     -> Res.string.sign_dates_aquarius
        ZodiacSign.PISCES       -> Res.string.sign_dates_pisces
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = stringResource(signNameRes).uppercase(),
            color = YzGold,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 5.sp,
            // Uses YzTypography display/serif family — ensure it's applied at theme level
        )
        Text(
            text = stringResource(dateRangeRes).uppercase(),
            color = YzMuted,
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            letterSpacing = 1.5.sp
        )
    }
}

// ─────────────────────────────────────────────
// Daily reading card
// ─────────────────────────────────────────────

@Composable
private fun DailyReadingCard(
    text: String,
    onReadMoreClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))              // CardShape
            .background(YzSurface)                        // #0F1428
            .border(0.5.dp, YzBorder, RoundedCornerShape(20.dp))
            .padding(20.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

            // Section label with gold bullet
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(RoundedCornerShape(999.dp))
                        .background(YzGold)
                )
                Text(
                    text = stringResource(Res.string.home_daily_commentary_label),
                    color = YzGold,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 2.sp
                )
            }

            // Reading body — truncated to 6 lines
            Text(
                text = text,
                color = YzInk,
                fontSize = 15.sp,
                lineHeight = 24.sp,
                maxLines = 6,
                overflow = TextOverflow.Ellipsis
            )

            // "Read more" link — right-aligned
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                        indication = null,
                        onClick = onReadMoreClick
                    ),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(Res.string.home_read_more),
                    color = YzMuted,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = "→",
                    color = YzGold,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

// ─────────────────────────────────────────────
// Energy section
// ─────────────────────────────────────────────

@Composable
private fun EnergySection(
    scores: ScoreSet,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Section label
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(RoundedCornerShape(999.dp))
                    .background(YzGold)
            )
            Text(
                text = stringResource(Res.string.home_daily_energy_label),
                color = YzGold,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 2.sp
            )
        }

        // 2×2 grid
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                EnergyScoreTile(
                    label = stringResource(Res.string.home_score_love),
                    score = scores.love,
                    icon = FeatherIcons.Heart,
                    modifier = Modifier.weight(1f)
                )
                EnergyScoreTile(
                    label = stringResource(Res.string.home_score_work),
                    score = scores.work,
                    icon = FeatherIcons.Briefcase,
                    modifier = Modifier.weight(1f)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                EnergyScoreTile(
                    label = stringResource(Res.string.home_score_health),
                    score = scores.health,
                    icon = FeatherIcons.Activity,
                    modifier = Modifier.weight(1f)
                )
                EnergyScoreTile(
                    label = stringResource(Res.string.home_score_luck),
                    score = scores.luck,
                    icon = FeatherIcons.Star,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun EnergyScoreTile(
    label: String,
    score: Int,
    icon: ImageVector,
    modifier: Modifier = Modifier,
) {
    // Animate the score bar width from 0 → fraction on first composition
    var animationPlayed by remember { mutableStateOf(false) }
    val animatedFraction by animateFloatAsState(
        targetValue = if (animationPlayed) score / 10f else 0f,
        animationSpec = tween(durationMillis = 900, easing = EaseOutCubic),
        label = "score_bar_$label"
    )
    LaunchedEffect(score) { animationPlayed = true }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(YzSurface)
            .border(0.5.dp, YzBorder, RoundedCornerShape(20.dp))
            .padding(start = 16.dp, end = 16.dp, top = 14.dp, bottom = 14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {

            // Top row: icon (left) + score (right)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = YzMuted,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "$score/10",
                    color = YzGold,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.5.sp
                )
            }

            // Label
            Text(
                text = label,
                color = YzMuted,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 1.5.sp
            )

            // Animated score bar — layered: track + fill + shimmer glow
            ScoreBar(animatedFraction = animatedFraction)
        }
    }
}

/**
 * ScoreBar — thin animated gold bar on a dark track.
 *
 * Visual layering:
 *   1. Dark track (full width, YzSurfaceAlt)
 *   2. Gold fill (animates from 0 → fraction)
 *   3. Bright gold tip — 4dp cap that glows at the leading edge
 *
 * This gives depth without shadows: the tip reads as the energy "cursor".
 */
@Composable
private fun ScoreBar(
    animatedFraction: Float,
    modifier: Modifier = Modifier,
) {
    val goldColor   = YzGold
    val trackColor  = YzSurfaceAlt   // #141830
    val tipColor    = Color(0xFFFFD980)  // brighter than YzGold for the tip highlight

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(3.dp)
            .clip(RoundedCornerShape(999.dp))
            .background(trackColor)
            .drawBehind {
                val barWidth = size.width * animatedFraction
                if (barWidth > 0f) {
                    // Gold fill
                    drawRect(
                        color = goldColor,
                        size = androidx.compose.ui.geometry.Size(barWidth, size.height)
                    )
                    // Bright tip cap — 6dp wide glow at leading edge
                    val tipWidth = 6.dp.toPx().coerceAtMost(barWidth)
                    drawRect(
                        color = tipColor,
                        topLeft = Offset(x = barWidth - tipWidth, y = 0f),
                        size = androidx.compose.ui.geometry.Size(tipWidth, size.height)
                    )
                }
            }
    )
}

// ─────────────────────────────────────────────
// Share FAB
// ─────────────────────────────────────────────

@Composable
private fun HomeShareFab(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(52.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFD4A843),   // warm gold top-left
                        Color(0xFFA07830)    // deeper gold bottom-right
                    )
                )
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = FeatherIcons.Share2,
            contentDescription = stringResource(Res.string.home_share_cd),
            tint = Color(0xFF1A1000),    // near-black on gold — better contrast than white
            modifier = Modifier.size(22.dp)
        )
    }
}

// ─────────────────────────────────────────────
// Loading state — shimmer skeletons
// ─────────────────────────────────────────────

@Composable
private fun HomeLoadingContent(
    contentMaxWidth: Dp,
    onShareCardClick: () -> Unit,
    onNotificationClick: () -> Unit,
) {
    // Shimmer animation — linear loop
    val shimmerTransition = rememberInfiniteTransition(label = "shimmer")
    val shimmerOffset by shimmerTransition.animateFloat(
        initialValue = -1f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_offset"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .let { if (contentMaxWidth != Dp.Infinity) it.widthIn(max = contentMaxWidth) else it }
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Top bar skeleton
        HomeTopBar(
            dateLabel = "── ──────, ─────",
            onNotificationClick = onNotificationClick,
            onShareCardClick = onShareCardClick,
        )

        Spacer(Modifier.height(24.dp))

        // Constellation placeholder
        ShimmerBox(
            width = 200.dp, height = 200.dp,
            shape = RoundedCornerShape(999.dp),
            shimmerOffset = shimmerOffset
        )

        Spacer(Modifier.height(20.dp))

        // Sign name skeleton
        ShimmerBox(width = 120.dp, height = 24.dp, shimmerOffset = shimmerOffset)
        Spacer(Modifier.height(8.dp))
        ShimmerBox(width = 80.dp, height = 14.dp, shimmerOffset = shimmerOffset)

        Spacer(Modifier.height(28.dp))

        // Reading card skeleton
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(YzSurface)
                .border(0.5.dp, YzBorder, RoundedCornerShape(20.dp))
                .padding(20.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                ShimmerBox(width = 110.dp, height = 12.dp, shimmerOffset = shimmerOffset)
                repeat(5) {
                    ShimmerBox(
                        width = if (it == 4) 160.dp else Dp.Infinity,
                        height = 13.dp,
                        shimmerOffset = shimmerOffset,
                        modifier = if (it != 4) Modifier.fillMaxWidth() else Modifier
                    )
                }
            }
        }

        Spacer(Modifier.height(28.dp))

        // Energy grid skeleton
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ShimmerBox(width = 100.dp, height = 12.dp, shimmerOffset = shimmerOffset)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ShimmerBox(height = 90.dp, shimmerOffset = shimmerOffset,
                    modifier = Modifier.weight(1f), shape = RoundedCornerShape(20.dp))
                ShimmerBox(height = 90.dp, shimmerOffset = shimmerOffset,
                    modifier = Modifier.weight(1f), shape = RoundedCornerShape(20.dp))
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ShimmerBox(height = 90.dp, shimmerOffset = shimmerOffset,
                    modifier = Modifier.weight(1f), shape = RoundedCornerShape(20.dp))
                ShimmerBox(height = 90.dp, shimmerOffset = shimmerOffset,
                    modifier = Modifier.weight(1f), shape = RoundedCornerShape(20.dp))
            }
        }

        Spacer(Modifier.height(96.dp))
    }
}

@Composable
private fun ShimmerBox(
    shimmerOffset: Float,
    height: Dp,
    modifier: Modifier = Modifier,
    width: Dp = Dp.Unspecified,
    shape: RoundedCornerShape = RoundedCornerShape(6.dp),
) {
    val baseColor    = YzSurface
    val highlightColor = Color(0xFF1E2448)   // slightly brighter than YzSurface

    val brush = Brush.linearGradient(
        colors = listOf(baseColor, highlightColor, baseColor),
        start = Offset(shimmerOffset * 400f, 0f),
        end   = Offset(shimmerOffset * 400f + 400f, 0f)
    )

    Box(
        modifier = modifier
            .then(if (width != Dp.Infinity && width != Dp.Unspecified) Modifier.width(width) else Modifier)
            .height(height)
            .clip(shape)
            .background(brush)
    )
}

// ─────────────────────────────────────────────
// Error state
// ─────────────────────────────────────────────

@Composable
private fun HomeErrorContent(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.padding(40.dp)
        ) {
            Text(
                text = "✦",
                color = YzGold.copy(alpha = 0.4f),
                fontSize = 32.sp
            )
            Text(
                text = message,
                color = YzMuted,
                fontSize = 14.sp,
                lineHeight = 22.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(YzSurface)
                    .border(0.5.dp, YzBorder, RoundedCornerShape(16.dp))
                    .clickable(onClick = onRetry)
                    .padding(horizontal = 24.dp, vertical = 12.dp)
            ) {
                Text(
                    text = stringResource(Res.string.home_error_retry),
                    color = YzGold,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}