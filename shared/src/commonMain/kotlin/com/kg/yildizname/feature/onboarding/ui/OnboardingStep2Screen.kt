package com.kg.yildizname.feature.onboarding.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kg.yildizname.core.ui.components.PageIndicator
import com.kg.yildizname.core.ui.components.StarFieldBackground
import com.kg.yildizname.core.ui.components.YzButton
import com.kg.yildizname.core.ui.theme.YzBg
import com.kg.yildizname.core.ui.theme.YzGold
import com.kg.yildizname.core.ui.theme.YzMuted
import com.kg.yildizname.core.ui.theme.YzOnSurface
import com.kg.yildizname.core.ui.theme.YzPickerBg
import com.kg.yildizname.feature.onboarding.BirthDate
import horoscope.shared.generated.resources.Res
import horoscope.shared.generated.resources.month_april
import horoscope.shared.generated.resources.month_august
import horoscope.shared.generated.resources.month_december
import horoscope.shared.generated.resources.month_february
import horoscope.shared.generated.resources.month_january
import horoscope.shared.generated.resources.month_july
import horoscope.shared.generated.resources.month_june
import horoscope.shared.generated.resources.month_march
import horoscope.shared.generated.resources.month_may
import horoscope.shared.generated.resources.month_november
import horoscope.shared.generated.resources.month_october
import horoscope.shared.generated.resources.month_september
import horoscope.shared.generated.resources.onboarding_continue
import horoscope.shared.generated.resources.onboarding_skip
import horoscope.shared.generated.resources.onboarding_step2_subtitle
import horoscope.shared.generated.resources.onboarding_step2_title
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import kotlin.math.absoluteValue

// ─── Data ─────────────────────────────────────────────────────────────────────

private val DAYS  = (1..31).map { it.toString().padStart(2, '0') }
private val YEARS = (1930..2015).map { it.toString() }.reversed()

private val DEFAULT_DATE = BirthDate(day = 1, month = 1, year = 2000)

// ─── Drum column ──────────────────────────────────────────────────────────────

private const val ITEM_HEIGHT_DP = 48
private const val VISIBLE_ITEMS  = 7
private const val PADDING_ITEMS  = VISIBLE_ITEMS / 2

@Composable
private fun DrumColumn(
    items: List<String>,
    selectedIndex: Int,
    onIndexSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState    = rememberLazyListState()
    val scope        = rememberCoroutineScope()
    val itemHeightDp = ITEM_HEIGHT_DP.dp
    val itemHeightPx = with(LocalDensity.current) { ITEM_HEIGHT_DP.dp.toPx() }

    LaunchedEffect(selectedIndex) {
        listState.scrollToItem(selectedIndex)
    }

    LaunchedEffect(listState.isScrollInProgress) {
        if (!listState.isScrollInProgress) {
            val firstVisible = listState.firstVisibleItemIndex
            val offset       = listState.firstVisibleItemScrollOffset
            val snappedIndex = if (offset > itemHeightPx / 2) firstVisible + 1 else firstVisible
            val clamped      = snappedIndex.coerceIn(0, items.size - 1)
            scope.launch { listState.animateScrollToItem(clamped) }
            onIndexSelected(clamped)
        }
    }

    Box(modifier = modifier.height(itemHeightDp * VISIBLE_ITEMS)) {
        LazyColumn(
            state          = listState,
            contentPadding = PaddingValues(vertical = itemHeightDp * PADDING_ITEMS),
            modifier       = Modifier.fillMaxSize(),
        ) {
            items(items.size) { idx ->
                val distance = (idx - (listState.firstVisibleItemIndex + PADDING_ITEMS))
                    .absoluteValue.coerceAtMost(PADDING_ITEMS)
                val alpha    = 1f - (distance / PADDING_ITEMS.toFloat()) * 0.75f
                val isCenter = idx == selectedIndex

                Box(
                    modifier         = Modifier
                        .fillMaxWidth()
                        .height(itemHeightDp)
                        .clickable {
                            scope.launch { listState.animateScrollToItem(idx) }
                            onIndexSelected(idx)
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text       = items[idx],
                        color      = if (isCenter) YzGold else YzOnSurface.copy(alpha = alpha),
                        fontSize   = if (isCenter) 22.sp else 18.sp,
                        fontWeight = if (isCenter) FontWeight.Bold else FontWeight.Normal,
                        textAlign  = TextAlign.Center,
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeightDp * PADDING_ITEMS)
                .align(Alignment.TopCenter)
                .background(Brush.verticalGradient(listOf(YzPickerBg, Color.Transparent)))
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeightDp * PADDING_ITEMS)
                .align(Alignment.BottomCenter)
                .background(Brush.verticalGradient(listOf(Color.Transparent, YzPickerBg)))
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .align(Alignment.TopCenter)
                .offset(y = itemHeightDp * PADDING_ITEMS)
                .background(YzGold.copy(alpha = 0.6f))
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .align(Alignment.TopCenter)
                .offset(y = itemHeightDp * (PADDING_ITEMS + 1))
                .background(YzGold.copy(alpha = 0.6f))
        )
    }
}

// ─── OnboardingStep2Screen ────────────────────────────────────────────────────

@Composable
fun OnboardingStep2Screen(
    selectedDate: BirthDate?,
    onDateChanged: (BirthDate) -> Unit,
    onContinue: () -> Unit,
    onSkip: () -> Unit,
) {
    val months = listOf(
        stringResource(Res.string.month_january),
        stringResource(Res.string.month_february),
        stringResource(Res.string.month_march),
        stringResource(Res.string.month_april),
        stringResource(Res.string.month_may),
        stringResource(Res.string.month_june),
        stringResource(Res.string.month_july),
        stringResource(Res.string.month_august),
        stringResource(Res.string.month_september),
        stringResource(Res.string.month_october),
        stringResource(Res.string.month_november),
        stringResource(Res.string.month_december),
    )

    val date = selectedDate ?: DEFAULT_DATE

    var dayIndex   by remember(date) { mutableStateOf(date.day - 1) }
    var monthIndex by remember(date) { mutableStateOf(date.month - 1) }
    var yearIndex  by remember(date) {
        mutableStateOf(YEARS.indexOf(date.year.toString()).coerceAtLeast(0))
    }

    LaunchedEffect(dayIndex, monthIndex, yearIndex) {
        onDateChanged(
            BirthDate(
                day   = dayIndex + 1,
                month = monthIndex + 1,
                year  = YEARS[yearIndex].toInt(),
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(YzBg),
    ) {
        StarFieldBackground(Modifier.fillMaxSize())

        Column(
            modifier            = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(20.dp))
            PageIndicator(currentStep = 1)
            Spacer(Modifier.height(56.dp))

            Text(
                text       = stringResource(Res.string.onboarding_step2_title),
                color      = YzGold,
                fontSize   = 32.sp,
                fontWeight = FontWeight.Bold,
                textAlign  = TextAlign.Center,
            )

            Spacer(Modifier.height(10.dp))

            Text(
                text          = stringResource(Res.string.onboarding_step2_subtitle),
                color         = YzMuted,
                fontSize      = 11.sp,
                fontWeight    = FontWeight.Medium,
                letterSpacing = 1.5.sp,
                textAlign     = TextAlign.Center,
            )

            Spacer(Modifier.height(48.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(YzPickerBg),
            ) {
                Row(
                    modifier          = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    DrumColumn(
                        items           = DAYS,
                        selectedIndex   = dayIndex,
                        onIndexSelected = { dayIndex = it },
                        modifier        = Modifier.weight(1f),
                    )
                    DrumColumn(
                        items           = months,
                        selectedIndex   = monthIndex,
                        onIndexSelected = { monthIndex = it },
                        modifier        = Modifier.weight(1.4f),
                    )
                    DrumColumn(
                        items           = YEARS,
                        selectedIndex   = yearIndex,
                        onIndexSelected = { yearIndex = it },
                        modifier        = Modifier.weight(1f),
                    )
                }
            }

            Spacer(Modifier.height(32.dp))

            Text(
                text          = stringResource(Res.string.onboarding_skip),
                color         = YzMuted,
                fontSize      = 12.sp,
                fontWeight    = FontWeight.Medium,
                letterSpacing = 1.5.sp,
                modifier      = Modifier
                    .clickable(onClick = onSkip)
                    .padding(8.dp),
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Brush.verticalGradient(listOf(Color.Transparent, YzBg, YzBg)))
                .padding(horizontal = 20.dp, vertical = 20.dp),
        ) {
            YzButton(text = stringResource(Res.string.onboarding_continue), onClick = onContinue)
        }
    }
}
