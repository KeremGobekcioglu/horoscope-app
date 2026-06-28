package com.kg.yildizname.core.ui.components

import com.kg.yildizname.core.ui.theme.YzBorder
import com.kg.yildizname.core.ui.theme.YzGold
import com.kg.yildizname.core.ui.theme.YzMuted
import com.kg.yildizname.core.ui.theme.YzSurface
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


/**
 * Bottom navigation destination descriptor.
 *
 * @param route        The typed route this tab represents — pass the route object's class name
 *                     or whatever the nav graph uses to identify the current destination.
 * @param labelRes     String resource for the tab label.
 * @param iconRes      Drawable resource for the unselected icon.
 *                     You can swap to separate selected/unselected resources if desired.
 */
data class BottomNavItem(
    val route: String,
    val labelResKey: String,         // used for matching currentRoute
    val iconContentDescription: String
)

/**
 * YzBottomNav — standalone bottom navigation bar.
 *
 * The nav graph hosts this composable and passes [currentRoute] + [onTabSelected].
 * HomeScreen (and other screens) do NOT own the bottom bar.
 *
 * Usage in nav graph scaffold:
 *
 *   Scaffold(
 *       bottomBar = {
 *           YzBottomNav(
 *               currentRoute = currentBackStackEntry?.destination?.route ?: "",
 *               onTabSelected = { route -> navController.navigate(route) { ... } }
 *           )
 *       }
 *   ) { ... }
 *
 * Icons: drop your vector drawables into composeResources/drawable/ and wire them
 * into the [items] list below. Each item expects a [Painter] for flexibility.
 */
@Composable
fun YzBottomNav(
    currentRoute: String,
    onTabSelected: (route: String) -> Unit,
    modifier: Modifier = Modifier,
    // Pass your actual painters from the call site once assets are ready.
    // Signature kept flexible so you swap icon painters without touching this composable.
    items: List<YzBottomNavItemData>,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(YzSurface)         // #0F1428 — one step above background
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        // Top hairline border — no drop shadows in this design system
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(0.5.dp)
                .background(YzBorder)      // #1E2240
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
    // Springy scale pop on selection
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.08f else 1f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = 400f),
        label = "tab_scale_${item.route}"
    )

    val labelColor = if (isSelected) YzGold else YzMuted
    val iconTint   = if (isSelected) YzGold else YzMuted

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
            painter = item.icon,
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

        // Gold underline dot for selected tab — subtle, not a bar
        Box(
            modifier = Modifier
                .size(width = 16.dp, height = 2.dp)
                .clip(RoundedCornerShape(999.dp))
                .background(if (isSelected) YzGold else Color.Transparent)
        )
    }
}

/**
 * Data holder for a single bottom nav item.
 * Keep this in the nav graph layer — the tab's icon and label are resolved there.
 */
data class YzBottomNavItemData(
    val route: String,
    val label: String,
    val icon: Painter,
)