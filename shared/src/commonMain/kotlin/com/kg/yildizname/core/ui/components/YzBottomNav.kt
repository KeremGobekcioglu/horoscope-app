package com.kg.yildizname.core.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kg.yildizname.core.ui.theme.*

/**
 * YzBottomNav — standalone bottom navigation bar.
 *
 * Hosted by the nav graph Scaffold. No screen owns this composable.
 * Visibility is controlled by the nav graph based on current route.
 *
 * Shown on:  Home, Calendar, Compatibility, Settings
 * Hidden on: Splash, Onboarding, ReadingDetail
 */
@Composable
fun YzBottomNav(
    currentRoute: String,
    onTabSelected: (route: String) -> Unit,
    items: List<YzBottomNavItemData>,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(YzSurface)
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        // Top hairline border — depth without shadows
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(0.5.dp)
                .background(YzBorder)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val isSelected = currentRoute.contains(item.route, ignoreCase = true)
                YzNavTab(
                    item = item,
                    isSelected = isSelected,
                    onClick = { if (!isSelected) onTabSelected(item.route) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun YzNavTab(
    item: YzBottomNavItemData,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.08f else 1f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = 400f),
        label = "tab_scale_${item.route}"
    )

    val iconTint   = if (isSelected) YzGold else YzMuted
    val labelColor = if (isSelected) YzGold else YzMuted

    Column(
        modifier = modifier
            .scale(scale)
            .clip(RoundedCornerShape(12.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.label,
            tint = iconTint,
            modifier = Modifier.size(22.dp)
        )

        Text(
            text = item.label,
            color = labelColor,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            letterSpacing = 0.3.sp,
            maxLines = 1
        )

        // Selected indicator — gold pill under active tab
        Box(
            modifier = Modifier
                .size(width = 16.dp, height = 2.dp)
                .clip(RoundedCornerShape(999.dp))
                .background(if (isSelected) YzGold else Color.Transparent)
        )
    }
}

/**
 * Data for a single bottom nav tab.
 * Constructed in the nav graph — icons and labels resolved there.
 */
data class YzBottomNavItemData(
    val route: String,       // must match the route string in the back stack
    val label: String,       // already-resolved string (use stringResource at call site)
    val icon: ImageVector,   // Feather icon — no asset files needed
)