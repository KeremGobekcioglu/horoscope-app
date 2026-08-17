package com.kg.yildizname.feature.share.ui.domain

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.kg.yildizname.platform.ShareTarget

data class ShareOption(
    val target: ShareTarget,
    val label: String,
    val icon: ImageVector,
    val background: Brush,
    val iconTint: Color = Color.White
)
