package com.kg.yildizname.feature.onboarding.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kg.yildizname.core.ui.components.PageIndicator
import com.kg.yildizname.core.ui.components.StarFieldBackground
import com.kg.yildizname.core.ui.components.YzButton
import com.kg.yildizname.core.ui.theme.YzBg
import com.kg.yildizname.core.ui.theme.YzBorder
import com.kg.yildizname.core.ui.theme.YzCardBg
import com.kg.yildizname.core.ui.theme.YzGold
import com.kg.yildizname.core.ui.theme.YzMuted
import com.kg.yildizname.core.ui.theme.YzOnSurface
import com.kg.yildizname.feature.onboarding.Gender
import com.kg.yildizname.feature.onboarding.OnboardingOptionalData
import horoscope.shared.generated.resources.Res
import horoscope.shared.generated.resources.gender_female
import horoscope.shared.generated.resources.gender_male
import horoscope.shared.generated.resources.gender_other
import horoscope.shared.generated.resources.gender_prefer_not
import horoscope.shared.generated.resources.onboarding_step3_birth_city_hint
import horoscope.shared.generated.resources.onboarding_step3_birth_time_hint
import horoscope.shared.generated.resources.onboarding_step3_gender_hint
import horoscope.shared.generated.resources.onboarding_step3_optional_badge
import horoscope.shared.generated.resources.onboarding_step3_skip
import horoscope.shared.generated.resources.onboarding_step3_start
import horoscope.shared.generated.resources.onboarding_step3_title
import org.jetbrains.compose.resources.stringResource
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

// ─── Gender label (composable, locale-aware) ──────────────────────────────────

@Composable
private fun Gender.displayLabel(): String = when (this) {
    Gender.MALE       -> stringResource(Res.string.gender_male)
    Gender.FEMALE     -> stringResource(Res.string.gender_female)
    Gender.OTHER      -> stringResource(Res.string.gender_other)
    Gender.PREFER_NOT -> stringResource(Res.string.gender_prefer_not)
}

// ─── Sparkle icon ─────────────────────────────────────────────────────────────

@Composable
private fun SparkleIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(64.dp)) {
        val cx = size.width / 2f
        val cy = size.height / 2f

        fun drawStar(cx: Float, cy: Float, outerR: Float, innerR: Float, color: Color) {
            val path = Path()
            for (i in 0 until 8) {
                val angle = (i * 45.0 - 90.0) * PI / 180.0
                val r     = if (i % 2 == 0) outerR else innerR
                val x     = cx + (r * cos(angle)).toFloat()
                val y     = cy + (r * sin(angle)).toFloat()
                if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            path.close()
            drawPath(path, color = color)
        }

        drawStar(cx, cy, outerR = 24.dp.toPx(), innerR = 6.dp.toPx(), color = YzGold)
        drawStar(
            cx + 20.dp.toPx(), cy - 18.dp.toPx(),
            outerR = 10.dp.toPx(), innerR = 2.5f.dp.toPx(),
            color = YzGold,
        )
    }
}

// ─── Input row ────────────────────────────────────────────────────────────────

@Composable
private fun InputRow(
    icon: @Composable () -> Unit,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier          = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(YzCardBg)
            .border(1.dp, YzBorder, RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        icon()
        Spacer(Modifier.width(12.dp))
        content()
    }
}

// ─── OnboardingStep3Screen ────────────────────────────────────────────────────

@Composable
fun OnboardingStep3Screen(
    optionalData: OnboardingOptionalData,
    onDataChanged: (OnboardingOptionalData) -> Unit,
    onStart: () -> Unit,
    onSkip: () -> Unit,
    error: String? = null,
    onErrorShown: () -> Unit = {},
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(error) {
        if (error != null) {
            snackbarHostState.showSnackbar(error)
            onErrorShown()
        }
    }

    var genderExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(YzBg),
    ) {
        StarFieldBackground(Modifier.fillMaxSize())

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 20.dp),
        ) {
            Spacer(Modifier.height(20.dp))

            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                PageIndicator(currentStep = 2)
            }

            Spacer(Modifier.height(32.dp))

            Text(
                text       = stringResource(Res.string.onboarding_step3_title),
                color      = YzGold,
                fontSize   = 28.sp,
                fontWeight = FontWeight.Bold,
                fontStyle  = FontStyle.Italic,
                lineHeight = 36.sp,
            )

            Spacer(Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(999.dp))
                    .border(1.dp, YzGold.copy(alpha = 0.5f), RoundedCornerShape(999.dp))
                    .padding(horizontal = 12.dp, vertical = 4.dp),
            ) {
                Text(
                    text       = stringResource(Res.string.onboarding_step3_optional_badge),
                    color      = YzGold.copy(alpha = 0.8f),
                    fontSize   = 12.sp,
                    fontWeight = FontWeight.Medium,
                )
            }

            Spacer(Modifier.height(32.dp))

            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                SparkleIcon()
            }

            Spacer(Modifier.height(36.dp))

            // Birth time
            InputRow(
                icon = {
                    Icon(
                        imageVector        = Icons.Outlined.Schedule,
                        contentDescription = null,
                        tint               = YzMuted,
                        modifier           = Modifier.size(20.dp),
                    )
                },
            ) {
                BasicTextField(
                    value         = optionalData.birthTime ?: "",
                    onValueChange = { onDataChanged(optionalData.copy(birthTime = it.ifBlank { null })) },
                    singleLine    = true,
                    textStyle     = TextStyle(color = YzOnSurface, fontSize = 15.sp),
                    decorationBox = { inner ->
                        if (optionalData.birthTime.isNullOrBlank()) {
                            Text(stringResource(Res.string.onboarding_step3_birth_time_hint), color = YzMuted, fontSize = 15.sp)
                        }
                        inner()
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            Spacer(Modifier.height(12.dp))

            // Birth city
            InputRow(
                icon = {
                    Icon(
                        imageVector        = Icons.Outlined.LocationOn,
                        contentDescription = null,
                        tint               = YzMuted,
                        modifier           = Modifier.size(20.dp),
                    )
                },
            ) {
                BasicTextField(
                    value         = optionalData.birthCity ?: "",
                    onValueChange = { onDataChanged(optionalData.copy(birthCity = it.ifBlank { null })) },
                    singleLine    = true,
                    textStyle     = TextStyle(color = YzOnSurface, fontSize = 15.sp),
                    decorationBox = { inner ->
                        if (optionalData.birthCity.isNullOrBlank()) {
                            Text(stringResource(Res.string.onboarding_step3_birth_city_hint), color = YzMuted, fontSize = 15.sp)
                        }
                        inner()
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            Spacer(Modifier.height(12.dp))

            // Gender dropdown
            Box {
                InputRow(
                    icon = {
                        Icon(
                            imageVector        = Icons.Outlined.Person,
                            contentDescription = null,
                            tint               = YzMuted,
                            modifier           = Modifier.size(20.dp),
                        )
                    },
                ) {
                    Text(
                        text     = optionalData.gender?.displayLabel()
                            ?: stringResource(Res.string.onboarding_step3_gender_hint),
                        color    = if (optionalData.gender != null) YzOnSurface else YzMuted,
                        fontSize = 15.sp,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { genderExpanded = true },
                    )
                    Icon(
                        imageVector        = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint               = YzMuted,
                        modifier           = Modifier
                            .size(20.dp)
                            .clickable { genderExpanded = true },
                    )
                }

                DropdownMenu(
                    expanded         = genderExpanded,
                    onDismissRequest = { genderExpanded = false },
                    modifier         = Modifier.background(Color(0xFF1C1F2F)),
                ) {
                    Gender.entries.forEach { g ->
                        DropdownMenuItem(
                            text    = { Text(g.displayLabel(), color = YzOnSurface, fontSize = 15.sp) },
                            onClick = {
                                onDataChanged(optionalData.copy(gender = g))
                                genderExpanded = false
                            },
                        )
                    }
                }
            }
        }

        Column(
            modifier            = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 20.dp)
                .widthIn(max = 480.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            YzButton(text = stringResource(Res.string.onboarding_step3_start), onClick = onStart)

            Spacer(Modifier.height(16.dp))

            Text(
                text          = stringResource(Res.string.onboarding_step3_skip),
                color         = YzMuted,
                fontSize      = 12.sp,
                fontWeight    = FontWeight.Medium,
                letterSpacing = 1.5.sp,
                textAlign     = TextAlign.Center,
                modifier      = Modifier
                    .clickable(onClick = onSkip)
                    .padding(8.dp),
            )
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier  = Modifier.align(Alignment.BottomCenter).padding(16.dp),
        )
    }
}
