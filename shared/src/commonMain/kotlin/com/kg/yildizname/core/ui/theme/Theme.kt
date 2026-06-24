package com.kg.yildizname.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf

private val DarkColorScheme = darkColorScheme(
    background       = YzBg,
    surface          = YzSurface,
    surfaceVariant   = YzSurfaceAlt,
    primary          = YzGold,
    onPrimary        = YzBg,
    secondary        = YzViolet,
    onSecondary      = YzInk,
    tertiary         = YzTertiary,
    onBackground     = YzInk,
    onSurface        = YzInk,
    onSurfaceVariant = YzMuted,
    outline          = YzBorder,
    outlineVariant   = YzBorder,
    error            = androidx.compose.ui.graphics.Color(0xFFCF6679)
)

@Immutable
data class YzExtendedColors(
    val gold:       androidx.compose.ui.graphics.Color = YzGold,
    val violet:     androidx.compose.ui.graphics.Color = YzViolet,
    val ink:        androidx.compose.ui.graphics.Color = YzInk,
    val muted:      androidx.compose.ui.graphics.Color = YzMuted,
    val tertiary:   androidx.compose.ui.graphics.Color = YzTertiary,
    val border:     androidx.compose.ui.graphics.Color = YzBorder,
    val surfaceAlt: androidx.compose.ui.graphics.Color = YzSurfaceAlt
)

val LocalYzColors  = staticCompositionLocalOf { YzExtendedColors() }
val LocalYzSpacing = staticCompositionLocalOf { YzSpacingValues() }

@Composable
fun YzTheme(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalYzColors   provides YzExtendedColors(),
        LocalYzSpacing  provides YzSpacing
    ) {
        MaterialTheme(
            colorScheme = DarkColorScheme,
            typography  = YzTypography,
            shapes      = YzShapes,
            content     = content
        )
    }
}

val MaterialTheme.yzColors: YzExtendedColors
    @Composable get() = LocalYzColors.current

val MaterialTheme.yzSpacing: YzSpacingValues
    @Composable get() = LocalYzSpacing.current
