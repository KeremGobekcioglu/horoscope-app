package com.kg.yildizname.core.ui.theme

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring

object YzMotion {
    val springSpec = spring<Float>(
        dampingRatio = 0.7f,
        stiffness    = Spring.StiffnessMediumLow
    )

    val easeOutCubic  = CubicBezierEasing(0.33f, 1f, 0.68f, 1f)
    val easeOutExpo   = CubicBezierEasing(0.19f, 1f, 0.22f, 1f)
    val easeInOutSine = CubicBezierEasing(0.37f, 0f, 0.63f, 1f)

    const val scoreCountUpMs  = 800
    const val compatRevealMs  = 1200
    const val slideTransitionMs = 350
    const val shimmerMs       = 1200
    const val particleBurstMs = 600
    const val twinkleMinMs    = 2000
    const val twinkleMaxMs    = 4000
}
